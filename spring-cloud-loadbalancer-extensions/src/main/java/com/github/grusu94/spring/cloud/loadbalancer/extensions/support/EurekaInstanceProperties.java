package com.github.grusu94.spring.cloud.loadbalancer.extensions.support;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static com.github.grusu94.spring.cloud.loadbalancer.extensions.support.LoadBalancerExtensionsConstants.*;

/**
 * Eureka instance properties.
 */
@ConfigurationProperties(prefix = EUREKA_INSTANCE_PREFIX)
@Component
@Getter
@Setter
public class EurekaInstanceProperties {
    /**
     * the eureka client meta data
     */
    private Map<String, String> metadataMap = new HashMap<>();

    /**
     * @return the eureka client instance id
     */
    public String getInstanceId() {
        return metadataMap.get(EUREKA_INSTANCE_ID);
    }

    /**
     * @return the eureka client zone
     */
    public String getZone() {
        return metadataMap.getOrDefault(EUREKA_ZONE_PROPERTY, DEFAULT_EUREKA_ZONE);
    }
}
