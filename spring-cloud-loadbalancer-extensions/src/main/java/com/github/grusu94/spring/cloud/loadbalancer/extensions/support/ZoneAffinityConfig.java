package com.github.grusu94.spring.cloud.loadbalancer.extensions.support;

import com.github.grusu94.spring.cloud.loadbalancer.extensions.matcher.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.loadbalancer.LoadBalancerAutoConfiguration;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static com.github.grusu94.spring.cloud.loadbalancer.extensions.support.LoadBalancerExtensionsConstants.ZONE_AFFINITY_RULE_CLIENT_ENABLED_EXPRESSION;
import static com.github.grusu94.spring.cloud.loadbalancer.extensions.support.LoadBalancerExtensionsConstants.ZONE_AFFINITY_RULE_ENABLED;

/**
 * @see EnableLoadBalancerZoneAffinity
 */
@Configuration
@AutoConfigureBefore(LoadBalancerAutoConfiguration.class)
@ConditionalOnProperty(value = ZONE_AFFINITY_RULE_ENABLED, matchIfMissing = true)
@ConditionalOnExpression(value = ZONE_AFFINITY_RULE_CLIENT_ENABLED_EXPRESSION)
@Slf4j
public class ZoneAffinityConfig {

    /**
     * Zone affinity rule.
     *
     * @return the zone affinity rule.
     */
    @Bean
    public ServiceInstanceListSupplier zoneAffinity(ConfigurableApplicationContext context,
                                                    InstanceProperties instanceProperties) {

        final ServiceInstanceListSupplier delegate = ServiceInstanceListSupplier.builder()
                .withDiscoveryClient()
                .build(context);

        final CompositeStrategyMatcher compositeStrategyMatcher = new CompositeStrategyMatcher(List.of(
           new ZoneAffinityMatcher(instanceProperties.getZone()),
           new ZoneAvoidanceMatcher(0.8),
           new AvailabilityMatcher()
        ));
        log.info("Zone affinity enabled for client.");

        return new ServiceInstanceListSupplierMatcher(delegate, compositeStrategyMatcher);
    }
}
