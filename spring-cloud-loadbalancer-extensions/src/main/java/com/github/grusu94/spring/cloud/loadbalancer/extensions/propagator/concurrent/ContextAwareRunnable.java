package com.github.grusu94.spring.cloud.loadbalancer.extensions.propagator.concurrent;

import com.github.grusu94.spring.cloud.loadbalancer.extensions.context.ExecutionContext;
import com.github.grusu94.spring.cloud.loadbalancer.extensions.context.ExecutionContextHolder;

/**
 * Copies current {@link ExecutionContext} to delegate runnable.
 */
public class ContextAwareRunnable implements Runnable {
    /**
     * The delegate runnable.
     */
    private final Runnable delegate;
    /**
     * the current execution context
     */
    private final ExecutionContext context;

    /**
     * constructor: saves the current {@link ExecutionContext} for later {@link #run()} invocation.
     *
     * @param delegate the delegate {@link Runnable}
     */
    public ContextAwareRunnable(Runnable delegate) {
        this.delegate = delegate;
        context = ExecutionContextHolder.current().copy();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
        ExecutionContextHolder.switchTo(context);
        delegate.run();
    }

    /**
     * Wraps a Runnable to a {@link ContextAwareRunnable}
     *
     * @param runnable the runnable to wrap
     * @return the instance of {@link ContextAwareRunnable} over the runnable
     */
    public static Runnable wrap(Runnable runnable) {
        return new ContextAwareRunnable(runnable);
    }
}
