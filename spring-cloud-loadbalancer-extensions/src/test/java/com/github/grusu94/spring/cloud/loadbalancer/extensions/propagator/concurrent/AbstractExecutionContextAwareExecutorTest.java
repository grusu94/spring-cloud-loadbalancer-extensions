package com.github.grusu94.spring.cloud.loadbalancer.extensions.propagator.concurrent;

import org.junit.jupiter.api.AfterEach;
import org.springframework.scheduling.Trigger;

import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import static com.github.grusu94.spring.cloud.loadbalancer.extensions.context.ExecutionContextHolder.current;
import static com.github.grusu94.spring.cloud.loadbalancer.extensions.context.ExecutionContextHolder.remove;
import static org.mockito.Mockito.mock;

public abstract class AbstractExecutionContextAwareExecutorTest {
    protected final String key = "key";
    protected final String value = "value";
    protected final BlockingQueue<String> signal = new ArrayBlockingQueue<>(10);
    protected final Runnable runnable = () -> {
        try {
            signal.put(current().get(key));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    };
    protected final Callable<String> callable = () -> {
        try {
            signal.put(current().get(key));
            return current().get(key);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    };
    protected final long period = 1;
    protected final Date date = new Date();
    protected final Trigger trigger = mock(Trigger.class);

    @AfterEach
    public void after() {
        remove();
    }

    public static <T> T uncheck(Future<T> future) {
        try {
            return future.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}