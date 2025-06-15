package com.github.grusu94.spring.cloud.loadbalancer.extensions.matcher;

import com.github.grusu94.spring.cloud.loadbalancer.extensions.context.ExecutionContextHolder;
import org.springframework.cloud.client.ServiceInstance;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class SingleMetadataMatcher implements LoadBalancingStrategyMatcher {

    /**
     * the metadata key to test against
     */
    private final String metadataKey;

    /**
     * Constructor.
     *
     * @param metadataKey the metadata key.
     */
    public SingleMetadataMatcher(String metadataKey) {
        this.metadataKey = metadataKey;
    }

    @Override
    public List<ServiceInstance> apply(List<ServiceInstance> instances) {
        final String metadataValue = ExecutionContextHolder.current().get(metadataKey);

        return instances.stream()
                .filter(i -> {
                    String actual = i.getMetadata().get(metadataKey);
                    return (metadataValue == null && actual == null) || (metadataValue != null && metadataValue.equals(actual));
                })
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return format("SingleMetadataMatcher[%s=%s]", metadataKey, ExecutionContextHolder.current().get(metadataKey));
    }
}
