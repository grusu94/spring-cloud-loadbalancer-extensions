package com.github.grusu94.spring.cloud.loadbalancer.extensions.context;

/**
 * Interface for accessing and managing the {@link ExecutionContext}.
 * Implementations can provide different strategies for storing and retrieving contexts
 * (e.g., ThreadLocal for servlet applications, Reactor Context for reactive applications).
 */
public interface ContextAccessor {

    /**
     * Retrieves the current execution context.
     *
     * @return the current execution context
     */
    ExecutionContext current();

    /**
     * Switches to a new execution context.
     *
     * @param context the new execution context
     * @return the execution context that was set
     */
    ExecutionContext switchTo(ExecutionContext context);

    /**
     * Removes the current execution context.
     *
     * @return the execution context that was removed
     */
    ExecutionContext remove();
}

