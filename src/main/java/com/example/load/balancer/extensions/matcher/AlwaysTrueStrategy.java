package com.example.load.balancer.extensions.matcher;

import org.springframework.cloud.client.ServiceInstance;

import java.util.List;

public class AlwaysTrueStrategy implements LoadBalancingStrategy {

    @Override
    public List<ServiceInstance> apply(List<ServiceInstance> instances) {
        return instances;
    }
}
