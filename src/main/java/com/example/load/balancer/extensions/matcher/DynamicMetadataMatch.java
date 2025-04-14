package com.example.load.balancer.extensions.matcher;

import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.client.ServiceInstance;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.load.balancer.extensions.context.ExecutionContextHolder.current;
import static java.lang.String.format;

public class DynamicMetadataMatch implements LoadBalancingStrategy {

    /**
     * the dynamic entry key. used to get the metadata key to match.
     */
    private final String dynamicEntryKey;

    /**
     * matches result when dynamic entry key is missing.
     */
    private final boolean matchIfMissing;

    /**
     * constructor.
     *
     * @param dynamicEntryKey the dynamic metadata key.
     * @param matchIfMissing  the result when dynamic entry key is not defined
     */
    public DynamicMetadataMatch(@NotNull String dynamicEntryKey, boolean matchIfMissing) {
        this.dynamicEntryKey = dynamicEntryKey;
        this.matchIfMissing = matchIfMissing;
    }

    @Override
    public List<ServiceInstance> apply(List<ServiceInstance> instances) {
        List<ServiceInstance> instanceList = null;
        final String metadataValue = current().get(dynamicEntryKey);
        if (metadataValue != null) {
            instanceList = instances.stream()
                    .filter(i -> metadataValue.equals(i.getMetadata().get(dynamicEntryKey)))
                    .collect(Collectors.toList());
        }
        return instanceList == null || instanceList.isEmpty() ? instances : instanceList;
    }

    @Override
    public String toString() {
        String metadataKey = current().get(dynamicEntryKey);
        return format("DynamicMetadataMatcher[(%s=%s)=%s,matchIfMissing=%b]", dynamicEntryKey, metadataKey, metadataKey == null ? null : current().get(metadataKey), matchIfMissing);
    }
}
