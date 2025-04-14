package com.example.load.balancer.extensions.matcher;

import org.springframework.cloud.client.ServiceInstance;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.load.balancer.extensions.context.ExecutionContextHolder.current;
import static java.lang.String.format;

public class SingleMetadataMatch implements LoadBalancingStrategy {

    /**
     * the metadata key to test against
     */
    private final String metadataKey;

    /**
     * Constructor.
     *
     * @param metadataKey the metadata key.
     */
    public SingleMetadataMatch(String metadataKey) {
        this.metadataKey = metadataKey;
    }

    @Override
    public List<ServiceInstance> apply(List<ServiceInstance> instances) {
        final String metadataValue = current().get(metadataKey);

        return instances.stream()
                .filter(i -> metadataValue != null && metadataValue.equals(i.getMetadata().get(metadataKey)))
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return format("SingleMetadataMatcher[%s=%s]", metadataKey, current().get(metadataKey));
    }
}
