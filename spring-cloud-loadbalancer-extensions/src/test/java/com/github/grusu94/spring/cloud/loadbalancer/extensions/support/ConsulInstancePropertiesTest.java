package com.github.grusu94.spring.cloud.loadbalancer.extensions.support;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = { ConsulInstancePropertiesTest.class },
        properties = {
                "spring.application.name=consul-instance-properties-test",
                "eureka.client.enabled=false",
                "spring.cloud.consul.enabled=true",
                "spring.cloud.consul.discovery.enabled=true",
                "spring.cloud.consul.discovery.instance-zone=test-zone",
                "spring.cloud.consul.discovery.instance-id=test-instance-consul"
        }
)
@Import(InstancePropertiesConfig.class)
public class ConsulInstancePropertiesTest {

    @Autowired
    private InstanceProperties instanceProperties;

    @Test
    public void test_consul_instance_properties_are_injected() {
        assertNotNull(instanceProperties, "InstanceProperties should be injected");
        assertInstanceOf(ConsulInstanceProperties.class,
                instanceProperties,
                "Should be ConsulInstanceProperties when Consul is enabled");
    }

    @Test
    public void test_consul_instance_id_is_available() {
        String instanceId = instanceProperties.getInstanceId();
        assertNotNull(instanceId, "InstanceId should not be null");
        assertTrue(instanceId.contains("test-instance-consul"),
                "Instanceid should contain the configured value");
    }

    @Test
    public void test_consul_zone_is_available() {
        String zone = instanceProperties.getZone();
        assertNotNull(zone, "Zone should not be null");
        assertTrue(zone.contains("test-zone"),
                "Zone should contain the configured value");
    }
}
