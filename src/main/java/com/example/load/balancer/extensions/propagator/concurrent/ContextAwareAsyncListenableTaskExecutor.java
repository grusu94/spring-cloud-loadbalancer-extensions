package com.example.load.balancer.extensions.propagator.concurrent;

import com.example.load.balancer.extensions.context.ExecutionContext;
import jakarta.validation.constraints.NotNull;
import org.springframework.core.task.AsyncListenableTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.Callable;

/**
 * Copies current {@link ExecutionContext} to delegate async listenable executor task.
 *
 * @author Nadim Benabdenbi
 */
public class ContextAwareAsyncListenableTaskExecutor extends ContextAwareAsyncTaskExecutor implements AsyncListenableTaskExecutor {
    /**
     * the delegate async listenable task executor.
     */
    private final AsyncListenableTaskExecutor delegate;

    /**
     * Sole Constructor
     *
     * @param delegate the delegate executor service.
     */
    public ContextAwareAsyncListenableTaskExecutor(@NotNull AsyncListenableTaskExecutor delegate) {
        super(delegate);
        this.delegate = delegate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ListenableFuture<?> submitListenable(Runnable task) {
        return delegate.submitListenable(ContextAwareRunnable.wrap(task));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> ListenableFuture<T> submitListenable(Callable<T> task) {
        return delegate.submitListenable(ContextAwareCallable.wrap(task));
    }
}
