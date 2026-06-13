package com.github.grusu94.spring.cloud.loadbalancer.extensions.context;

import lombok.extern.slf4j.Slf4j;

/**
 * Execution CONTEXT holder.
 */
@Slf4j
public final class ExecutionContextHolder {

    /**
     * utility class should not be instantiated
     */
    private ExecutionContextHolder() {
    }

    /**
     * Retrieves the current CONTEXT.
     *
     * @return the current CONTEXT.
     */
    public static ExecutionContext current() {
        return ContextAccessorFactory.getContextAccessor().current();
    }

    /**
     * Switches the current CONTEXT to the provided one.
     *
     * @param context the current CONTEXT replacement.
     * @return the current CONTEXT.
     */
    public static ExecutionContext switchTo(ExecutionContext context) {
        return ContextAccessorFactory.getContextAccessor().switchTo(context);
    }

    /**
     * Removes the current CONTEXT.
     *
     * @return the CONTEXT that have been removed.
     */
    public static ExecutionContext remove() {
        return ContextAccessorFactory.getContextAccessor().remove();
    }
}
