package com.example.load.balancer.extensions.matcher;

import org.springframework.cloud.client.ServiceInstance;

import java.util.List;
import java.util.stream.Collectors;

public class AvailabilityMatch implements LoadBalancingStrategy {

    @Override
    public List<ServiceInstance> apply(List<ServiceInstance> instances) {
        return instances.stream()
                .filter(this::isAvailable)
                .collect(Collectors.toList());
    }

    private boolean isAvailable(ServiceInstance instance) {
        String status = instance.getMetadata().getOrDefault("status", "UP");
        return "UP".equalsIgnoreCase(status);
    }
}
