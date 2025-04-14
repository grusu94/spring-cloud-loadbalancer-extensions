package com.example.load.balancer.extensions.propagator.concurrent;

import com.example.load.balancer.extensions.context.ExecutionContext;
import jakarta.validation.constraints.NotNull;
import org.springframework.core.task.AsyncListenableTaskExecutor;
import org.springframework.scheduling.SchedulingTaskExecutor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;

import java.util.Date;
import java.util.concurrent.ScheduledFuture;

/**
 * Copies current {@link ExecutionContext} to delegate composite scheduler of {@link AsyncListenableTaskExecutor} and {@link SchedulingTaskExecutor} and {@link TaskScheduler}.
 * <p>{@link org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler} use case.
 *
 * @author Nadim Benabdenbi
 */
public class ContextAwareThreadPoolTaskScheduler extends ContextAwareThreadPoolTaskExecutor implements TaskScheduler {
    /**
     * The delegate task scheduler propagator.
     */
    private final ContextAwareTaskScheduler executionContextAwareTaskScheduler;

    /**
     * Sole Constructor.
     *
     * @param asyncListenableTaskExecutor The delegate async listenable task executor.
     * @param schedulingTaskExecutor      The delegate scheduling task executor.
     * @param taskScheduler               the delegate task scheduler.
     */
    public ContextAwareThreadPoolTaskScheduler(@NotNull AsyncListenableTaskExecutor asyncListenableTaskExecutor,
                                               @NotNull SchedulingTaskExecutor schedulingTaskExecutor,
                                               @NotNull TaskScheduler taskScheduler) {
        super(asyncListenableTaskExecutor, schedulingTaskExecutor);
        executionContextAwareTaskScheduler = new ContextAwareTaskScheduler(taskScheduler);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ScheduledFuture<?> schedule(Runnable task, Trigger trigger) {
        return executionContextAwareTaskScheduler.schedule(task, trigger);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ScheduledFuture<?> schedule(Runnable task, Date startTime) {
        return executionContextAwareTaskScheduler.schedule(task, startTime);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable task, Date startTime, long period) {
        return executionContextAwareTaskScheduler.scheduleAtFixedRate(task, startTime, period);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable task, long period) {
        return executionContextAwareTaskScheduler.scheduleAtFixedRate(task, period);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable task, Date startTime, long delay) {
        return executionContextAwareTaskScheduler.scheduleWithFixedDelay(task, startTime, delay);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable task, long delay) {
        return executionContextAwareTaskScheduler.scheduleWithFixedDelay(task, delay);
    }
}
