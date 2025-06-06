package com.example.spring.cloud.loadbalancer.extensions.propagator.concurrent;

import com.example.spring.cloud.loadbalancer.extensions.context.ExecutionContext;
import jakarta.validation.constraints.NotNull;
import org.springframework.core.task.AsyncListenableTaskExecutor;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.SchedulingTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * Copies current {@link ExecutionContext} to delegate composite executor of {@link AsyncListenableTaskExecutor} and {@link SchedulingTaskExecutor}.
 * <p>{@link org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor} use case.
 */
public class ContextAwareThreadPoolTaskExecutor implements AsyncListenableTaskExecutor, SchedulingTaskExecutor {
    /**
     * The delegate async listenable task executor propagator.
     */
    private final ContextAwareAsyncListenableTaskExecutor asyncListenableTaskExecutorPropagator;
    /**
     * The delegate scheduling task executor propagator.
     */
    private final ContextAwareSchedulingTaskExecutor schedulingTaskExecutorPropagator;

    /**
     * Constructor.
     *
     * @param asyncListenableTaskExecutor The delegate async listenable task executor.
     * @param schedulingTaskExecutor      The delegate scheduling task executor.
     */
    public ContextAwareThreadPoolTaskExecutor(@NotNull AsyncListenableTaskExecutor asyncListenableTaskExecutor,
                                              @NotNull SchedulingTaskExecutor schedulingTaskExecutor) {
        asyncListenableTaskExecutorPropagator = new ContextAwareAsyncListenableTaskExecutor(asyncListenableTaskExecutor);
        schedulingTaskExecutorPropagator = new ContextAwareSchedulingTaskExecutor(schedulingTaskExecutor);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Deprecated
    public void execute(@NonNull Runnable task, long startTimeout) {
        schedulingTaskExecutorPropagator.execute(task, startTimeout);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NonNull
    public Future<?> submit(@NonNull Runnable task) {
        return schedulingTaskExecutorPropagator.submit(task);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NonNull
    public <T> Future<T> submit(@NonNull Callable<T> task) {
        return schedulingTaskExecutorPropagator.submit(task);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean prefersShortLivedTasks() {
        return schedulingTaskExecutorPropagator.prefersShortLivedTasks();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NonNull
    public void execute(@NonNull Runnable command) {
        asyncListenableTaskExecutorPropagator.execute(command);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NonNull
    public ListenableFuture<?> submitListenable(@NonNull Runnable task) {
        return asyncListenableTaskExecutorPropagator.submitListenable(task);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NonNull
    public <T> ListenableFuture<T> submitListenable(@NonNull Callable<T> task) {
        return asyncListenableTaskExecutorPropagator.submitListenable(task);
    }
}
