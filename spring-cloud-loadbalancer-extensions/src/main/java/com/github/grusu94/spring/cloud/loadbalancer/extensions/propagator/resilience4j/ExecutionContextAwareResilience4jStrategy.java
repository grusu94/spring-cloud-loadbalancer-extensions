package com.github.grusu94.spring.cloud.loadbalancer.extensions.propagator.resilience4j;

import com.github.grusu94.spring.cloud.loadbalancer.extensions.context.ExecutionContext;
import com.github.grusu94.spring.cloud.loadbalancer.extensions.propagator.concurrent.ContextAwareCallable;
import io.github.resilience4j.decorators.Decorators;

import java.util.concurrent.Callable;

import static com.github.grusu94.spring.cloud.loadbalancer.extensions.propagator.concurrent.ContextAwareCallable.wrap;

/**
 * Preserves the {@link ExecutionContext} on async Resilience4j</a>
 *
 * @see ContextAwareCallable
 */
public class ExecutionContextAwareResilience4jStrategy {

    public <T> Callable<T> decorate(Callable<T> callable) {
        Callable<T> wrapped = this.wrapCallable(callable);
        return Decorators.ofCallable(wrapped)
                .decorate();
    }

    private <T> Callable<T> wrapCallable(Callable<T> callable) {
        return wrap(callable);
    }
}
