package com.github.grusu94.spring.cloud.loadbalancer.extensions.context;

import lombok.extern.slf4j.Slf4j;

/**
 * Execution CONTEXT accessor for servlet applications using ThreadLocal.
 * Stores and retrieves the {@link ExecutionContext} using a ThreadLocal variable.
 */
@Slf4j
public class ThreadLocalContextAccessor implements ContextAccessor {

    /**
     * Singleton instance of ThreadLocalContextAccessor.
     */
    private static final ThreadLocalContextAccessor INSTANCE = new ThreadLocalContextAccessor();

    /**
     * Stores the {@link ExecutionContext} for current thread.
     */
    private static final ThreadLocal<ExecutionContext> CONTEXT = ThreadLocal.withInitial(() -> {
        log.debug("Instantiated from ThreadLocal");
        return new DefaultExecutionContext();
    });

    /**
     * utility class should not be instantiated
     */
    private ThreadLocalContextAccessor() {
    }

    /**
     * Gets the singleton instance.
     *
     * @return the singleton instance
     */
    public static ThreadLocalContextAccessor getInstance() {
        return INSTANCE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExecutionContext current() {
        return CONTEXT.get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExecutionContext switchTo(ExecutionContext context) {
        CONTEXT.set(context);
        return context;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExecutionContext remove() {
        ExecutionContext current = CONTEXT.get();
        CONTEXT.remove();
        return current;
    }
}
