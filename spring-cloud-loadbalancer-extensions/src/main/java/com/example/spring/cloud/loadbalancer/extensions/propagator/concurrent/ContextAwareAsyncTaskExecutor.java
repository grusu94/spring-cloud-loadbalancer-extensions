package com.example.spring.cloud.loadbalancer.extensions.propagator.concurrent;

import com.example.spring.cloud.loadbalancer.extensions.context.ExecutionContext;
import jakarta.validation.constraints.NotNull;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.lang.NonNull;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * Copies current {@link ExecutionContext} to delegate async task executor task.
 */
public class ContextAwareAsyncTaskExecutor extends ContextAwareExecutor implements AsyncTaskExecutor {
    /**
     * The delegate async task executor.
     */
    private final AsyncTaskExecutor delegate;

    /**
     * Constructor.
     *
     * @param delegate the delegate async task executor...
     */
    public ContextAwareAsyncTaskExecutor(@NotNull AsyncTaskExecutor delegate) {
        super(delegate);
        this.delegate = delegate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Deprecated
    public final void execute(@NonNull Runnable task, long startTimeout) {
        delegate.execute(ContextAwareRunnable.wrap(task), startTimeout);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NonNull
    public final Future<?> submit(@NonNull Runnable task) {
        return delegate.submit(ContextAwareRunnable.wrap(task));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NonNull
    public final <T> Future<T> submit(@NonNull Callable<T> task) {
        return delegate.submit(ContextAwareCallable.wrap(task));
    }
}
