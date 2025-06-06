package com.example.spring.cloud.loadbalancer.extensions.propagator.concurrent;

import org.junit.jupiter.api.Test;
import org.springframework.core.task.AsyncTaskExecutor;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ContextAwareAsyncTaskExecutorTest extends AbstractExecutionContextAwareExecutorTest {
    private final AsyncTaskExecutor delegate = mock(AsyncTaskExecutor.class);
    private final ContextAwareAsyncTaskExecutor propagator = new ContextAwareAsyncTaskExecutor(delegate);

    @Test
    public void execute() {
        propagator.execute(runnable);
        verify(delegate).execute(any(ContextAwareRunnable.class));
    }

    @Test
    public void submitRunnable() {
        propagator.submit(runnable);
        verify(delegate).submit(any(ContextAwareRunnable.class));
    }

    @Test
    public void submitCallable() {
        propagator.submit(callable);
        verify(delegate).submit(any(ContextAwareCallable.class));
    }
}