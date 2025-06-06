package com.example.spring.cloud.loadbalancer.extensions.support.strategy;

import com.example.spring.cloud.loadbalancer.extensions.propagator.concurrent.*;
import com.example.spring.cloud.loadbalancer.extensions.support.PropagationProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncListenableTaskExecutor;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.SchedulingTaskExecutor;
import org.springframework.scheduling.TaskScheduler;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Default executor propagation strategy that will decorate any executor found in the context.
 * <p>Support for java and spring executor: requires however that your dependency injection is base on the interface
 * and not the implementation.
 */
@Configuration
@ConditionalOnProperty(value = "loadbalancer.extensions.propagation.executor.enabled", matchIfMissing = true)
@ConditionalOnExpression(value = "${loadbalancer.extensions.propagation.enabled:true}")
@Slf4j
public class PreservesExecutionContextExecutorStrategy implements InstantiationAwareBeanPostProcessor {

    private final PropagationProperties properties;

    public PreservesExecutionContextExecutorStrategy(PropagationProperties properties) {
        this.properties = properties;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) {
        if (bean instanceof Executor || bean instanceof TaskScheduler) {
            if (properties.getExecutor().accept(beanName)) {
                if (bean instanceof AsyncListenableTaskExecutor && bean instanceof SchedulingTaskExecutor && bean instanceof TaskScheduler) {
                    log.info("Context propagation ENABLED for ~ThreadPoolTaskScheduler [{}]:[{}].", beanName, bean.getClass().getName());
                    return new ContextAwareThreadPoolTaskScheduler((AsyncListenableTaskExecutor) bean, (SchedulingTaskExecutor) bean, (TaskScheduler) bean);
                } else if (bean instanceof AsyncListenableTaskExecutor && bean instanceof SchedulingTaskExecutor) {
                    log.info("Context propagation ENABLED for ~ThreadPoolTaskExecutor [{}]:[{}].", beanName, bean.getClass().getName());
                    return new ContextAwareThreadPoolTaskExecutor((AsyncListenableTaskExecutor) bean, (SchedulingTaskExecutor) bean);
                } else if (bean instanceof TaskScheduler) {
                    log.info("Context propagation ENABLED for TaskScheduler [{}]:[{}].", beanName, bean.getClass().getName());
                    return new ContextAwareTaskScheduler((TaskScheduler) bean);
                } else if (bean instanceof SchedulingTaskExecutor) {
                    log.info("Context propagation ENABLED for SchedulingTaskExecutor [{}]:[{}].", beanName, bean.getClass().getName());
                    return new ContextAwareSchedulingTaskExecutor((SchedulingTaskExecutor) bean);
                } else if (bean instanceof AsyncListenableTaskExecutor) {
                    log.info("Context propagation ENABLED for AsyncListenableTaskExecutor [{}]:[{}].", beanName, bean.getClass().getName());
                    return new ContextAwareAsyncListenableTaskExecutor((AsyncListenableTaskExecutor) bean);
                } else if (bean instanceof AsyncTaskExecutor) {
                    log.info("Context propagation ENABLED for AsyncTaskExecutor [{}]:[{}].", beanName, bean.getClass().getName());
                    return new ContextAwareAsyncTaskExecutor((AsyncTaskExecutor) bean);
                } else if (bean instanceof ScheduledExecutorService) {
                    log.info("Context propagation ENABLED for ScheduledExecutorService [{}]:[{}].", beanName, bean.getClass().getName());
                    return new ContextAwareScheduledExecutorService((ScheduledExecutorService) bean);
                } else if (bean instanceof ExecutorService) {
                    log.info("Context propagation ENABLED for ExecutorService [{}]:[{}].", beanName, bean.getClass().getName());
                    return new ContextAwareExecutorService((ExecutorService) bean);
                } else {
                    log.info("Context propagation ENABLED for Executor [{}]:[{}].", beanName, bean.getClass().getName());
                    return new ContextAwareExecutor((Executor) bean);
                }
            } else {
                log.debug("Context propagation DISABLED for Executor [{}]", beanName);
            }
        }
        return bean;
    }
}
