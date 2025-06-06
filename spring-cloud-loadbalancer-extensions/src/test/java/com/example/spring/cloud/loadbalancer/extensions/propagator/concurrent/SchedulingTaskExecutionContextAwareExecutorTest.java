package com.example.spring.cloud.loadbalancer.extensions.propagator.concurrent;

import org.junit.jupiter.api.Test;
import org.springframework.scheduling.SchedulingTaskExecutor;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class SchedulingTaskExecutionContextAwareExecutorTest extends AbstractExecutionContextAwareExecutorTest {
    private final SchedulingTaskExecutor delegate = mock(SchedulingTaskExecutor.class);
    private final ContextAwareSchedulingTaskExecutor propagator = new ContextAwareSchedulingTaskExecutor(delegate);

    @Test
    public void prefersShortLivedTasks() {
        propagator.prefersShortLivedTasks();
        verify(delegate).prefersShortLivedTasks();
    }
}