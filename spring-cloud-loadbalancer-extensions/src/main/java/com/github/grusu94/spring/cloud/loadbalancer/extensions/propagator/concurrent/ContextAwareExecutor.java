package com.github.grusu94.spring.cloud.loadbalancer.extensions.propagator.concurrent;

import com.github.grusu94.spring.cloud.loadbalancer.extensions.context.ExecutionContext;
import org.springframework.core.task.TaskExecutor;
import org.springframework.lang.NonNull;

import java.util.concurrent.Executor;

/**
 * Copies current {@link ExecutionContext} to delegate executor task.
 */
public class ContextAwareExecutor implements Executor, TaskExecutor {
    /**
     * The delegate executor.
     */
    private final Executor delegate;

    /**
     * Constructor.
     *
     * @param delegate the delegate executor.
     */
    public ContextAwareExecutor(@NonNull Executor delegate) {
        this.delegate = delegate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(@NonNull Runnable command) {
        delegate.execute(ContextAwareRunnable.wrap(command));
    }
}
