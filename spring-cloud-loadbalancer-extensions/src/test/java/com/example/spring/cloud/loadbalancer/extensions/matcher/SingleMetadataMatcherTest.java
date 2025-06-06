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
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SingleMetadataMatcherTest {
    private final String attributeKey = "key";
    private final String attributeValue = "value";
    private final ServiceInstance instance = mock(ServiceInstance.class);
    private final Map<String, String> metadata = new HashMap<>();

    @BeforeEach
    public void before() {
        when(instance.getMetadata()).thenReturn(metadata);
    }

    @AfterEach
    public void after() {
        remove();
    }

    @Test
    public void should_filter_server_when_different_values() {
        SingleMetadataMatcher singleMetadataMatcher = new SingleMetadataMatcher(attributeKey);
        metadata.put(attributeKey, attributeValue);
        current().put(attributeKey, "");
        assertThat(singleMetadataMatcher.apply(List.of(instance)).size(), is(0));
        assertThat(singleMetadataMatcher.toString(), is("SingleMetadataMatcher[key=]"));
    }

    @Test
    public void should_filter_server_when_expected_is_not_defined() {
        SingleMetadataMatcher singleMetadataMatcher = new SingleMetadataMatcher(attributeKey);
        metadata.put(attributeKey, attributeValue);
        current().put(attributeKey, null);
        assertThat(singleMetadataMatcher.apply(List.of(instance)).size(), is(0));
        assertThat(singleMetadataMatcher.toString(), is("SingleMetadataMatcher[key=null]"));
    }

    @Test
    public void should_not_filter_server_when_empty_context() {
        SingleMetadataMatcher singleMetadataMatcher = new SingleMetadataMatcher(attributeKey);
        assertThat(singleMetadataMatcher.apply(List.of(instance)).size(), is(1));
        assertThat(singleMetadataMatcher.toString(), is("SingleMetadataMatcher[key=null]"));
    }

    @Test
    public void should_not_filter_server_with_same_attribute_value() {
        SingleMetadataMatcher singleMetadataMatcher = new SingleMetadataMatcher(attributeKey);
        metadata.put(attributeKey, attributeValue);
        current().put(attributeKey, attributeValue);
        assertThat(singleMetadataMatcher.apply(List.of(instance)).size(), is(1));
    }

    @Test
    public void should_not_filter_server_with_same_null_attribute_value() {
        SingleMetadataMatcher singleMetadataMatcher = new SingleMetadataMatcher(attributeKey);
        metadata.put(attributeKey, null);
        current().put(attributeKey, null);
        assertThat(singleMetadataMatcher.apply(List.of(instance)).size(), is(1));
    }
}
