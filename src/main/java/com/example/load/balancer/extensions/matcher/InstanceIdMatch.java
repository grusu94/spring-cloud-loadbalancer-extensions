package com.example.load.balancer.extensions.matcher;

import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.client.ServiceInstance;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class InstanceIdMatch implements LoadBalancingStrategy {

    /**
     * The expected instance id to match.
     */
    private final String expectedInstanceId;

    /**
     * Sole constructor.
     *
     * @param expectedInstanceId The expected instance id to match.
     */
    public InstanceIdMatch(@NotNull String expectedInstanceId) {
        this.expectedInstanceId = expectedInstanceId;
    }

    @Override
    public List<ServiceInstance> apply(List<ServiceInstance> instances) {
        return instances.stream()
                .filter(i -> expectedInstanceId.equals(i.getInstanceId()))
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return format("InstanceIdMatcher[instanceId=%s]", expectedInstanceId);
    }
}
