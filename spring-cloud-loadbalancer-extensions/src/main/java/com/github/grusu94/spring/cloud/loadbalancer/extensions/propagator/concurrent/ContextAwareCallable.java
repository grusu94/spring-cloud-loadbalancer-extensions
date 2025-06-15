package com.github.grusu94.spring.cloud.loadbalancer.extensions.propagator.concurrent;

import com.github.grusu94.spring.cloud.loadbalancer.extensions.context.ExecutionContext;
import com.github.grusu94.spring.cloud.loadbalancer.extensions.context.ExecutionContextHolder;

import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

/**
 * Copies current {@link ExecutionContext} to delegate callable.
 */
public class ContextAwareCallable<T> implements Callable<T> {
    /**
     * The delegate callable.
     */
    private final Callable<T> delegate;
    /**
     * the current execution context.
     */
    private final ExecutionContext context;

    /**
     * Constructor: saves the current {@link ExecutionContext} for later {@link #call()} invocation.
     *
     * @param delegate the delegate {@link Callable}
     */
    public ContextAwareCallable(Callable<T> delegate) {
        this.delegate = delegate;
        context = ExecutionContextHolder.current().copy();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T call() throws Exception {
        ExecutionContextHolder.switchTo(context);
        return delegate.call();
    }

    /**
     * Wraps a callable to a {@link ContextAwareCallable}
     *
     * @param callable the callable to wrap
     * @param <T>      the callable result type
     * @return The {@link ContextAwareCallable} instance over #callable
     */
    public static <T> ContextAwareCallable<T> wrap(Callable<T> callable) {
        return new ContextAwareCallable<>(callable);
    }

    /**
     * Wraps a callable's collection to a {@link ContextAwareCallable} collection.
     *
     * @param tasks the callable's collection to wrap
     * @param <T>   the callable result type
     * @return The {@link ContextAwareCallable} instances over #tasks
     */
    public static <T> Collection<Callable<T>> wrap(Collection<? extends Callable<T>> tasks) {
        return tasks.stream().map(ContextAwareCallable::wrap).collect(Collectors.toList());
    }
}
