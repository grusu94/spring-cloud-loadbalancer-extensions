package com.github.grusu94.spring.cloud.loadbalancer.extensions.propagator.concurrent;

import org.junit.jupiter.api.Test;
import org.springframework.scheduling.SchedulingTaskExecutor;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ThreadPoolTaskExecutionContextAwareExecutorTest extends AbstractExecutionContextAwareExecutorTest {
    private final SchedulingTaskExecutor schedulingTaskExecutor = mock(SchedulingTaskExecutor.class);
    private final ContextAwareThreadPoolTaskExecutor propagator = new ContextAwareThreadPoolTaskExecutor(schedulingTaskExecutor);

    @Test
    public void execute() {
        propagator.execute(runnable, 1);
        verify(schedulingTaskExecutor).execute(any(ContextAwareRunnable.class));
    }

    @Test
    public void submit() {
        propagator.submit(runnable);
        verify(schedulingTaskExecutor).submit(any(ContextAwareRunnable.class));
    }

    @Test
    public void submit1() {
        propagator.submit(callable);
        verify(schedulingTaskExecutor).submit(any(ContextAwareCallable.class));
    }

    @Test
    public void prefersShortLivedTasks() {
        propagator.prefersShortLivedTasks();
        verify(schedulingTaskExecutor).prefersShortLivedTasks();
    }

    @Test
    public void execute1() {
        propagator.execute(runnable);
        verify(schedulingTaskExecutor).execute(any(ContextAwareRunnable.class));
    }

    @Test
    public void submitCompletableRunnable() {
        propagator.submitCompletable(runnable);
        verify(schedulingTaskExecutor).submitCompletable(any(ContextAwareRunnable.class));
    }

    @Test
    public void submitCompletableCallable() {
        propagator.submitCompletable(callable);
        verify(schedulingTaskExecutor).submitCompletable(any(ContextAwareCallable.class));
    }
}