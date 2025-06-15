package com.github.grusu94.spring.cloud.loadbalancer.extensions.propagator.concurrent;

import com.github.grusu94.spring.cloud.loadbalancer.extensions.context.ExecutionContext;
import jakarta.validation.constraints.NotNull;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ScheduledFuture;

/**
 * Copies current {@link ExecutionContext} to delegate scheduler task.
 */
public class ContextAwareTaskScheduler implements TaskScheduler {

    /**
     * The delegate task scheduler.
     */
    private final TaskScheduler delegate;

    /**
     * Constructor.
     *
     * @param delegate the delegate task scheduler.
     */
    public ContextAwareTaskScheduler(@NotNull TaskScheduler delegate) {
        this.delegate = delegate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ScheduledFuture<?> schedule(@NonNull Runnable task, @NonNull Trigger trigger) {
        return delegate.schedule(ContextAwareRunnable.wrap(task), trigger);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NonNull
    public ScheduledFuture<?> schedule(@NonNull Runnable task, @NonNull Instant startTime) {
        return delegate.schedule(ContextAwareRunnable.wrap(task), startTime);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NonNull
    public ScheduledFuture<?> scheduleAtFixedRate(@NonNull Runnable task,
                                                  @NonNull Instant startTime,
                                                  @NonNull Duration period) {
        return delegate.scheduleAtFixedRate(ContextAwareRunnable.wrap(task), startTime, period);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NonNull
    public ScheduledFuture<?> scheduleAtFixedRate(@NonNull Runnable task, @NonNull Duration period) {
        return delegate.scheduleAtFixedRate(ContextAwareRunnable.wrap(task), period);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NonNull
    public ScheduledFuture<?> scheduleWithFixedDelay(@NonNull Runnable task,
                                                     @NonNull Instant startTime,
                                                     @NonNull Duration delay) {
        return delegate.scheduleWithFixedDelay(ContextAwareRunnable.wrap(task), startTime, delay);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NonNull
    public ScheduledFuture<?> scheduleWithFixedDelay(@NonNull Runnable task, @NonNull Duration delay) {
        return delegate.scheduleWithFixedDelay(ContextAwareRunnable.wrap(task), delay);
    }
}
