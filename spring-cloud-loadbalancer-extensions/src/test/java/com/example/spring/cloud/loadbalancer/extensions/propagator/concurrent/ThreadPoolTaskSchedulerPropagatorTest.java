package com.example.spring.cloud.loadbalancer.extensions.propagator.concurrent;

import org.junit.jupiter.api.Test;
import org.springframework.core.task.AsyncListenableTaskExecutor;
import org.springframework.scheduling.SchedulingTaskExecutor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ThreadPoolTaskSchedulerPropagatorTest extends AbstractExecutionContextAwareExecutorTest {
    private final TaskScheduler taskScheduler = mock(TaskScheduler.class);
    private final SchedulingTaskExecutor schedulingTaskExecutor = mock(SchedulingTaskExecutor.class);
    private final AsyncListenableTaskExecutor asyncListenableTaskExecutor = mock(AsyncListenableTaskExecutor.class);
    private final ContextAwareThreadPoolTaskScheduler propagator =
            new ContextAwareThreadPoolTaskScheduler(asyncListenableTaskExecutor, schedulingTaskExecutor, taskScheduler);

    @Test
    public void scheduleTrigger() {
        propagator.schedule(runnable, trigger);
        verify(taskScheduler).schedule(any(ContextAwareRunnable.class), any(Trigger.class));
    }

    @Test
    public void scheduleDate() {
        propagator.schedule(runnable, date);
        verify(taskScheduler).schedule(any(ContextAwareRunnable.class), eq(date));
    }

    @Test
    public void scheduleAtFixedRateWithDate() {
        propagator.scheduleAtFixedRate(runnable, date, period);
        verify(taskScheduler).scheduleAtFixedRate(any(ContextAwareRunnable.class), eq(date), eq(period));
    }

    @Test
    public void scheduleAtFixedRate() {
        propagator.scheduleAtFixedRate(runnable, period);
        verify(taskScheduler).scheduleAtFixedRate(any(ContextAwareRunnable.class), eq(period));
    }

    @Test
    public void scheduleWithFixedDelayWithDate() {
        propagator.scheduleWithFixedDelay(runnable, date, period);
        verify(taskScheduler).scheduleWithFixedDelay(any(ContextAwareRunnable.class), eq(date), eq(period));
    }

    @Test
    public void scheduleWithFixedDelay() {
        propagator.scheduleWithFixedDelay(runnable, period);
        verify(taskScheduler).scheduleWithFixedDelay(any(ContextAwareRunnable.class), eq(period));
    }
}