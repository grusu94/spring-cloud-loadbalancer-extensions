package com.example.load.balancer.extensions.propagator.hystrix;

import com.example.load.balancer.extensions.propagator.concurrent.ContextAwareCallable;
import com.example.load.balancer.extensions.propagator.resilience4j.ExecutionContextAwareResilience4jStrategy;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

public class ExecutionContextAwareResilience4jStrategyTest {
    ExecutionContextAwareResilience4jStrategy strategy = new ExecutionContextAwareResilience4jStrategy();

    @Test
    public void wrapCallable() {
        MatcherAssert.assertThat(strategy.decorate(null).getClass(), Matchers.equalTo(ContextAwareCallable.class));
    }
}