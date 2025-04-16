package com.example.load.balancer.extensions.support.strategy;

import com.example.load.balancer.extensions.propagator.servlet.PreservesHttpHeadersInterceptor;
import com.example.load.balancer.extensions.propagator.gateway.GatewayHeadersEnricher;
import com.example.load.balancer.extensions.support.EurekaInstanceProperties;
import com.example.load.balancer.extensions.support.PropagationProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Default Gateway propagation strategy based on http request headers copy to the execution context.
 */
@Configuration
@ConditionalOnProperty(value = "loadbalancer.extensions.propagation.gateway.enabled", matchIfMissing = true)
@ConditionalOnExpression(value = "${loadbalancer.extensions.propagation.enabled:true}")
@Slf4j
public class PreservesHttpHeadersGatewayStrategy {
    @Autowired
    private PropagationProperties properties;
    @Autowired
    private EurekaInstanceProperties eurekaInstanceProperties;

    @Bean
    public GlobalFilter httpHeadersToContext() {
        log.info("Context propagation enabled for http request (GatewayStrategy) on keys={}.", properties.getKeys());
        return new PreservesHttpHeadersInterceptor(properties.buildEntriesFilter());
    }

    @Bean
    public GlobalFilter httpHeadersEnricher() {
        log.info("Context propagation enabled for gateway enricher (GatewayStrategy) on keys={}.", properties.getKeys());
        return new GatewayHeadersEnricher(properties.buildEntriesFilter(), properties.buildExtraStaticEntries(eurekaInstanceProperties));
    }
}
