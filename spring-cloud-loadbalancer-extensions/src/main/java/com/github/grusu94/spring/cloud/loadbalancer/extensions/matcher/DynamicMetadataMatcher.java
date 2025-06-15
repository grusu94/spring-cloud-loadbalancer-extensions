package com.github.grusu94.spring.cloud.loadbalancer.extensions.matcher;

import com.github.grusu94.spring.cloud.loadbalancer.extensions.context.ExecutionContextHolder;
import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.client.ServiceInstance;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class DynamicMetadataMatcher implements LoadBalancingStrategyMatcher {

    /**
     * the dynamic entry key. used to get the metadata key to match.
     */
    private final String dynamicEntryKey;

    /**
     * matches result when dynamic entry key is missing.
     */
    private final boolean matchIfMissing;

    /**
     * Constructor.
     *
     * @param dynamicEntryKey the dynamic metadata key.
     * @param matchIfMissing  the result when dynamic entry key is not defined
     */
    public DynamicMetadataMatcher(@NotNull String dynamicEntryKey, boolean matchIfMissing) {
        this.dynamicEntryKey = dynamicEntryKey;
        this.matchIfMissing = matchIfMissing;
    }

    @Override
    public List<ServiceInstance> apply(List<ServiceInstance> instances) {
        List<ServiceInstance> filteredInstances = new ArrayList<>();
        final String metadataKey = ExecutionContextHolder.current().get(dynamicEntryKey);
        if (metadataKey != null) {
            filteredInstances = instances.stream()
                    .filter(i -> {
                        String expected = ExecutionContextHolder.current().get(metadataKey);
                        String metadataValue = i.getMetadata().get(metadataKey);
                        return (expected == null && metadataValue == null) || (expected != null && expected.equals(metadataValue));
                    })
                    .collect(Collectors.toList());
            return filteredInstances;
        } else {
            return matchIfMissing ? instances : filteredInstances;
        }
    }

    @Override
    public String toString() {
        String metadataKey = ExecutionContextHolder.current().get(dynamicEntryKey);
        return format("DynamicMetadataMatcher[(%s=%s)=%s,matchIfMissing=%b]", dynamicEntryKey, metadataKey, metadataKey == null ? null : ExecutionContextHolder.current().get(metadataKey), matchIfMissing);
    }
}
