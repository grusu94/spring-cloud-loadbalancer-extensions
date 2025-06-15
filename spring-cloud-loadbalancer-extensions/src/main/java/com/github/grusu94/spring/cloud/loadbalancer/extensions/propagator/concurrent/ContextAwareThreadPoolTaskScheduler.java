package com.github.grusu94.spring.cloud.loadbalancer.extensions.propagator.concurrent;

import com.github.grusu94.spring.cloud.loadbalancer.extensions.context.ExecutionContext;
import jakarta.validation.constraints.NotNull;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.SchedulingTaskExecutor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ScheduledFuture;

/**
 * Copies current {@link ExecutionContext} to delegate composite scheduler of {@link SchedulingTaskExecutor}
 * and {@link TaskScheduler}.
 * <p>{@link org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler} use case.
 */
public class ContextAwareThreadPoolTaskScheduler extends ContextAwareThreadPoolTaskExecutor
        implements TaskScheduler {
    /**
     * The delegate task scheduler propagator.
     */
    private final ContextAwareTaskScheduler executionContextAwareTaskScheduler;

    /**
     * Constructor.
     *
     * @param schedulingTaskExecutor      The delegate scheduling task executor.
     * @param taskScheduler               the delegate task scheduler.
     */
    public ContextAwareThreadPoolTaskScheduler(@NotNull SchedulingTaskExecutor schedulingTaskExecutor,
                                               @NotNull TaskScheduler taskScheduler) {
        super(schedulingTaskExecutor);
        executionContextAwareTaskScheduler = new ContextAwareTaskScheduler(taskScheduler);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ScheduledFuture<?> schedule(@NonNull Runnable task, @NonNull Trigger trigger) {
        return executionContextAwareTaskScheduler.schedule(task, trigger);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NonNull
    public ScheduledFuture<?> schedule(@NonNull Runnable task, @NonNull Instant startTime) {
        return executionContextAwareTaskScheduler.schedule(task, startTime);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NonNull
    public ScheduledFuture<?> scheduleAtFixedRate(@NonNull Runnable task,
                                                  @NonNull Instant startTime,
                                                  @NonNull Duration period) {
        return executionContextAwareTaskScheduler.scheduleAtFixedRate(task, startTime, period);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NonNull
    public ScheduledFuture<?> scheduleAtFixedRate(@NonNull Runnable task, @NonNull Duration period) {
        return executionContextAwareTaskScheduler.scheduleAtFixedRate(task, period);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NonNull
    public ScheduledFuture<?> scheduleWithFixedDelay(@NonNull Runnable task,
                                                     @NonNull Instant startTime,
                                                     @NonNull Duration delay) {
        return executionContextAwareTaskScheduler.scheduleWithFixedDelay(task, startTime, delay);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NonNull
    public ScheduledFuture<?> scheduleWithFixedDelay(@NonNull Runnable task, @NonNull Duration delay) {
        return executionContextAwareTaskScheduler.scheduleWithFixedDelay(task, delay);
    }
}
