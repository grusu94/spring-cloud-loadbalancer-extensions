package com.example.load.balancer.extensions.matcher;

import org.springframework.cloud.client.ServiceInstance;

import java.util.List;

public class CompositeStrategy implements LoadBalancingStrategy {

    private final List<LoadBalancingStrategy> components;

    public CompositeStrategy(List<LoadBalancingStrategy> components) {
        this.components = components;
    }

    @Override
    public List<ServiceInstance> apply(List<ServiceInstance> instances) {
        List<ServiceInstance> result = instances;
        for (LoadBalancingStrategy strategy : components) {
            result = strategy.apply(result);
            if (result.isEmpty()) {
                break;
            }
        }
        return result;
    }
}
