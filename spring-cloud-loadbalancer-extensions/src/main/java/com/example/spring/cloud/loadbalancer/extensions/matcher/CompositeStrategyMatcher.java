package com.example.spring.cloud.loadbalancer.extensions.matcher;

import org.springframework.cloud.client.ServiceInstance;

import java.util.List;

public class CompositeStrategyMatcher implements LoadBalancingStrategyMatcher {

    private final List<LoadBalancingStrategyMatcher> components;

    public CompositeStrategyMatcher(List<LoadBalancingStrategyMatcher> components) {
        this.components = components;
    }

    @Override
    public List<ServiceInstance> apply(List<ServiceInstance> instances) {
        List<ServiceInstance> result = instances;
        for (LoadBalancingStrategyMatcher strategy : components) {
            result = strategy.apply(instances);
            if (!result.isEmpty()) {
                return result;
            }
        }
        return result;
    }
}
