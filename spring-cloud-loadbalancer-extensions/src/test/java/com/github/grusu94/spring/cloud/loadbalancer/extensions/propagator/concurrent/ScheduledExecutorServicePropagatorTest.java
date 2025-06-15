package com.github.grusu94.spring.cloud.loadbalancer.extensions.propagator.concurrent;

import org.junit.jupiter.api.Test;

import static com.github.grusu94.spring.cloud.loadbalancer.extensions.context.ExecutionContextHolder.current;
import static java.util.concurrent.Executors.newScheduledThreadPool;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ScheduledExecutorServicePropagatorTest extends AbstractExecutionContextAwareExecutorTest {
    private final ContextAwareScheduledExecutorService propagator =
            new ContextAwareScheduledExecutorService(newScheduledThreadPool(4));

    @Test
    public void scheduleRunnable() throws Exception {
        current().put(key, value);
        propagator.schedule(runnable, 1, MILLISECONDS);
        assertThat(signal.poll(1, SECONDS), is(value));
    }

    @Test
    public void scheduleCallable() throws Exception {
        current().put(key, value);
        propagator.schedule(callable, 1, MILLISECONDS);
        assertThat(signal.poll(1, SECONDS), is(value));
    }

    @Test
    public void scheduleAtFixedRate() throws Exception {
        current().put(key, value);
        propagator.scheduleAtFixedRate(runnable, 1, 1000, MILLISECONDS);
        assertThat(signal.poll(1, SECONDS), is(value));
    }

    @Test
    public void scheduleWithFixedDelay() throws Exception {
        current().put(key, value);
        propagator.scheduleWithFixedDelay(runnable, 1, 1000, MILLISECONDS);
        assertThat(signal.poll(1, SECONDS), is(value));
    }
}