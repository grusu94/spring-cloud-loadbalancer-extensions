package com.example.load.balancer.extensions.propagator.concurrent;

import com.example.load.balancer.extensions.context.ExecutionContext;

import static com.example.load.balancer.extensions.context.ExecutionContextHolder.current;
import static com.example.load.balancer.extensions.context.ExecutionContextHolder.switchTo;

/**
 * Copies current {@link ExecutionContext} to delegate runnable.
 *
 * @author Nadim Benabdenbi
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
     * Sole constructor: saves the current {@link ExecutionContext} for later {@link #run()} invocation.
     *
     * @param delegate the delegate {@link Runnable}
     */
    public ContextAwareRunnable(Runnable delegate) {
        this.delegate = delegate;
        context = current().copy();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
        switchTo(context);
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
