package com.example.spring.cloud.loadbalancer.extensions.support.strategy;

import com.example.spring.cloud.loadbalancer.extensions.propagator.stomp.PreservesHeadersStompSessionAdapter;
import com.example.spring.cloud.loadbalancer.extensions.support.EurekaInstanceProperties;
import com.example.spring.cloud.loadbalancer.extensions.support.PropagationProperties;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.messaging.simp.stomp.StompSession;

/**
 * Default Stomp propagation strategy based on stomp headers copy from/to the execution context.
 */
@Configuration
@ConditionalOnClass(StompSession.class)
@ConditionalOnProperty(value = "loadbalancer.extensions.propagation.stomp.enabled", matchIfMissing = true)
@ConditionalOnExpression(value = "${loadbalancer.extensions.propagation.enabled:true}")
@Setter
@Slf4j
public class PreservesStompHeadersStrategy implements InstantiationAwareBeanPostProcessor {

    private PropagationProperties propagationProperties;
    private EurekaInstanceProperties eurekaInstanceProperties;

    public PreservesStompHeadersStrategy(PropagationProperties propagationProperties, EurekaInstanceProperties eurekaInstanceProperties) {
        this.propagationProperties = propagationProperties;
        this.eurekaInstanceProperties = eurekaInstanceProperties;
    }

    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) {
        if (bean instanceof StompSession && !(bean instanceof PreservesHeadersStompSessionAdapter)) {
            if (propagationProperties.getStomp().accept(beanName)) {
                log.info("Context propagation ENABLED for stomp session [{}] on keys={}.", beanName,
                        propagationProperties.getKeys());
                return new PreservesHeadersStompSessionAdapter((StompSession) bean,
                        propagationProperties.buildEntriesFilter(),
                        propagationProperties.buildExtraStaticEntries(eurekaInstanceProperties));
            } else {
                log.debug("Context propagation DISABLED for stomp session [{}]", beanName);
            }
        }
        return bean;
    }
}
