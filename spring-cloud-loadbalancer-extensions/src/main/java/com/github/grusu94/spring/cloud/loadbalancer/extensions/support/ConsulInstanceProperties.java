package com.github.grusu94.spring.cloud.loadbalancer.extensions.support;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static com.github.grusu94.spring.cloud.loadbalancer.extensions.support.LoadBalancerExtensionsConstants.*;

/**
 * Consul instance properties
 * Provides the same interface as EurekaInstanceProperties for compatibility
 */
@ConfigurationProperties(prefix = CONSUL_INSTANCE_PREFIX)
@Component
@Getter
@Setter
public class ConsulInstanceProperties implements InstanceProperties {
    /**
     * the consul client meta data
     */
    private Map<String, String> metadataMap = new HashMap<>();

    /**
     * the instance ID for consul - maps to spring.cloud.consul.discovery.instance-id
     */
    private String instanceId;

    /**
     * the zone for consul - maps to spring.cloud.consul.discovery.instance-zone
     */
    private String instanceZone = DEFAULT_CONSUL_ZONE;

    /**
     * @return the consul client instance id
     */
    @Override
    public String getInstanceId() {
        if (instanceId != null) {
            return instanceId;
        }

        return metadataMap.get(CONSUL_INSTANCE_ID);
    }

    /**
     * @return the consul client zone
     */
    @Override
    public String getZone() {
        return instanceZone != null ? instanceZone : metadataMap.getOrDefault(CONSUL_ZONE_PROPERTY, DEFAULT_CONSUL_ZONE);
    }
}
