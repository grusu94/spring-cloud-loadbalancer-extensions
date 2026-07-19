package com.github.grusu94.spring.cloud.loadbalancer.extensions.config;

import com.github.grusu94.spring.cloud.loadbalancer.extensions.context.ExecutionContextHooks;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@Slf4j
public class ReactiveExecutionContextAutoConfig {

    @PostConstruct
    public void init() {
        ExecutionContextHooks.register();
        log.info("ReactiveExecutionContext propagation is ENABLED.");
    }

    @PreDestroy
    public void destroy() {
        ExecutionContextHooks.unregister();
        log.info("ReactiveExecutionContext propagation is DISABLED.");
    }
}
