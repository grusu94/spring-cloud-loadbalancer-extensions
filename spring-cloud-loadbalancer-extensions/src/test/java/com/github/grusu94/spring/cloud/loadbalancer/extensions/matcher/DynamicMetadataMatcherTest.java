package com.github.grusu94.spring.cloud.loadbalancer.extensions.matcher;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.cloud.client.ServiceInstance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.grusu94.spring.cloud.loadbalancer.extensions.context.ExecutionContextHolder.current;
import static com.github.grusu94.spring.cloud.loadbalancer.extensions.context.ExecutionContextHolder.remove;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DynamicMetadataMatcherTest {
    private final String dynamicAttributeKey = "dynamic";
    private final String attributeKey = "key";
    private final String attributeValue = "value";
    private final Map<String, String> metadata = new HashMap<>();
    private final ServiceInstance instance = mock(ServiceInstance.class);
    private final DynamicMetadataMatcher dynamicMetadataMatcher =
            new DynamicMetadataMatcher(dynamicAttributeKey, false);

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
        assertThat(dynamicMetadataMatcher.apply(List.of(instance)).size(), is(0));
    }

    @Test
    public void should_filter_server_when_expected_is_not_defined() {
        metadata.put(attributeKey, attributeValue);
        current().put(dynamicAttributeKey, attributeKey);
        current().put(attributeKey, null);
        assertThat(dynamicMetadataMatcher.apply(List.of(instance)).size(), is(0));
    }

    @Test
    public void should_filter_server_when_dynamic_key_not_defined() {
        metadata.put(attributeKey, attributeValue);
        current().put(attributeKey, attributeValue);
        assertThat(dynamicMetadataMatcher.apply(List.of(instance)).size(), is(0));
        assertThat(dynamicMetadataMatcher.toString(),
                is("DynamicMetadataMatcher[(dynamic=null)=null,matchIfMissing=false]"));
    }

    @Test
    public void should_not_filter_server_when_dynamic_key_not_defined() {
        DynamicMetadataMatcher dynamicMetadataMatcher = new DynamicMetadataMatcher(dynamicAttributeKey, true);
        metadata.put(attributeKey, attributeValue);
        current().put(attributeKey, attributeValue);
        assertThat(dynamicMetadataMatcher.apply(List.of(instance)).size(), is(1));
        assertThat(dynamicMetadataMatcher.toString(), is("DynamicMetadataMatcher[(" + dynamicAttributeKey
                + "=null)=null,matchIfMissing=true]"));
    }

    @Test
    public void should_not_filter_server_with_same_attribute_value() {
        metadata.put(attributeKey, attributeValue);
        current().put(dynamicAttributeKey, attributeKey);
        current().put(attributeKey, attributeValue);
        assertThat(dynamicMetadataMatcher.apply(List.of(instance)).size(), is(1));
        assertThat(dynamicMetadataMatcher.toString(), is("DynamicMetadataMatcher[(" + dynamicAttributeKey
                + "=" + attributeKey + ")=" + attributeValue + ",matchIfMissing=false]"));
    }

    @Test
    public void should_not_filter_server_with_same_null_attribute_value() {
        metadata.put(attributeKey, null);
        current().put(dynamicAttributeKey, attributeKey);
        current().put(attributeKey, null);
        assertThat(dynamicMetadataMatcher.apply(List.of(instance)).size(), is(1));
    }
}
