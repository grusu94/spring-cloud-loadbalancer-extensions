package com.example.spring.cloud.loadbalancer.extensions.propagator.concurrent;

import com.example.spring.cloud.loadbalancer.extensions.context.ExecutionContext;
import jakarta.validation.constraints.NotNull;
import org.springframework.lang.NonNull;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

import static com.example.spring.cloud.loadbalancer.extensions.propagator.concurrent.ContextAwareCallable.wrap;

/**
 * Copies current {@link ExecutionContext} to delegate executor service task.
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
    @NonNull
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
    public final boolean awaitTermination(long timeout, @NonNull TimeUnit unit)
            throws InterruptedException {
        return delegate.awaitTermination(timeout, unit);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NonNull
    public final <T> Future<T> submit(@NonNull Callable<T> task) {
        return delegate.submit(wrap(task));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NonNull
    public final <T> Future<T> submit(@NonNull Runnable task, T result) {
        return delegate.submit(ContextAwareRunnable.wrap(task), result);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NonNull
    public final Future<?> submit(@NonNull Runnable task) {
        return delegate.submit(ContextAwareRunnable.wrap(task));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NonNull
    public final <T> List<Future<T>> invokeAll(@NonNull Collection<? extends Callable<T>> tasks)
            throws InterruptedException {
        return delegate.invokeAll(ContextAwareCallable.wrap(tasks));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NonNull
    public final <T> List<Future<T>> invokeAll(@NonNull Collection<? extends Callable<T>> tasks,
                                               long timeout,
                                               @NonNull TimeUnit unit) throws InterruptedException {
        return delegate.invokeAll(ContextAwareCallable.wrap(tasks), timeout, unit);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NonNull
    public final <T> T invokeAny(@NonNull Collection<? extends Callable<T>> tasks)
            throws InterruptedException, ExecutionException {
        return delegate.invokeAny(ContextAwareCallable.wrap(tasks));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <T> T invokeAny(@NonNull Collection<? extends Callable<T>> tasks,
                                 long timeout,
                                 @NonNull TimeUnit unit)
            throws InterruptedException, ExecutionException, TimeoutException {
        return delegate.invokeAny(ContextAwareCallable.wrap(tasks), timeout, unit);
    }
}
