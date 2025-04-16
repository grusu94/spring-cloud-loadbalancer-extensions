package com.example.load.balancer.extensions.propagator.concurrent;

import com.example.load.balancer.extensions.context.ExecutionContext;
import jakarta.validation.constraints.NotNull;
import org.springframework.core.task.AsyncTaskExecutor;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * Copies current {@link ExecutionContext} to delegate async task executor task.
 *
 * @author Nadim Benabdenbi
 */
public class ContextAwareAsyncTaskExecutor extends ContextAwareExecutor implements AsyncTaskExecutor {
    /**
     * The delegate async task executor.
     */
    private final AsyncTaskExecutor delegate;


    /**
     * Constructor.
     *
     * @param delegate the delegate async task executor..
     */
    public ContextAwareAsyncTaskExecutor(@NotNull AsyncTaskExecutor delegate) {
        super(delegate);
        this.delegate = delegate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void execute(Runnable task, long startTimeout) {
        delegate.execute(ContextAwareRunnable.wrap(task), startTimeout);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Future<?> submit(Runnable task) {
        return delegate.submit(ContextAwareRunnable.wrap(task));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <T> Future<T> submit(Callable<T> task) {
        return delegate.submit(ContextAwareCallable.wrap(task));
    }
}
