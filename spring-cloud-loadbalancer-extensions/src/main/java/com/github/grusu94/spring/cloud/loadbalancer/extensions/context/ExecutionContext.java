package com.github.grusu94.spring.cloud.loadbalancer.extensions.context;

import java.io.Serializable;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Execution context that stores entries to be used later in the process.
 * <p><strong>WARNING</strong>: This context should never be used to register business logic: Code Reviewers should forbid such behaviors.
 * It is true that storing such logic in the context will reduce drastically the coding effort however this is very bad choice.
 */
public interface ExecutionContext extends Serializable {

    /**
     * Associates the specified value with the specified key in this context.
     * If the context previously contained a entry for the key, the old value is replaced by the specified value.
     * stores the context entry.
     *
     * @param key   the entry key
     * @param value the entry value
     * @return the context instance
     */
    ExecutionContext put(String key, String value);

    /**
     * If the specified key is not already associated with a value (or is mapped to null) associates it with the given value.
     *
     * @param key   the entry key
     * @param value the entry value
     * @return {@code this}
     */
    ExecutionContext putIfAbsent(String key, String value);

    /**
     * Check if an entry key is present.
     *
     * @param key the entry key
     * @return {@code true} when the entry key is present otherwise {@code false}.
     */
    boolean containsKey(String key);

    /**
     * Retrieves the entry value matching the given key.
     *
     * @param key the entry key
     * @return the entry value
     */
    String get(String key);

    /**
     * Removes the context entry.
     *
     * @param key the entry key
     * @return {@code this}
     */
    ExecutionContext remove(String key);

    /**
     * Enables concurrent access.
     *
     * @return {@code this}
     */
    ExecutionContext enableConcurrency();


    /**
     * Retrieves the entry set.
     *
     * @return the stored entries.
     */
    Set<Entry<String, String>> entrySet();

    /**
     * Copies the current instance.
     *
     * @return a deep copy of {@code this} instance.
     */
    ExecutionContext copy();
}
