package com.example.spring.cloud.loadbalancer.extensions.propagator.concurrent;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class TaskSchedulerPropagatorTest extends AbstractExecutionContextAwareExecutorTest {
    private final TaskScheduler delegate = mock(TaskScheduler.class);
    private final ContextAwareTaskScheduler propagator = new ContextAwareTaskScheduler(delegate);

    @Test
    public void scheduleTrigger() {
        propagator.schedule(runnable, trigger);
        verify(delegate).schedule(any(ContextAwareRunnable.class), any(Trigger.class));
    }

    @Test
    public void scheduleDate() {
        propagator.schedule(runnable, date);
        verify(delegate).schedule(any(ContextAwareRunnable.class), eq(date));
    }

    @Test
    public void scheduleAtFixedRateWithDate() {
        propagator.scheduleAtFixedRate(runnable, date, period);
        verify(delegate).scheduleAtFixedRate(any(ContextAwareRunnable.class), eq(date), ArgumentMatchers.eq(period));
    }

    @Test
    public void scheduleAtFixedRate() {
        propagator.scheduleAtFixedRate(runnable, period);
        verify(delegate).scheduleAtFixedRate(any(ContextAwareRunnable.class), ArgumentMatchers.eq(period));
    }

    @Test
    public void scheduleWithFixedDelayWithDate() {
        propagator.scheduleWithFixedDelay(runnable, date, period);
        verify(delegate).scheduleWithFixedDelay(any(ContextAwareRunnable.class), eq(date), ArgumentMatchers.eq(period));
    }

    @Test
    public void scheduleWithFixedDelay() {
        propagator.scheduleWithFixedDelay(runnable, period);
        verify(delegate).scheduleWithFixedDelay(any(ContextAwareRunnable.class), ArgumentMatchers.eq(period));
    }
}