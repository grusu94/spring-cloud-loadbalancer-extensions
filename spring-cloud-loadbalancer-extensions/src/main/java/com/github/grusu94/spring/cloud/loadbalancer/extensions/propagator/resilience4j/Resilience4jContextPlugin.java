package com.github.grusu94.spring.cloud.loadbalancer.extensions.propagator.resilience4j;

public class Resilience4jContextPlugin {

    private static ExecutionContextAwareResilience4jStrategy globalStrategy;

    public static void register(ExecutionContextAwareResilience4jStrategy strategy) {
        globalStrategy = strategy;
    }

    public ExecutionContextAwareResilience4jStrategy get() {
        return globalStrategy;
    }
}
