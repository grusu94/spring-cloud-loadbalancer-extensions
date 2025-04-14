package com.example.load.balancer.extensions.matcher;

import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.client.ServiceInstance;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class ZoneAffinityMatch implements LoadBalancingStrategy {

    /**
     * the application zone.
     */
    private final String zone;

    /**
     * Constructor.
     *
     * @param zone the application zone.
     */
    public ZoneAffinityMatch(@NotNull String zone) {
        this.zone = zone;
    }

    @Override
    public List<ServiceInstance> apply(List<ServiceInstance> instances) {
        return instances.stream()
                .filter(i -> zone.equals(i.getMetadata().get("zone")))
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return format("ZoneAffinityMatcher[zone=%s]", zone);
    }
}
