package com.github.grusu94.spring.cloud.loadbalancer.extensions.propagator;

/**
 * An interface that decide if an object should be accepted or filtered.
 *
 * @param <T> the type of the filter target object.
 */
@FunctionalInterface
public interface Filter<T> {

    /**
     * Decides if the given object should be accepted or filtered..
     *
     * @param t the object to evaluate
     * @return {@code true} if the object should be accepted otherwise {@code false}.
     */
    boolean accept(T t);
}
