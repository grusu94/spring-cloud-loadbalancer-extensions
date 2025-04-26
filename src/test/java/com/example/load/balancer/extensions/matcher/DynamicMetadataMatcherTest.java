package com.example.load.balancer.extensions.matcher;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.cloud.client.ServiceInstance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.load.balancer.extensions.context.ExecutionContextHolder.current;
import static com.example.load.balancer.extensions.context.ExecutionContextHolder.remove;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DynamicMetadataMatcherTest {
    String dynamicAttributeKey = "dynamic";
    String attributeKey = "key";
    String attributeValue = "value";
    Map<String, String> metadata = new HashMap<>();
    ServiceInstance instance = mock(ServiceInstance.class);
    DynamicMetadataMatch dynamicMetadataMatch = new DynamicMetadataMatch(dynamicAttributeKey, false);

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
        metadata.put(attributeKey, attributeValue);
        current().put(dynamicAttributeKey, attributeKey);
        current().put(attributeKey, "");
        assertThat(dynamicMetadataMatch.apply(List.of(instance)).size(), is(0));
    }

    @Test
    public void should_filter_server_when_expected_is_not_defined() {
        metadata.put(attributeKey, attributeValue);
        current().put(dynamicAttributeKey, attributeKey);
        current().put(attributeKey, null);
        assertThat(dynamicMetadataMatch.apply(List.of(instance)).size(), is(0));
    }

    @Test
    public void should_filter_server_when_dynamic_key_not_defined() {
        metadata.put(attributeKey, attributeValue);
        current().put(attributeKey, attributeValue);
        assertThat(dynamicMetadataMatch.apply(List.of(instance)).size(), is(0));
        assertThat(dynamicMetadataMatch.toString(), is("DynamicMetadataMatcher[(dynamic=null)=null,matchIfMissing=false]"));
    }

    @Test
    public void should_not_filter_server_when_dynamic_key_not_defined() {
        DynamicMetadataMatch dynamicMetadataMatch = new DynamicMetadataMatch(dynamicAttributeKey, true);
        metadata.put(attributeKey, attributeValue);
        current().put(attributeKey, attributeValue);
        assertThat(dynamicMetadataMatch.apply(List.of(instance)).size(), is(1));
        assertThat(dynamicMetadataMatch.toString(), is("DynamicMetadataMatcher[(" + dynamicAttributeKey + "=null)=null,matchIfMissing=true]"));
    }

    @Test
    public void should_not_filter_server_with_same_attribute_value() throws Exception {
        metadata.put(attributeKey, attributeValue);
        current().put(dynamicAttributeKey, attributeKey);
        current().put(attributeKey, attributeValue);
        assertThat(dynamicMetadataMatch.apply(List.of(instance)).size(), is(1));
        assertThat(dynamicMetadataMatch.toString(), is("DynamicMetadataMatcher[(" + dynamicAttributeKey + "=" + attributeKey + ")=" + attributeValue + ",matchIfMissing=false]"));
    }

    @Test
    public void should_not_filter_server_with_same_null_attribute_value() {
        metadata.put(attributeKey, null);
        current().put(dynamicAttributeKey, attributeKey);
        current().put(attributeKey, null);
        assertThat(dynamicMetadataMatch.apply(List.of(instance)).size(), is(1));
    }
}