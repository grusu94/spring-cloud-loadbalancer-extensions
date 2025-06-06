package com.example.spring.cloud.loadbalancer.extensions.propagator.concurrent;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import static com.example.spring.cloud.loadbalancer.extensions.context.ExecutionContextHolder.current;
import static java.util.Arrays.asList;
import static java.util.concurrent.Executors.newSingleThreadExecutor;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ExecutorServicePropagatorTest extends AbstractExecutionContextAwareExecutorTest {
    private final ExecutorService delegate = mock(ExecutorService.class);
    private final ContextAwareExecutorService mocked = new ContextAwareExecutorService(delegate);
    private final ContextAwareExecutorService propagator = new ContextAwareExecutorService(newSingleThreadExecutor());


    @Test
    public void testShutdown() {
        mocked.shutdown();
        verify(delegate).shutdown();
    }

    @Test
    public void testShutdownNow() {
        mocked.shutdownNow();
        verify(delegate).shutdownNow();
    }

    @Test
    public void testIsShutdown() {
        mocked.isShutdown();
        verify(delegate).isShutdown();
    }

    @Test
    public void testIsTerminated() {
        mocked.isTerminated();
        verify(delegate).isTerminated();
    }

    @Test
    public void testAwaitTermination() throws Exception {
        mocked.awaitTermination(1, TimeUnit.MILLISECONDS);
        verify(delegate).awaitTermination(1, TimeUnit.MILLISECONDS);
    }


    @Test
    public void testSubmitCallable() throws Exception {
        current().put(key, value);
        assertThat(propagator.submit(callable).get(), is(value));
    }

    @Test
    public void testSubmitRunnable() throws Exception {
        current().put(key, value);
        propagator.submit(runnable).get();
        assertThat(signal.poll(1, SECONDS), is(value));
    }

    @Test
    public void testSubmitRunnableWithResult() throws Exception {
        current().put(key, value);
        propagator.submit(runnable, true).get();
        assertThat(signal.poll(1, SECONDS), is(value));
    }

    @Test
    public void testInvokeAll() throws Exception {
        current().put(key, value);
        assertThat(propagator.invokeAll(asList(callable, callable))
                .stream()
                .map(AbstractExecutionContextAwareExecutorTest::uncheck)
                .reduce((x, y) -> x + y)
                .get(), is(value + value));
    }

    @Test
    public void testInvokeAllWithTimeOut() throws Exception {
        current().put(key, value);
        assertThat(propagator.invokeAll(asList(callable, callable), 10, SECONDS)
                .stream()
                .map(AbstractExecutionContextAwareExecutorTest::uncheck)
                .reduce((x, y) -> x + y)
                .get(), is(value + value));
    }

    @Test
    public void testInvokeAny() throws Exception {
        current().put(key, value);
        assertThat(propagator.invokeAny(asList(callable, callable)), is(value));
    }

    @Test
    public void testInvokeAnyWithTimeOut() throws Exception {
        current().put(key, value);
        assertThat(propagator.invokeAny(asList(callable, callable), 10, SECONDS), is(value));
    }
}