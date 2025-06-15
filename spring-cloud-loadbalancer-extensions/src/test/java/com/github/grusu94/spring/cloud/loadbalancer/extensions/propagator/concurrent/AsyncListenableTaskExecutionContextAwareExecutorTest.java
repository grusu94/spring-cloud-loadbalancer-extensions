package com.github.grusu94.spring.cloud.loadbalancer.extensions.propagator.concurrent;

import org.junit.jupiter.api.Test;
import org.springframework.core.task.AsyncListenableTaskExecutor;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class AsyncListenableTaskExecutionContextAwareExecutorTest extends AbstractExecutionContextAwareExecutorTest {
    private final AsyncListenableTaskExecutor delegate = mock(AsyncListenableTaskExecutor.class);
    private final ContextAwareAsyncListenableTaskExecutor propagator = new ContextAwareAsyncListenableTaskExecutor(delegate);

    @Test
    public void submitRunnable() {
        propagator.submitListenable(runnable);
        verify(delegate).submitListenable(any(ContextAwareRunnable.class));
    }

    @Test
    public void submitCallable() {
        propagator.submitListenable(callable);
        verify(delegate).submitListenable(any(ContextAwareCallable.class));
    }
}