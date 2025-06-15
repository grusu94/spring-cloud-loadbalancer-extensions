package com.github.grusu94.spring.cloud.loadbalancer.extensions.propagator.concurrent;

import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static com.github.grusu94.spring.cloud.loadbalancer.extensions.context.ExecutionContextHolder.current;
import static java.util.concurrent.Executors.newSingleThreadExecutor;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ExecutionContextAwareExecutorTest extends AbstractExecutionContextAwareExecutorTest {

    private final ContextAwareExecutor propagator = new ContextAwareExecutor(newSingleThreadExecutor());

    @Test
    public void testExecute() throws Exception {
        current().put(key, value);
        propagator.execute(runnable);
        assertThat(signal.poll(5, TimeUnit.SECONDS), is(value));
    }
}