package com.example.load.balancer.extensions.support.strategy;

import com.example.load.balancer.extensions.propagator.resilience4j.ExecutionContextAwareResilience4jStrategy;
import com.example.load.balancer.extensions.propagator.resilience4j.Resilience4jContextPlugin;
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
@ConditionalOnProperty(value = "ribbon.extensions.propagation.hystrix.enabled", matchIfMissing = true)
@ConditionalOnExpression(value = "${ribbon.extensions.propagation.enabled:true}")
@AutoConfigureAfter(Resilience4JAutoConfiguration.class)
@Slf4j
public class PreservesExecutionContextResilience4jStrategy {

    private final ExecutionContextAwareResilience4jStrategy decorator;

    public PreservesExecutionContextResilience4jStrategy(ExecutionContextAwareResilience4jStrategy decorator) {
        this.decorator = decorator;
    }

    /**
     * Decorates the current Resilience4j concurrent strategy.
     */
    @PostConstruct
    public void postConstruct() {
        init();
    }

    /**
     * registers the {@link ExecutionContextAwareResilience4jStrategy}
     * Usages:
     * Resilience4jContextPlugin contextPlugin = new Resilience4jContextPlugin();
     * contextPlugin.get().decorate(() -> someLogic()).get();
     */
    public void init() {
        Resilience4jContextPlugin contextPlugin = new Resilience4jContextPlugin();
        contextPlugin.register(decorator);
        log.info("Context propagation enabled for Resilience4j.");
    }
}
