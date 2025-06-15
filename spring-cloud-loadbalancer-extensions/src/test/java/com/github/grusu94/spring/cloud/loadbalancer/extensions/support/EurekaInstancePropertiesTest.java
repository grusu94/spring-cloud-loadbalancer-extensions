package com.github.grusu94.spring.cloud.loadbalancer.extensions.support;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class EurekaInstancePropertiesTest {
    EurekaInstanceProperties properties = new EurekaInstanceProperties();

    @Test
    public void metadata() throws Exception {
        assertNull(properties.getMetadataMap().get("key"));
        properties.setMetadataMap(new HashMap<>() {
            {
                put("key", "value");
            }
        });
        assertEquals("value", properties.getMetadataMap().get("key"));
    }

    @Test
    public void getInstanceId() {
        assertNull(properties.getInstanceId());
        properties.getMetadataMap().put("instanceId", "instanceId");
        assertEquals("instanceId", properties.getInstanceId());
    }

    @Test
    public void getZone() {
        assertEquals("default", properties.getZone());
        properties.getMetadataMap().put("zone", "zone");
        assertEquals("zone", properties.getZone());
    }
}