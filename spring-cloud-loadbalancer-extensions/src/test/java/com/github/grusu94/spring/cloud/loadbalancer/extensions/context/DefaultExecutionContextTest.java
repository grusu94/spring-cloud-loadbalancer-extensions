package com.github.grusu94.spring.cloud.loadbalancer.extensions.context;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class DefaultExecutionContextTest {
    final DefaultExecutionContext context = new DefaultExecutionContext();
    final String key1 = "key1";
    final String value1 = "value1";
    final String value2 = "value2";

    @Test
    public void testPut() {
        assertThat(context.put(key1, value1), is(context));
        assertThat(context.get(key1), is(value1));
        assertThat(context.put(key1, value2), is(context));
        assertThat(context.get(key1), is(value2));
    }

    @Test
    public void testPutIfAbsent() {
        assertThat(context.putIfAbsent(key1, value1), is(context));
        assertThat(context.get(key1), is(value1));
        assertThat(context.putIfAbsent(key1, value2), is(context));
        assertThat(context.get(key1), is(value1));
    }

    @Test
    public void testContainsKey() {
        assertThat(context.containsKey(key1), is(false));
        context.put(key1, value1);
        assertThat(context.containsKey(key1), is(true));
    }

    @Test
    public void testRemove() {
        assertThat(context.remove(key1), is(context));
        assertThat(context.containsKey(key1), is(false));
        context.put(key1, value1);
        assertThat(context.containsKey(key1), is(true));
        assertThat(context.remove(key1), is(context));
        assertThat(context.containsKey(key1), is(false));
    }

    @Test
    public void testCopy() {
        assertThat(context.put(key1, value1), is(context));
        assertThat(context.copy().get(key1), is(value1));
    }


    @Test
    public void enableConcurrency() throws Exception {
        assertThat(context.enableConcurrency(), is(context));
        assertThat(context.enableConcurrency(), is(context));
        testConcurrency();
    }

    private void testConcurrency() throws Exception {
        int concurrent = 8;
        ExecutorService executorService = Executors.newFixedThreadPool(concurrent);
        Random random = new Random();
        for (int i = 0; i < 1000; i++) {
            String key = Integer.toString(i);
            context.put(key, key);
        }
        List<Future<Boolean>> futures = new ArrayList<>(concurrent);
        for (int i = 0; i < concurrent; i++) {
            futures.add(executorService.submit(() -> {
                List<Entry<String, String>> shuffles = new ArrayList<>(context.entrySet());
                Collections.shuffle(shuffles);
                shuffles.forEach(x -> {
                            if (random.nextBoolean()) {
                                context.put(x.getKey(), x.getValue());
                            } else {
                                context.remove(x.getKey());
                            }
                        });
                return true;
            }));
        }
        for (Future<Boolean> x : futures) {
            assertThat(x.get(), is(true));
        }

    }
}