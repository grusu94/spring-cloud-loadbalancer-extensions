package com.github.grusu94.spring.cloud.loadbalancer.extensions.matcher;

import com.github.grusu94.spring.cloud.loadbalancer.extensions.context.ExecutionContextHolder;
import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.client.ServiceInstance;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class DynamicZoneMatcher implements LoadBalancingStrategyMatcher {

    /**
     * the zone entry key.
     */
    private final String zoneEntryKey;

    /**
     * Constructor.
     *
     * @param zoneEntryKey the favorite zone entry key.
     */
    public DynamicZoneMatcher(@NotNull String zoneEntryKey) {
        this.zoneEntryKey = zoneEntryKey;
    }

    @Override
    public List<ServiceInstance> apply(List<ServiceInstance> instances) {
        final String zoneValue = ExecutionContextHolder.current().get(zoneEntryKey);

        return instances.stream()
                .filter(i -> zoneValue != null && zoneValue.equals(i.getMetadata().get("zone")))
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return format("DynamicZoneMatcher[%s=%s]", zoneEntryKey, ExecutionContextHolder.current().get(zoneEntryKey));
    }
}
