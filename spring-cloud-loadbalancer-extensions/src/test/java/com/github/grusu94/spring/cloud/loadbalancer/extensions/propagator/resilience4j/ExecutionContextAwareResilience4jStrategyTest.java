package com.github.grusu94.spring.cloud.loadbalancer.extensions.propagator.resilience4j;

import com.github.grusu94.spring.cloud.loadbalancer.extensions.propagator.concurrent.ContextAwareCallable;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

public class ExecutionContextAwareResilience4jStrategyTest {
    private final ExecutionContextAwareResilience4jStrategy strategy = new ExecutionContextAwareResilience4jStrategy();

    @Test
    public void wrapCallable() {
        MatcherAssert.assertThat(strategy.decorate(null).getClass(), Matchers.equalTo(ContextAwareCallable.class));
    }
}