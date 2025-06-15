package com.github.grusu94.spring.cloud.loadbalancer.extensions.matcher;

import org.springframework.cloud.client.ServiceInstance;

import java.util.List;

public interface LoadBalancingStrategyMatcher {

    /**
     * Filters or ranks service instances.
     *
     * @param instances all available service instances
     * @return a filtered or ordered list of instances
     */
    List<ServiceInstance> apply(List<ServiceInstance> instances);

    /**
     * Optional: strategy name or order for logging/debugging
     */
    default String name() {
        return getClass().getSimpleName();
    }
}
