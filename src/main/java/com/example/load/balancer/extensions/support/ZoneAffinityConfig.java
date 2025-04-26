package com.example.load.balancer.extensions.support;

import com.example.load.balancer.extensions.matcher.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.loadbalancer.LoadBalancerAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.example.load.balancer.extensions.support.RibbonExtensionsConstants.*;

/**
 * @see EnableRibbonZoneAffinity
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
    public CompositeStrategyMatch zoneAffinity() {
//        AvailabilityMatch availabilityMatch = new AvailabilityMatch();
//        ZoneAvoidanceMatch zoneAvoidanceMatch = new ZoneAvoidanceMatch(0.8);
//        ZoneAffinityMatch zoneAffinityMatcher = new ZoneAffinityMatch(getEurekaInstanceProperties().getZone());
//        log.info("Zone affinity enabled for client.");
//        return zoneAffinityMatcher;
        return null;
    }
}
