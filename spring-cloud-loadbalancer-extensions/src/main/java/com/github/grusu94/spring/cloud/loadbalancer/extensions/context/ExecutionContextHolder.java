package com.github.grusu94.spring.cloud.loadbalancer.extensions.context;

import lombok.extern.slf4j.Slf4j;

/**
 * Execution CONTEXT holder.
 */
@Slf4j
public final class ExecutionContextHolder {

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
    private ExecutionContextHolder() {
    }

    /**
     * Retrieves the current CONTEXT.
     *
     * @return the current CONTEXT.
     */
    public static ExecutionContext current() {
        return CONTEXT.get();
    }

    /**
     * switches the current CONTEXT to the provided one.
     *
     * @param context the current CONTEXT replacement.
     * @return the current CONTEXT.
     */
    public static ExecutionContext switchTo(ExecutionContext context) {
        ExecutionContextHolder.CONTEXT.set(context);
        return context;
    }

    /**
     * removes the current CONTEXT.
     *
     * @return the CONTEXT that have been removed.
     */
    public static ExecutionContext remove() {
        ExecutionContext current = CONTEXT.get();
        CONTEXT.remove();
        return current;
    }

    /**
     * opens a new scope for the provided CONTEXT.
     *
     * @param context the CONTEXT to open a new scope for.
     * @return the {@link ExecutionContextScope} that will close the scope when invoked.
     */
    public static ExecutionContextScope openScope(ExecutionContext context) {
        ExecutionContext previous = current();
        switchTo(context);

        return () -> {
            if (previous == null) {
                remove();
            } else {
                switchTo(previous);
            }
        };
    }
}
