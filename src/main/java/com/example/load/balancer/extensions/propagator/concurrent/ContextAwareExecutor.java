package com.example.load.balancer.extensions.propagator.concurrent;

import com.example.load.balancer.extensions.context.ExecutionContext;
import jakarta.validation.constraints.NotNull;
import org.springframework.core.task.TaskExecutor;

import java.util.concurrent.Executor;

/**
 * Copies current {@link ExecutionContext} to delegate executor task.
 *
 * @author Nadim Benabdenbi
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
    public ContextAwareExecutor(@NotNull Executor delegate) {
        this.delegate = delegate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void execute(Runnable command) {
        delegate.execute(ContextAwareRunnable.wrap(command));
    }
}
