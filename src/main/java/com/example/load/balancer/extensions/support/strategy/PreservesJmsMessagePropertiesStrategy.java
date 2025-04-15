package com.example.load.balancer.extensions.support.strategy;

import com.example.load.balancer.extensions.propagator.jms.MessagePropertyEncoder;
import com.example.load.balancer.extensions.propagator.jms.PreservesMessagePropertiesConnectionFactoryAdapter;
import com.example.load.balancer.extensions.support.EurekaInstanceProperties;
import com.example.load.balancer.extensions.support.PropagationProperties;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import javax.jms.ConnectionFactory;

/**
 * Default jms propagation strategy based on jms propagationProperties copy from/to the execution context.
 */
@Configuration
@ConditionalOnClass(PreservesMessagePropertiesConnectionFactoryAdapter.class)
@ConditionalOnProperty(value = "loadbalancer.extensions.propagation.jms.enabled", matchIfMissing = true)
@ConditionalOnExpression(value = "${loadbalancer.extensions.propagation.enabled:true}")
@Slf4j
public class PreservesJmsMessagePropertiesStrategy implements InstantiationAwareBeanPostProcessor {
    @Autowired
    private PropagationProperties properties;
    @Autowired
    private EurekaInstanceProperties eurekaInstanceProperties;

    @Value("${ribbon.extensions.propagation.jms.encoder:com.github.enadim.spring.cloud.ribbon.propagator.jms.SimpleMessagePropertyEncoder}")
    @Setter
    private Class<? extends MessagePropertyEncoder> encoderType;

    private MessagePropertyEncoder encoder = null;

    private MessagePropertyEncoder getEncoder() {
        if (encoder == null) {
            try {
                encoder = encoderType.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new IllegalArgumentException("message property encoder '" + encoderType + "' should be accessible with a default constructor");
            }
        }
        return encoder;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        if (bean instanceof ConnectionFactory && !(bean instanceof PreservesMessagePropertiesConnectionFactoryAdapter)) {
            if (properties.getJms().accept(beanName)) {
                log.info("Context propagation enabled for jms connection factory [{}] on keys={}.", beanName, properties.getKeys());
                return new PreservesMessagePropertiesConnectionFactoryAdapter((ConnectionFactory) bean,
                        properties.buildEntriesFilter(),
                        properties.buildExtraStaticEntries(eurekaInstanceProperties),
                        getEncoder());
            } else {
                log.debug("Context propagation disabled for jms connection factory [{}]", beanName);
            }
        }
        return bean;
    }
}
