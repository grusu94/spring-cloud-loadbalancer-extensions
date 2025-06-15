package com.github.grusu94.spring.cloud.loadbalancer.extensions.support;

import com.github.grusu94.spring.cloud.loadbalancer.extensions.context.ExecutionContext;
import com.github.grusu94.spring.cloud.loadbalancer.extensions.matcher.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static com.github.grusu94.spring.cloud.loadbalancer.extensions.support.LoadBalancerExtensionsConstants.*;

/**
 * The Favorite zone load balancing rule configuration.
 * <p>Should not be imported directly for further compatibility reason: please use {@link EnableLoadBalancerFavoriteZone}
 * <p>Favorite Rule Definition
 * <ul>
 * <li>Start applying {@link DynamicZoneMatcher} : choose a server in the favorite zone defined in the {@link ExecutionContext}.
 * <li>Fallbacks to {@link ZoneAffinityMatcher}: choose a server in the same zone as the current instance.
 * <li>Fallbacks to {@link DynamicZoneMatcher}: choose a server in the up stream zone defined in the {@link ExecutionContext}.
 * <li>Fallbacks to {@link ZoneAffinityMatcher}: choose a server in the fallback zone.
 * <li>Fallbacks to {@link ZoneAvoidanceMatcher} &amp; {@link AvailabilityMatcher}: choose an available server avoiding blacklisted zones.
 * <li>Fallbacks to {@link AvailabilityMatcher}: choose an available server.
 * <li>Fallbacks to any server
 * </ul>
 * @see EnableLoadBalancerFavoriteZone
 */
@Configuration
@ConditionalOnProperty(value = FAVORITE_ZONE_RULE_ENABLED, matchIfMissing = true)
@ConditionalOnExpression(value = FAVORITE_ZONE_RULE_CLIENT_ENABLED_EXPRESSION)
@EnableConfigurationProperties(FavoriteZoneConfig.FavoriteZoneProperties.class)
@Slf4j
public class FavoriteZoneConfig {

    /**
     * Favorite zone rule bean.
     *
     * @param favoriteZoneProperties the favorite zone properties
     * @param propagationProperties  the propagation properties
     * @return the favorite zone rule
     */

    @Bean
    public ServiceInstanceListSupplier favoriteZone(ConfigurableApplicationContext context,
                                                 EurekaInstanceProperties eurekaInstanceProperties,
                                                 FavoriteZoneProperties favoriteZoneProperties,
                                                 PropagationProperties propagationProperties) {

        final ServiceInstanceListSupplier delegate = ServiceInstanceListSupplier.builder()
                .withDiscoveryClient()
                .build(context);

        final CompositeStrategyMatcher compositeStrategyMatcher = new CompositeStrategyMatcher(List.of(
           new DynamicZoneMatcher(favoriteZoneProperties.getKey()),
           new ZoneAffinityMatcher(eurekaInstanceProperties.getZone()),
           new DynamicZoneMatcher(propagationProperties.getUpStreamZone().getKey()),
           new ZoneAffinityMatcher(favoriteZoneProperties.getFallback()),
           new ZoneAvoidanceMatcher(0.8),
           new AvailabilityMatcher()
        ));
        log.info("Favorite zone enabled for client using key: {}.", favoriteZoneProperties.getKey());

        return new ServiceInstanceListSupplierMatcher(delegate, compositeStrategyMatcher);
    }

    @ConfigurationProperties(FAVORITE_ZONE_PREFIX)
    @Getter
    @Setter
    public static class FavoriteZoneProperties {
        private String key = DEFAULT_FAVORITE_ZONE_KEY;
        private String fallback = DEFAULT_EUREKA_ZONE;
    }
}
