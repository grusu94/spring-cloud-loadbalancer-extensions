package com.example.load.balancer.extensions.matcher;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.cloud.client.ServiceInstance;

import java.util.List;
import java.util.Map;

import static com.example.load.balancer.extensions.context.ExecutionContextHolder.remove;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class InstanceIdMatcherTest {
    String expected = "1";

    ServiceInstance instance = mock(ServiceInstance.class);

    InstanceIdMatch instanceIdMatcher = new InstanceIdMatch(expected);

    @AfterEach
    public void after() {
        remove();
    }

    @Test
    public void should_filter_when_instanceId_not_provided() {
        assertThat(instanceIdMatcher.apply(List.of(instance)).size(), is(0));
        assertThat(instanceIdMatcher.toString(), is("InstanceIdMatcher[instanceId=1]"));
    }

    @Test
    public void should_filter_when_instanceId_is_different() {
        when(instance.getInstanceId()).thenReturn("2");
        when(instance.getMetadata()).thenReturn(Map.of("zone", "1"));
        assertThat(instanceIdMatcher.apply(List.of(instance)).size(), is(0));
        assertThat(instanceIdMatcher.toString(), is("InstanceIdMatcher[instanceId=1]"));
    }

    @Test
    public void should_filter_when_instanceId_is_same() {
        when(instance.getInstanceId()).thenReturn(expected);
        when(instance.getMetadata()).thenReturn(Map.of("zone", "1"));
        assertThat(instanceIdMatcher.apply(List.of(instance)).size(), is(1));
    }
}