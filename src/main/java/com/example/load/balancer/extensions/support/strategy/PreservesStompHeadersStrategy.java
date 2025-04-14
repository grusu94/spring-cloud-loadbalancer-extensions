package com.example.load.balancer.extensions.support.strategy;

import com.example.load.balancer.extensions.propagator.stomp.PreservesHeadersStompSessionAdapter;
import com.example.load.balancer.extensions.support.PropagationProperties;
import com.example.load.balancer.extensions.support.EurekaInstanceProperties;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.stomp.StompSession;

/**
 * Default Stomp propagation strategy based on stomp headers copy from/to the execution context.
 */
@Configuration
@ConditionalOnClass(StompSession.class)
@ConditionalOnProperty(value = "ribbon.extensions.propagation.stomp.enabled", matchIfMissing = true)
@ConditionalOnExpression(value = "${ribbon.extensions.propagation.enabled:true}")
@Slf4j
public class PreservesStompHeadersStrategy extends InstantiationAwareBeanPostProcessorAdapter {
    @Autowired
    @Setter
    private PropagationProperties propagationProperties;
    @Autowired
    @Setter
    private EurekaInstanceProperties eurekaInstanceProperties;

    /**
     * {@inheritDoc}
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        if (bean instanceof StompSession && !(bean instanceof PreservesHeadersStompSessionAdapter)) {
            if (propagationProperties.getStomp().accept(beanName)) {
                log.info("Context propagation enabled for stomp session [{}] on keys={}.", beanName, propagationProperties.getKeys());
                return new PreservesHeadersStompSessionAdapter((StompSession) bean,
                        propagationProperties.buildEntriesFilter(),
                        propagationProperties.buildExtraStaticEntries(eurekaInstanceProperties));
            } else {
                log.debug("Context propagation disabled for stomp session [{}]", beanName);
            }
        }
        return bean;
    }
}
