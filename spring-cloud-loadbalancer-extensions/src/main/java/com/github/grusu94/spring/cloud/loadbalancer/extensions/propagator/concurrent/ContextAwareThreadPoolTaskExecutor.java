package com.github.grusu94.spring.cloud.loadbalancer.extensions.propagator.concurrent;

import com.github.grusu94.spring.cloud.loadbalancer.extensions.context.ExecutionContext;
import jakarta.validation.constraints.NotNull;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.SchedulingTaskExecutor;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * Copies current {@link ExecutionContext} to delegate composite executor of {@link SchedulingTaskExecutor}.
 * <p>{@link org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor} use case.
 */
public class ContextAwareThreadPoolTaskExecutor implements SchedulingTaskExecutor {

    /**
     * The delegate scheduling task executor propagator.
     */
    private final ContextAwareSchedulingTaskExecutor schedulingTaskExecutorPropagator;

    /**
     * Constructor.
     *
     * @param schedulingTaskExecutor      The delegate scheduling task executor.
     */
    public ContextAwareThreadPoolTaskExecutor(@NotNull SchedulingTaskExecutor schedulingTaskExecutor) {
        schedulingTaskExecutorPropagator = new ContextAwareSchedulingTaskExecutor(schedulingTaskExecutor);
    }

    /**
     * {@inheritDoc}
     */
    @Override
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
        schedulingTaskExecutorPropagator.execute(command);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NonNull
    public CompletableFuture<Void> submitCompletable(@NonNull Runnable task) {
        return schedulingTaskExecutorPropagator.submitCompletable(task);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NonNull
    public <T> CompletableFuture<T> submitCompletable(@NonNull Callable<T> task) {
        return schedulingTaskExecutorPropagator.submitCompletable(task);
    }
}
