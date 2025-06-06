package com.example.spring.cloud.loadbalancer.extensions.support.strategy;

import com.example.spring.cloud.loadbalancer.extensions.propagator.servlet.PreservesHttpHeadersInterceptor;
import com.example.spring.cloud.loadbalancer.extensions.support.PropagationProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Default inbound http request propagation strategy based on http request headers copy to the execution context.
 */
@Configuration
@ConditionalOnWebApplication
@ConditionalOnProperty(value = "loadbalancer.extensions.propagation.http.enabled", matchIfMissing = true)
@ConditionalOnExpression(value = "${loadbalancer.extensions.propagation.enabled:true}")
@Slf4j
public class PreservesHeadersInboundHttpRequestStrategy implements WebMvcConfigurer {

    private final PropagationProperties properties;

    public PreservesHeadersInboundHttpRequestStrategy(PropagationProperties properties) {
        this.properties = properties;
    }

    /**
     * Adds http request interceptor copying headers from the request to the context
     *
     * @param registry the interceptor registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new PreservesHttpHeadersInterceptor(properties.buildEntriesFilter())).addPathPatterns("/**");
        log.info("Context propagation ENABLED for http request on keys={}.", properties.getKeys());
    }
}
