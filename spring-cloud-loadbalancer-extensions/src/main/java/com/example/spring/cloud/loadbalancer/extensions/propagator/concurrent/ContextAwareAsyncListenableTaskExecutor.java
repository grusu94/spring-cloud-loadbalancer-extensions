package com.example.spring.cloud.loadbalancer.extensions.propagator.concurrent;

import com.example.spring.cloud.loadbalancer.extensions.context.ExecutionContext;
import jakarta.validation.constraints.NotNull;
import org.springframework.core.task.AsyncListenableTaskExecutor;
import org.springframework.lang.NonNull;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.Callable;

/**
 * Copies current {@link ExecutionContext} to delegate async listenable executor task.
 */
public class ContextAwareAsyncListenableTaskExecutor extends ContextAwareAsyncTaskExecutor
        implements AsyncListenableTaskExecutor {
    /**
     * the delegate async listenable task executor.
     */
    private final AsyncListenableTaskExecutor delegate;

    /**
     * Constructor
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
    @NonNull
    public ListenableFuture<?> submitListenable(@NonNull Runnable task) {
        return delegate.submitListenable(ContextAwareRunnable.wrap(task));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NonNull
    public <T> ListenableFuture<T> submitListenable(@NonNull Callable<T> task) {
        return delegate.submitListenable(ContextAwareCallable.wrap(task));
    }
}
