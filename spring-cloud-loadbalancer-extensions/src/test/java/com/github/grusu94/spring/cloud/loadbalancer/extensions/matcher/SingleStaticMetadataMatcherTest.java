package com.github.grusu94.spring.cloud.loadbalancer.extensions.matcher;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.cloud.client.ServiceInstance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.grusu94.spring.cloud.loadbalancer.extensions.context.ExecutionContextHolder.remove;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SingleStaticMetadataMatcherTest {
    private final String attributeKey = "name";
    private final String expectedValue = "value";

    private final Map<String, String> metadata = new HashMap<>();

    ServiceInstance instance = mock(ServiceInstance.class);

    private final SingleStaticMetadataMatcher singleStaticMetadataMatcher =
            new SingleStaticMetadataMatcher(attributeKey, expectedValue);

    @BeforeEach
    public void before() {
        when(instance.getMetadata()).thenReturn(metadata);
    }

    @AfterEach
    public void after() {
        remove();
    }

    @Test
    public void should_not_filter_server_with_expected_attribute_value() {
        metadata.put(attributeKey, expectedValue);
        assertThat(singleStaticMetadataMatcher.apply(List.of(instance)).size(), is(1));
        assertThat(singleStaticMetadataMatcher.toString(), is("StrictMetadataMatcher[name=value]"));
    }

    @Test
    public void should_filter_server_with_no_attribute() {
        assertThat(singleStaticMetadataMatcher.apply(List.of(instance)).size(), is(0));
    }

    @Test
    public void should_filter_server_with_different_attribute_value() {
        metadata.put(attributeKey, attributeKey);
        assertThat(singleStaticMetadataMatcher.apply(List.of(instance)).size(), is(0));
    }
}
