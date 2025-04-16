package com.example.load.balancer.extensions.propagator.concurrent;

import com.example.load.balancer.extensions.context.ExecutionContext;
import jakarta.validation.constraints.NotNull;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;

import java.util.Date;
import java.util.concurrent.ScheduledFuture;

/**
 * Copies current {@link ExecutionContext} to delegate scheduler task.
 *
 * @author Nadim Benabdenbi
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
    public ScheduledFuture<?> schedule(Runnable task, Trigger trigger) {
        return delegate.schedule(ContextAwareRunnable.wrap(task), trigger);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ScheduledFuture<?> schedule(Runnable task, Date startTime) {
        return delegate.schedule(ContextAwareRunnable.wrap(task), startTime);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable task, Date startTime, long period) {
        return delegate.scheduleAtFixedRate(ContextAwareRunnable.wrap(task), startTime, period);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable task, long period) {
        return delegate.scheduleAtFixedRate(ContextAwareRunnable.wrap(task), period);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable task, Date startTime, long delay) {
        return delegate.scheduleWithFixedDelay(ContextAwareRunnable.wrap(task), startTime, delay);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable task, long delay) {
        return delegate.scheduleWithFixedDelay(ContextAwareRunnable.wrap(task), delay);
    }
}
