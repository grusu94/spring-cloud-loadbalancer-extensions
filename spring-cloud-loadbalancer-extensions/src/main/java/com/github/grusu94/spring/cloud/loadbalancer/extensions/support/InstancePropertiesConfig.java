package com.github.grusu94.spring.cloud.loadbalancer.extensions.support;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Configuration for instance properties that supports both Eureka and Consul
 * This provides the appropriate InstanceProperties implementation based on which
 * service discovery is enabled
 */
@Configuration
public class InstancePropertiesConfig {

    /**
     * Eureka instance properties configuration
     * Active when Eureka is enabled or missing (default behavior).
     */
    @Configuration
    @ConditionalOnProperty(name = "eureka.client.enabled", havingValue = "true", matchIfMissing = true)
    @EnableConfigurationProperties(EurekaInstanceProperties.class)
    static class EurekaConfig {

        @Bean
        @Primary
        public InstanceProperties eurekaInstanceProperties(final EurekaInstanceProperties eurekaInstanceProperties) {
            return eurekaInstanceProperties;
        }
    }

    /**
     * Consul instance properties configuration
     * Active when Eureka is not enabled
     */
    @Configuration
    @ConditionalOnProperty(name = "eureka.client.enabled", havingValue = "false")
    @EnableConfigurationProperties(ConsulInstanceProperties.class)
    static class ConsulConfig {

        @Bean
        @Primary
        public InstanceProperties consulInstanceProperties(final ConsulInstanceProperties consulInstanceProperties) {
            return consulInstanceProperties;
        }
    }
}
