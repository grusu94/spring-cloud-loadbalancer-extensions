package com.example.load.balancer.extensions.propagator.concurrent;

import com.example.load.balancer.extensions.context.ExecutionContext;
import jakarta.validation.constraints.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

import static com.example.load.balancer.extensions.propagator.concurrent.ContextAwareCallable.wrap;


/**
 * Copies current {@link ExecutionContext} to delegate executor service task.
 *
 * @author Nadim Benabdenbi
 */
public class ContextAwareExecutorService extends ContextAwareExecutor implements ExecutorService {
    /**
     * The delegate executor service.
     */
    private final ExecutorService delegate;

    /**
     * Constructor
     *
     * @param delegate the delegate executor service.
     */
    public ContextAwareExecutorService(@NotNull ExecutorService delegate) {
        super(delegate);
        this.delegate = delegate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void shutdown() {
        delegate.shutdown();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final List<Runnable> shutdownNow() {
        return delegate.shutdownNow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean isShutdown() {
        return delegate.isShutdown();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean isTerminated() {
        return delegate.isTerminated();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return delegate.awaitTermination(timeout, unit);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <T> Future<T> submit(Callable<T> task) {
        return delegate.submit(wrap(task));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <T> Future<T> submit(Runnable task, T result) {
        return delegate.submit(ContextAwareRunnable.wrap(task), result);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Future<?> submit(Runnable task) {
        return delegate.submit(ContextAwareRunnable.wrap(task));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        return delegate.invokeAll(wrap(tasks));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks,
                                               long timeout, TimeUnit unit) throws InterruptedException {
        return delegate.invokeAll(wrap(tasks), timeout, unit);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws
            InterruptedException,
            ExecutionException {
        return delegate.invokeAny(wrap(tasks));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <T> T invokeAny(Collection<? extends Callable<T>> tasks,
                                 long timeout,
                                 TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return delegate.invokeAny(wrap(tasks), timeout, unit);
    }
}
