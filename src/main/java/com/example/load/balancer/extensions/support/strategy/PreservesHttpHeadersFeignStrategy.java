package com.example.load.balancer.extensions.support.strategy;

import com.example.load.balancer.extensions.propagator.feign.PreservesHttpHeadersFeignInterceptor;
import com.example.load.balancer.extensions.support.PropagationProperties;
import com.example.load.balancer.extensions.support.EurekaInstanceProperties;
import feign.Feign;
import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
@ConditionalOnProperty(value = "ribbon.extensions.propagation.feign.enabled", matchIfMissing = true)
@ConditionalOnExpression(value = "${ribbon.extensions.propagation.enabled:true}")
@Slf4j
public class PreservesHttpHeadersFeignStrategy {
    @Autowired
    private PropagationProperties properties;
    @Autowired
    private EurekaInstanceProperties eurekaInstanceProperties;

    /**
     * @return the feign http headers interceptor
     * @see PreservesHttpHeadersFeignInterceptor
     */
    @Bean
    public RequestInterceptor feignPropagator() {
        log.info("Context propagation enabled for feign clients on keys={}: url-includes{},url-excludes{}", properties.getKeys(), properties.getFeign().getIncludes(), properties.getFeign().getExcludes());
        return new PreservesHttpHeadersFeignInterceptor(properties.getFeign(),
                properties.buildEntriesFilter(),
                properties.buildExtraStaticEntries(eurekaInstanceProperties));
    }
}
