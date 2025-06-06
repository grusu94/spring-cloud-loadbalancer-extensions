package com.example.spring.cloud.loadbalancer.extensions.propagator.concurrent;

import com.example.spring.cloud.loadbalancer.extensions.context.ExecutionContext;
import jakarta.validation.constraints.NotNull;
import org.springframework.lang.NonNull;

import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Copies current {@link ExecutionContext} to delegate scheduled executor service task.
 */
public class ContextAwareScheduledExecutorService extends ContextAwareExecutorService
        implements ScheduledExecutorService {

    /**
     * The delegate scheduled executor service.
     */
    private final ScheduledExecutorService delegate;

    /**
     * Constructor.
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
    @NonNull
    public ScheduledFuture<?> schedule(@NonNull Runnable command, long delay,
                                       @NonNull TimeUnit unit) {
        return delegate.schedule(ContextAwareRunnable.wrap(command), delay, unit);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NonNull
    public <V> ScheduledFuture<V> schedule(@NonNull Callable<V> callable, long delay,
                                           @NonNull TimeUnit unit) {
        return delegate.schedule(ContextAwareCallable.wrap(callable), delay, unit);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NonNull
    public ScheduledFuture<?> scheduleAtFixedRate(@NonNull Runnable command, long initialDelay,
                                                  long period, @NonNull TimeUnit unit) {
        return delegate.scheduleAtFixedRate(ContextAwareRunnable.wrap(command), initialDelay, period, unit);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NonNull
    public ScheduledFuture<?> scheduleWithFixedDelay(@NonNull Runnable command, long initialDelay,
                                                     long delay, @NonNull TimeUnit unit) {
        return delegate.scheduleWithFixedDelay(ContextAwareRunnable.wrap(command), initialDelay, delay, unit);
    }
}
