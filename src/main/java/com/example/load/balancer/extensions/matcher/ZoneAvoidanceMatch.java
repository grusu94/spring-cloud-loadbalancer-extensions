package com.example.load.balancer.extensions.matcher;

import org.springframework.cloud.client.ServiceInstance;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ZoneAvoidanceMatch implements LoadBalancingStrategy {

    private final double unhealthyThreshold; // e.g. 0.8 means if 80% of zone is unhealthy, avoid it

    public ZoneAvoidanceMatch(double unhealthyThreshold) {
        this.unhealthyThreshold = unhealthyThreshold;
    }

    @Override
    public List<ServiceInstance> apply(List<ServiceInstance> instances) {
        if (instances.isEmpty()) return instances;

        Map<String, List<ServiceInstance>> groupedByZone = instances.stream()
                .collect(Collectors.groupingBy(i -> i.getMetadata().getOrDefault("zone", "unknown")));

        // Calculate healthy ratios
        Map<String, List<ServiceInstance>> healthyZones = new HashMap<>();
        for (Map.Entry<String, List<ServiceInstance>> entry : groupedByZone.entrySet()) {
            String zone = entry.getKey();
            List<ServiceInstance> zoneInstances = entry.getValue();
            long healthyCount = zoneInstances.stream().filter(this::isHealthy).count();
            double healthyRatio = (double) healthyCount / zoneInstances.size();

            if (healthyRatio >= (1.0 - unhealthyThreshold)) {
                healthyZones.put(zone, zoneInstances);
            }
        }

        // If no healthy zones found, fallback to all
        if (healthyZones.isEmpty()) return instances;

        // Optionally: pick the zone with most healthy instances
        return healthyZones.values().stream()
                .max(Comparator.comparingInt(List::size))
                .orElse(instances);
    }

    private boolean isHealthy(ServiceInstance instance) {
        // Customize this based on your metadata or monitoring logic
        String status = instance.getMetadata().getOrDefault("status", "UP");
        return !"DOWN".equalsIgnoreCase(status);
    }
}
