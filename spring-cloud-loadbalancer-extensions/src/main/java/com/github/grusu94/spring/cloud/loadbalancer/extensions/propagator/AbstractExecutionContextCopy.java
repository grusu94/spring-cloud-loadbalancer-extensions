package com.github.grusu94.spring.cloud.loadbalancer.extensions.propagator;

import com.github.grusu94.spring.cloud.loadbalancer.extensions.context.ExecutionContext;
import com.github.grusu94.spring.cloud.loadbalancer.extensions.context.ExecutionContextHolder;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Abstract {@link ExecutionContext} Copy to a target object.
 * <p>Defines {@link #copy(Object)} method that copies current {@link ExecutionContext} entries
 * pre-filtering using the defined {@link #filter} and the defined copy function {@link #executionContextCopyFunction}.
 *
 * @param <T> The target type of the copy.
 */
@Slf4j
@Getter
public class AbstractExecutionContextCopy<T> {
    /**
     * The context entry key filter.
     */
    private final Filter<String> filter;

    /**
     * The execution context copy function.
     */
    private final ExecutionContextCopyFunction<T> executionContextCopyFunction;

    /**
     * The extra static entries to copy.
     */
    private final Map<String, String> extraStaticEntries;

    /**
     * Constructor.
     *
     * @param filter                       the context entry key filter
     * @param executionContextCopyFunction the execution context copy function.
     * @param extraStaticEntries           The extra static entries to copy.
     */
    public AbstractExecutionContextCopy(@NotNull Filter<String> filter,
                                        @NotNull ExecutionContextCopyFunction<T> executionContextCopyFunction,
                                        @NotNull Map<String, String> extraStaticEntries) {
        this.filter = filter;
        this.executionContextCopyFunction = executionContextCopyFunction;
        this.extraStaticEntries = extraStaticEntries;
    }

    /**
     * Copies current {@link ExecutionContext} attributes keys that matches {@link #filter} using the {@link #executionContextCopyFunction}.
     *
     * @param t the target type of the copy.
     * @return the copied {@link ExecutionContext} entries.
     */
    protected Set<Entry<String, String>> copy(T t) {
        Set<Entry<String, String>> result = new HashSet<>();
        copy(t, ExecutionContextHolder.current().entrySet(), result);
        copy(t, extraStaticEntries.entrySet(), result);
        return result;
    }

    /**
     * Copies the entry set that matches {@link #filter} using the {@link #executionContextCopyFunction}.
     *
     * @param t        the target type of the copy.
     * @param entrySet the entry set to copy
     * @param result   the copied entries
     */
    private void copy(T t, Set<Entry<String, String>> entrySet, Set<Entry<String, String>> result) {
        entrySet.stream()
                .filter(x -> filter.accept(x.getKey()))
                .forEach(x -> {
                    try {
                        executionContextCopyFunction.copy(t, x.getKey(), x.getValue());
                        result.add(x);
                    } catch (Exception e) {
                        log.debug("Failed to copy {}.", x, e);
                    }
                });
    }

    /**
     * The function that copies the execution context attributes to a target object.
     *
     * @param <T> the target type of the copy.
     */
    @FunctionalInterface
    public interface ExecutionContextCopyFunction<T> {

        /**
         * Copies the execution context attributes to the target object.
         *
         * @param t     the target.
         * @param key   the attribute key to copy.
         * @param value the attribute value to copy.
         * @throws Exception the checked sub class exception
         */
        void copy(T t, String key, String value) throws Exception;
    }
}
