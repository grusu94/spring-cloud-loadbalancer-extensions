package com.example.spring.cloud.loadbalancer.extensions.matcher;

import com.example.spring.cloud.loadbalancer.extensions.context.ExecutionContextHolder;
import org.springframework.cloud.client.ServiceInstance;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class StrictMetadataMatcher implements LoadBalancingStrategyMatcher {

    @Override
    public List<ServiceInstance> apply(List<ServiceInstance> instances) {
        final Set<Map.Entry<String, String>> entries = ExecutionContextHolder.current().entrySet();
        if (entries == null) {
            return Collections.emptyList();
        }

        return instances.stream()
                .filter(i -> i.getMetadata().entrySet().containsAll(entries))
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return format("StrictMetadataMatcher%s", ExecutionContextHolder.current().entrySet());
    }
}
