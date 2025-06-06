package com.example.spring.cloud.loadbalancer.extensions.propagator.concurrent;

import org.junit.jupiter.api.Test;
import org.springframework.core.task.AsyncListenableTaskExecutor;
import org.springframework.scheduling.SchedulingTaskExecutor;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ThreadPoolTaskExecutionContextAwareExecutorTest extends AbstractExecutionContextAwareExecutorTest {
    private final SchedulingTaskExecutor schedulingTaskExecutor = mock(SchedulingTaskExecutor.class);
    private final AsyncListenableTaskExecutor asyncListenableTaskExecutor = mock(AsyncListenableTaskExecutor.class);
    private final ContextAwareThreadPoolTaskExecutor propagator = new ContextAwareThreadPoolTaskExecutor(asyncListenableTaskExecutor, schedulingTaskExecutor);

    @Test
    public void execute() {
        propagator.execute(runnable);
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
        verify(asyncListenableTaskExecutor).execute(any(ContextAwareRunnable.class));
    }

    @Test
    public void submitListenable() {
        propagator.submitListenable(runnable);
        verify(asyncListenableTaskExecutor).submitListenable(any(ContextAwareRunnable.class));
    }

    @Test
    public void submitListenable1() {
        propagator.submitListenable(callable);
        verify(asyncListenableTaskExecutor).submitListenable(any(ContextAwareCallable.class));
    }
}