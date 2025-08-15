package com.github.grusu94.spring.cloud.loadbalancer.extensions.support;

/**
 * Common interface for instance properties regardless of service discovery implementation
 * This allows the same load balancer logic to work with both Eureka and Consul
 */
public interface InstanceProperties {

    /**
     * @return the service instance id
     */
    String getInstanceId();

    /**
     * @return the zone where this instance is located
     */
    String getZone();
}
