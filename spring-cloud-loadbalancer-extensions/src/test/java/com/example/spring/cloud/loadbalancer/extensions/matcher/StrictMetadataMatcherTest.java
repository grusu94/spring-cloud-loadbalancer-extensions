package com.example.spring.cloud.loadbalancer.extensions.matcher;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.cloud.client.ServiceInstance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.spring.cloud.loadbalancer.extensions.context.ExecutionContextHolder.current;
import static com.example.spring.cloud.loadbalancer.extensions.context.ExecutionContextHolder.remove;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StrictMetadataMatcherTest {

    private final Map<String, String> metadata = new HashMap<>();

    private final ServiceInstance instance = mock(ServiceInstance.class);

    private final StrictMetadataMatcher strictMetadataMatcher = new StrictMetadataMatcher();

    @BeforeEach
    public void before() {
        remove();
        when(instance.getMetadata()).thenReturn(metadata);
    }

    @AfterEach
    public void after() {
        remove();
        metadata.clear();
    }

    @Test
    public void should_not_filter_server_having_exactly_the_sames_attributes() {
        asList("1", "2").forEach(x -> metadata.put(x, x));
        asList("1", "2").forEach(x -> current().put(x, x));
        assertThat(strictMetadataMatcher.apply(List.of(instance)).size(), is(1));
        assertThat(strictMetadataMatcher.toString(), is("StrictMetadataMatcher[1=1, 2=2]"));
    }

    @Test
    public void should_not_filter_server_having_required_attributes() {
        asList("1", "2", "3").forEach(x -> metadata.put(x, x));
        asList("1", "2").forEach(x -> current().put(x, x));
        assertThat(strictMetadataMatcher.apply(List.of(instance)).size(), is(1));
        assertThat(strictMetadataMatcher.toString(), is("StrictMetadataMatcher[1=1, 2=2]"));
    }

    @Test
    public void should_filter_server_having_a_missing_attributes() {
        asList("1", "3").forEach(x -> metadata.put(x, x));
        asList("1", "2").forEach(x -> current().put(x, x));
        assertThat(strictMetadataMatcher.apply(List.of(instance)).size(), is(0));
    }

    @Test
    public void should_filter_server_having_same_attributes_with_different_value() {
        asList("1", "2").forEach(x -> metadata.put(x, x));
        metadata.put("2", "3");
        asList("1", "2").forEach(x -> current().put(x, x));
        assertThat(strictMetadataMatcher.apply(List.of(instance)).size(), is(0));
    }
}
