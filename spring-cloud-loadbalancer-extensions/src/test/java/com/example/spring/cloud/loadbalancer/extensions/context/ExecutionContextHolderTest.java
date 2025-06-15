package com.example.spring.cloud.loadbalancer.extensions.context;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import static com.example.spring.cloud.loadbalancer.extensions.context.ExecutionContextHolder.*;
import static java.util.concurrent.Executors.newFixedThreadPool;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.mock;

public class ExecutionContextHolderTest {

    @Test
    public void testConstructor() throws Exception {
        Constructor<?> constructor = ExecutionContextHolder.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        constructor.newInstance();
    }

    @Test
    public void testCurrent() {
        ExecutionContext context = current();
        assertThat(context.entrySet().size(), is(0));
        remove();
    }

    @Test
    public void testSwitchTo() {
        ExecutionContext context = mock(ExecutionContext.class);
        switchTo(context);
        assertThat(current(), is(context));
        remove();
    }

    @Test
    public void context_is_not_inherited_by_child_threads() throws Exception {
        String key = "key";
        String value = "value";
        current().put(key, value);
        ExecutorService executorService = newFixedThreadPool(1);
        Future<String> future = executorService.submit(() -> current().get(key));
        assertThat(future.get(), is(nullValue()));
    }

    @Test
    public void inheritance_do_not_work_when_executor_created_elsewhere() throws Exception {
        //init executor : what will happen with a spring context creating its own executors
        ExecutorService executorService = newFixedThreadPool(1);
        executorService.submit(() -> null).get();
        //test inheritance failure
        String key = "key";
        String value = "value";
        current().put(key, value);
        Future<String> future = executorService.submit(() -> current().get(key));
        assertThat(future.get(), is(nullValue()));
    }

    @AfterEach
    public void after() {
        remove();
    }

}
