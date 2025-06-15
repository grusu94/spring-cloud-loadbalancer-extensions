package com.github.grusu94.spring.cloud.loadbalancer.extensions.support.strategy;

import com.github.grusu94.spring.cloud.loadbalancer.extensions.propagator.gateway.GatewayHeadersEnricher;
import com.github.grusu94.spring.cloud.loadbalancer.extensions.propagator.gateway.PreservesGatewayHttpHeadersInterceptor;
import com.github.grusu94.spring.cloud.loadbalancer.extensions.support.EurekaInstanceProperties;
import com.github.grusu94.spring.cloud.loadbalancer.extensions.support.PropagationProperties;
import lombok.extern.slf4j.Slf4j;
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
    private final PropagationProperties properties;
    private final EurekaInstanceProperties eurekaInstanceProperties;

    public PreservesHttpHeadersGatewayStrategy(PropagationProperties properties, EurekaInstanceProperties eurekaInstanceProperties) {
        this.properties = properties;
        this.eurekaInstanceProperties = eurekaInstanceProperties;
    }

    @Bean
    public GlobalFilter httpHeadersToContext() {
        log.info("Context propagation ENABLED for http request (GatewayStrategy) on keys={}.", properties.getKeys());
        return new PreservesGatewayHttpHeadersInterceptor(properties.buildEntriesFilter());
    }

    @Bean
    public GlobalFilter httpHeadersEnricher() {
        log.info("Context propagation ENABLED for gateway enricher (GatewayStrategy) on keys={}.", properties.getKeys());
        return new GatewayHeadersEnricher(properties.buildEntriesFilter(),
                properties.buildExtraStaticEntries(eurekaInstanceProperties));
    }
}
