package com.example.load.balancer.extensions.propagator.resilience4j;

public class Resilience4jContextPlugin {

    private ExecutionContextAwareResilience4jStrategy globalStrategy;

    public void register(ExecutionContextAwareResilience4jStrategy strategy) {
        globalStrategy = strategy;
    }

    public ExecutionContextAwareResilience4jStrategy get() {
        return globalStrategy;
    }
}
