package com.example.load.balancer.extensions.propagator.concurrent;

import com.example.load.balancer.extensions.context.ExecutionContext;
import jakarta.validation.constraints.NotNull;
import org.springframework.scheduling.SchedulingTaskExecutor;

/**
 * Copies current {@link ExecutionContext} to delegate scheduled executor task.
 *
 * @author Nadim Benabdenbi
 */
public class ContextAwareSchedulingTaskExecutor extends ContextAwareAsyncTaskExecutor implements SchedulingTaskExecutor {
    /**
     * The delegate scheduling task executor.
     */
    private final SchedulingTaskExecutor delegate;


    /**
     * Constructor
     *
     * @param delegate the delegate scheduling task executor.
     */
    public ContextAwareSchedulingTaskExecutor(@NotNull SchedulingTaskExecutor delegate) {
        super(delegate);
        this.delegate = delegate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean prefersShortLivedTasks() {
        return delegate.prefersShortLivedTasks();
    }
}
