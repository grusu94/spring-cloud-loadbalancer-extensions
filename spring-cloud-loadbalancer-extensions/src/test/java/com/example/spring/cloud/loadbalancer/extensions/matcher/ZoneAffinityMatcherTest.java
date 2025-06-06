package com.example.spring.cloud.loadbalancer.extensions.matcher;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.cloud.client.ServiceInstance;

import java.util.List;
import java.util.Map;

import static com.example.spring.cloud.loadbalancer.extensions.context.ExecutionContextHolder.remove;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ZoneAffinityMatcherTest {
    private final String expectedZone = "zone1";

    private final ServiceInstance instance = mock(ServiceInstance.class);

    private final ZoneAffinityMatcher zoneAffinityMatcher = new ZoneAffinityMatcher(expectedZone);

    @AfterEach
    public void after() {
        remove();
    }

    @Test
    public void should_filter_when_server_zone_is_different() {
        when(instance.getMetadata()).thenReturn(Map.of("zone", "zone2"));

        assertThat(zoneAffinityMatcher.apply(List.of(instance)).size(), is(0));
        assertThat(zoneAffinityMatcher.toString(), is("ZoneAffinityMatcher[zone=zone1]"));
    }

    @Test
    public void should_not_filter_when_server_zone_is_same() {
        when(instance.getMetadata()).thenReturn(Map.of("zone", expectedZone));

        assertThat(zoneAffinityMatcher.apply(List.of(instance)).size(), is(1));
    }
}
