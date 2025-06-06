package com.example.spring.cloud.loadbalancer.extensions.propagator;

import org.junit.jupiter.api.Test;
import com.example.spring.cloud.loadbalancer.extensions.propagator.AbstractExecutionContextCopy.ExecutionContextCopyFunction;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.example.spring.cloud.loadbalancer.extensions.context.ExecutionContextHolder.current;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Answers.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;

public class AbstractExecutionContextCopyTest {
    private final Map<String, String> extraStaticEntries = new HashMap<>() {
        {
            put("extra", "extra");
        }
    };
    private final Set<String> keys = new HashSet<>(asList("1", "2", "extra"));
    private final Map<String, String> collector = new HashMap<>();
    private final ExecutionContextCopyFunction<Map<String, String>> function = Map::put;
    @SuppressWarnings("unchecked")
    private final AbstractExecutionContextCopy<Map<String, String>> propagator =
            (AbstractExecutionContextCopy<Map<String, String>>)
            mock(AbstractExecutionContextCopy.class, withSettings()
                    .defaultAnswer(CALLS_REAL_METHODS)
                    .useConstructor((Filter<String>) keys::contains, function, extraStaticEntries));

    @Test
    public void test_getters() {
        assertThat(propagator.getFilter(), is(notNullValue()));
        assertThat(propagator.getExecutionContextCopyFunction(), is(function));
        assertThat(propagator.getExtraStaticEntries(), is(extraStaticEntries));
    }

    @Test
    public void test_propagate() {
        asList("1", "3", "2").forEach(x -> current().put(x, x));
        propagator.copy(collector);
        assertThat(collector.get("1"), is("1"));
        assertThat(collector.get("2"), is("2"));
        assertThat(collector.get("extra"), is("extra"));
        assertThat(collector.containsKey("3"), is(false));
    }

    @Test
    public void test_propagate_with_exception() {
        asList("1", "3", "2").forEach(x -> current().put(x, x));
        ExecutionContextCopyFunction<Map<String, String>> function = (x, y, z) -> {
            if ("1".equals(y)) {
                throw new IllegalArgumentException(y);
            }
            x.put(y, z);
        };
        @SuppressWarnings("unchecked")
        AbstractExecutionContextCopy<Map<String, String>> propagator = (AbstractExecutionContextCopy<Map<String, String>>)
                mock(AbstractExecutionContextCopy.class, withSettings()
                        .defaultAnswer(CALLS_REAL_METHODS)
                        .useConstructor((Filter<String>) keys::contains, function, extraStaticEntries));
        propagator.copy(collector);
        assertThat(collector.containsKey("1"), is(false));
        assertThat(collector.get("2"), is("2"));
        assertThat(collector.containsKey("3"), is(false));
    }
}