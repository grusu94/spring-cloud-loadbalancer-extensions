package com.github.grusu94.spring.cloud.loadbalancer.extensions.support.strategy;

import com.github.grusu94.spring.cloud.loadbalancer.extensions.propagator.jms.MessagePropertyEncoder;
import com.github.grusu94.spring.cloud.loadbalancer.extensions.propagator.jms.PreservesMessagePropertiesConnectionFactoryAdapter;
import com.github.grusu94.spring.cloud.loadbalancer.extensions.support.InstanceProperties;
import com.github.grusu94.spring.cloud.loadbalancer.extensions.support.PropagationProperties;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;

import jakarta.jms.ConnectionFactory;
import java.lang.reflect.InvocationTargetException;

/**
 * Default jms propagation strategy based on jms propagationProperties copy from/to the execution context.
 */
@Configuration
@ConditionalOnClass(PreservesMessagePropertiesConnectionFactoryAdapter.class)
@ConditionalOnProperty(value = "loadbalancer.extensions.propagation.jms.enabled", matchIfMissing = true)
@ConditionalOnExpression(value = "${loadbalancer.extensions.propagation.enabled:true}")
@Setter
@Slf4j
public class PreservesJmsMessagePropertiesStrategy implements InstantiationAwareBeanPostProcessor {

    private PropagationProperties properties;
    private InstanceProperties instanceProperties;

    public PreservesJmsMessagePropertiesStrategy(PropagationProperties properties, InstanceProperties instanceProperties) {
        this.properties = properties;
        this.instanceProperties = instanceProperties;
    }

    @Value("${loadbalancer.extensions.propagation.jms.encoder:com.github.grusu94.spring.cloud.loadbalancer.extensions.propagator.jms.SimpleMessagePropertyEncoder}")
    private Class<? extends MessagePropertyEncoder> encoderType;

    private MessagePropertyEncoder encoder = null;

    private MessagePropertyEncoder getEncoder() {
        if (encoder == null) {
            try {
                encoder = encoderType.getDeclaredConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                throw new IllegalArgumentException("message property encoder '" + encoderType + "' should be accessible with a default constructor");
            }
        }
        return encoder;
    }

    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) {
        if (bean instanceof ConnectionFactory && !(bean instanceof PreservesMessagePropertiesConnectionFactoryAdapter)) {
            if (properties.getJms().accept(beanName)) {
                log.info("Context propagation ENABLED for jms connection factory [{}] on keys={}.", beanName,
                        properties.getKeys());
                return new PreservesMessagePropertiesConnectionFactoryAdapter((ConnectionFactory) bean,
                        properties.buildEntriesFilter(),
                        properties.buildExtraStaticEntries(instanceProperties),
                        getEncoder());
            } else {
                log.debug("Context propagation DISABLED for jms connection factory [{}]", beanName);
            }
        }
        return bean;
    }
}
