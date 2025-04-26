package com.example.load.balancer.extensions.matcher;

import org.junit.jupiter.api.AfterEach;
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

public class DynamicZoneMatcherTest {
    String favoriteZoneName = "favorite-zone";

    ServiceInstance instance = mock(ServiceInstance.class);

    DynamicZoneMatch dynamicZoneMatcher = new DynamicZoneMatch(favoriteZoneName);

    @AfterEach
    public void after() {
        remove();
    }

    @Test
    public void should_filter_when_favorite_zone_not_provided() {
        assertThat(dynamicZoneMatcher.apply(List.of(instance)).size(), is(0));
        assertThat(dynamicZoneMatcher.toString(), is("DynamicZoneMatcher[favorite-zone=null]"));
    }

    @Test
    public void should_filter_when_server_and_context_favorite_zones_are_different() {
        when(instance.getMetadata()).thenReturn(Map.of("zone", "1"));
        current().put(favoriteZoneName, "2");
        assertThat(dynamicZoneMatcher.apply(List.of(instance)).size(), is(0));
        assertThat(dynamicZoneMatcher.toString(), is("DynamicZoneMatcher[favorite-zone=2]"));
    }

    @Test
    public void should_filter_when_server_and_context_favorite_zones_are_equals() {
        when(instance.getMetadata()).thenReturn(Map.of("zone", "1"));
        current().put(favoriteZoneName, "1");
        assertThat(dynamicZoneMatcher.apply(List.of(instance)).size(), is(1));
    }

}