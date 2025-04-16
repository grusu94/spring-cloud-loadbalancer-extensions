package com.example.load.balancer.extensions.matcher;

import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.client.ServiceInstance;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.load.balancer.extensions.context.ExecutionContextHolder.current;
import static java.lang.String.format;

public class DynamicZoneMatch implements LoadBalancingStrategy {

    /**
     * the zone entry key.
     */
    private final String zoneEntryKey;

    /**
     * Constructor.
     *
     * @param zoneEntryKey the favorite zone entry key.
     */
    public DynamicZoneMatch(@NotNull String zoneEntryKey) {
        this.zoneEntryKey = zoneEntryKey;
    }

    @Override
    public List<ServiceInstance> apply(List<ServiceInstance> instances) {
        final String zoneValue = current().get(zoneEntryKey);

        return instances.stream()
                .filter(i -> zoneValue.equals(i.getMetadata().get("zone")))
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return format("DynamicZoneMatcher[%s=%s]", zoneEntryKey, current().get(zoneEntryKey));
    }
}
