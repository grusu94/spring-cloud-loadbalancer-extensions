package com.example.load.balancer.extensions.propagator.concurrent;

import com.example.load.balancer.extensions.context.ExecutionContext;
import jakarta.validation.constraints.NotNull;

import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Copies current {@link ExecutionContext} to delegate scheduled executor service task.
 *
 * @author Nadim Benabdenbi
 */
public class ContextAwareScheduledExecutorService extends ContextAwareExecutorService implements ScheduledExecutorService {

    /**
     * The delegate scheduled executor service.
     */
    private final ScheduledExecutorService delegate;

    /**
     * Sole Constructor.
     *
     * @param delegate the delegate scheduled executor service.
     */
    public ContextAwareScheduledExecutorService(@NotNull ScheduledExecutorService delegate) {
        super(delegate);
        this.delegate = delegate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
        return delegate.schedule(ContextAwareRunnable.wrap(command), delay, unit);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
        return delegate.schedule(ContextAwareCallable.wrap(callable), delay, unit);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
        return delegate.scheduleAtFixedRate(ContextAwareRunnable.wrap(command), initialDelay, period, unit);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
        return delegate.scheduleWithFixedDelay(ContextAwareRunnable.wrap(command), initialDelay, delay, unit);
    }
}
