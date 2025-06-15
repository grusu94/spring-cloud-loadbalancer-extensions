package com.example.spring.cloud.loadbalancer.extensions.support.strategy;

import com.example.spring.cloud.loadbalancer.extensions.propagator.resilience4j.ExecutionContextAwareResilience4jStrategy;
import com.example.spring.cloud.loadbalancer.extensions.propagator.resilience4j.Resilience4jContextPlugin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JAutoConfiguration;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * Default Resilience4j execution context propagation strategy.
 *
 * @see ExecutionContextAwareResilience4jStrategy
 */
@Configuration
@ConditionalOnClass(name = "io.github.resilience4j.circuitbreaker.CircuitBreaker")
@ConditionalOnProperty(value = "loadbalancer.extensions.propagation.hystrix.enabled", matchIfMissing = true)
@ConditionalOnExpression(value = "${loadbalancer.extensions.propagation.enabled:true}")
@AutoConfigureAfter(Resilience4JAutoConfiguration.class)
@Slf4j
public class PreservesExecutionContextResilience4jStrategy {

    /**
     * Decorates the current Resilience4j concurrent strategy.
     */
    @PostConstruct
    public void postConstruct() {
        init();
    }

    /**
     * registers the {@link ExecutionContextAwareResilience4jStrategy}
     */
    public void init() {
        Resilience4jContextPlugin.register(new ExecutionContextAwareResilience4jStrategy());
        log.info("Context propagation ENABLED for Resilience4j.");
    }
}
