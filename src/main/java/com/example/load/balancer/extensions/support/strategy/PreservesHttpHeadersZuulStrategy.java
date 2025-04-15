package com.example.load.balancer.extensions.support.strategy;

import com.example.load.balancer.extensions.propagator.servlet.PreservesHttpHeadersInterceptor;
import com.example.load.balancer.extensions.propagator.zuul.GatewayHeadersEnricher;
import com.example.load.balancer.extensions.support.EurekaInstanceProperties;
import com.example.load.balancer.extensions.support.PropagationProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

/**
 * Default Zuul propagation strategy based on http request headers copy to the execution context.
 */
@Configuration
@ConditionalOnProperty(value = "loadbalancer.extensions.propagation.gateway.enabled", matchIfMissing = true)
@ConditionalOnExpression(value = "${loadbalancer.extensions.propagation.enabled:true}")
@Slf4j
public class PreservesHttpHeadersZuulStrategy implements InstantiationAwareBeanPostProcessor {
    @Autowired
    private PropagationProperties properties;
    @Autowired
    private EurekaInstanceProperties eurekaInstanceProperties;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) {
        if (bean instanceof ZuulHandlerMapping) {
            ZuulHandlerMapping zuulHandlerMapping = (ZuulHandlerMapping) bean;
            zuulHandlerMapping.setInterceptors(new PreservesHttpHeadersInterceptor(properties.buildEntriesFilter()));
            zuulHandlerMapping.setInterceptors(new GatewayHeadersEnricher(properties.buildEntriesFilter(), properties.buildExtraStaticEntries(eurekaInstanceProperties)));
            log.info("Context propagation enabled for zuul handler[{}] on keys={}.", beanName, properties.getKeys());
        }
        return true;
    }
}
