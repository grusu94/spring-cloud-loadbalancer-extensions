package com.example.spring.cloud.loadbalancer.extensions.matcher;

import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.client.ServiceInstance;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class SingleStaticMetadataMatcher implements LoadBalancingStrategyMatcher {

    /**
     * the entry key to match.
     */
    private final String entryKey;
    /**
     * the expected entry value
     */
    private final String entryValue;

    /**
     * constructor
     *
     * @param entryKey   the attribute key to match.
     * @param entryValue the expected entry value
     */
    public SingleStaticMetadataMatcher(@NotNull String entryKey, @NotNull String entryValue) {
        this.entryKey = entryKey;
        this.entryValue = entryValue;
    }
    @Override
    public List<ServiceInstance> apply(List<ServiceInstance> instances) {
        return instances.stream()
                .filter(i -> entryValue != null && entryValue.equals(i.getMetadata().get(entryKey)))
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return format("StrictMetadataMatcher[%s=%s]", entryKey, entryValue);
    }
}
