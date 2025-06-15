package com.github.grusu94.spring.cloud.loadbalancer.extensions.support.strategy;

import com.github.grusu94.spring.cloud.loadbalancer.extensions.propagator.feign.PreservesHttpHeadersFeignInterceptor;
import com.github.grusu94.spring.cloud.loadbalancer.extensions.support.EurekaInstanceProperties;
import com.github.grusu94.spring.cloud.loadbalancer.extensions.support.PropagationProperties;
import feign.Feign;
import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Default Feign propagation strategy based on execution context copy to the feign headers.
 */
@Configuration
@ConditionalOnClass(Feign.class)
@ConditionalOnProperty(value = "loadbalancer.extensions.propagation.feign.enabled", matchIfMissing = true)
@ConditionalOnExpression(value = "${loadbalancer.extensions.propagation.enabled:true}")
@Slf4j
public class PreservesHttpHeadersFeignStrategy {

    private final PropagationProperties properties;
    private final EurekaInstanceProperties eurekaInstanceProperties;

    public PreservesHttpHeadersFeignStrategy(PropagationProperties properties, EurekaInstanceProperties eurekaInstanceProperties) {
        this.properties = properties;
        this.eurekaInstanceProperties = eurekaInstanceProperties;
    }

    /**
     * @return the feign http headers interceptor
     * @see PreservesHttpHeadersFeignInterceptor
     */
    @Bean
    public RequestInterceptor feignPropagator() {
        log.info("Context propagation enabled for feign clients on keys={}: url-includes{},url-excludes{}",
                properties.getKeys(), properties.getFeign().getIncludes(), properties.getFeign().getExcludes());
        return new PreservesHttpHeadersFeignInterceptor(properties.getFeign(),
                properties.buildEntriesFilter(),
                properties.buildExtraStaticEntries(eurekaInstanceProperties));
    }
}
