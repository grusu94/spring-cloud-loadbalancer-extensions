package com.github.grusu94.spring.cloud.loadbalancer.extensions.support.strategy;

import com.github.grusu94.spring.cloud.loadbalancer.extensions.propagator.concurrent.*;
import com.github.grusu94.spring.cloud.loadbalancer.extensions.support.PropagationProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.task.AsyncListenableTaskExecutor;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.SchedulingTaskExecutor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.regex.Pattern;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.mock;

public class PreservesExecutionContextExecutorStrategyTest {
    private final String toBeExcluded = "toBeExcluded";
    private PreservesExecutionContextExecutorStrategy processor;

    @BeforeEach
    public void before() {
        final PropagationProperties properties = new PropagationProperties();
        processor = new PreservesExecutionContextExecutorStrategy(properties);
        properties.getExecutor().getExcludes().add(Pattern.compile(toBeExcluded));
    }

    @Test
    public void postProcessAfterInitialization() {
        assertThat(processor.postProcessAfterInitialization(mock(Executor.class), toBeExcluded).getClass(),
                not(equalTo(ContextAwareExecutor.class)));
        //concurrent
        final String beanName = "bean";
        assertThat(processor.postProcessAfterInitialization(mock(Executor.class), beanName).getClass(),
                equalTo(ContextAwareExecutor.class));
        assertThat(processor.postProcessAfterInitialization(mock(ExecutorService.class), beanName).getClass(),
                equalTo(ContextAwareExecutorService.class));
        assertThat(processor.postProcessAfterInitialization(mock(ScheduledExecutorService.class), beanName).getClass(),
                equalTo(ContextAwareScheduledExecutorService.class));

        //spring
        assertThat(processor.postProcessAfterInitialization(mock(TaskScheduler.class), beanName).getClass(),
                equalTo(ContextAwareTaskScheduler.class));
        assertThat(processor.postProcessAfterInitialization(new ThreadPoolTaskExecutor(), beanName).getClass(),
                equalTo(ContextAwareThreadPoolTaskExecutor.class));
        assertThat(processor.postProcessAfterInitialization(new ThreadPoolTaskScheduler(), beanName).getClass(),
                equalTo(ContextAwareThreadPoolTaskScheduler.class));
        assertThat(processor.postProcessAfterInitialization(mock(AsyncListenableTaskExecutor.class), beanName).getClass(),
                equalTo(ContextAwareAsyncListenableTaskExecutor.class));
        assertThat(processor.postProcessAfterInitialization(mock(AsyncTaskExecutor.class), beanName).getClass(),
                equalTo(ContextAwareAsyncTaskExecutor.class));
        assertThat(processor.postProcessAfterInitialization(mock(SchedulingTaskExecutor.class), beanName).getClass(),
                equalTo(ContextAwareSchedulingTaskExecutor.class));
    }
}