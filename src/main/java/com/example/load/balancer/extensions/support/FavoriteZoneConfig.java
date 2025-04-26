package com.example.load.balancer.extensions.support;

import com.example.load.balancer.extensions.context.ExecutionContext;
import com.example.load.balancer.extensions.matcher.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.example.load.balancer.extensions.support.RibbonExtensionsConstants.*;

/**
 * The Favorite zone load balancing rule configuration.
 * <p>Should not be imported directly for further compatibility reason: please use {@link EnableRibbonFavoriteZone}
 * <p>Favorite Rule Definition
 * <ul>
 * <li>Start applying {@link DynamicZoneMatch} : choose a server in the favorite zone defined in the {@link ExecutionContext}.
 * <li>Fallbacks to {@link ZoneAffinityMatch}: choose a server in the same zone as the current instance.
 * <li>Fallbacks to {@link DynamicZoneMatch}: choose a server in the up stream zone defined in the {@link ExecutionContext}.
 * <li>Fallbacks to {@link ZoneAffinityMatch}: choose a server in the fallback zone.
 * <li>Fallbacks to {@link ZoneAvoidanceMatch} &amp; {@link AvailabilityMatch}: choose an available server avoiding blacklisted zones.
 * <li>Fallbacks to {@link AvailabilityMatch}: choose an available server.
 * <li>Fallbacks to any server
 * </ul>
 *
 * @author Nadim Benabdenbi
 * @see EnableRibbonFavoriteZone
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
    public CompositeStrategyMatch favoriteZone(FavoriteZoneProperties favoriteZoneProperties,
                                               PropagationProperties propagationProperties) {
//        AvailabilityMatch availabilityMatch = new AvailabilityMatch();
//        ZoneAvoidanceMatch zoneAvoidanceMatch = new ZoneAvoidanceMatch(0.8);
//        ZoneAffinityMatch zoneAffinityMatch = new ZoneAffinityMatch(getEurekaInstanceProperties().getZone());
//        DynamicZoneMatch dynamicZoneMatch = new DynamicZoneMatch(favoriteZoneProperties.getKey());
//        Builder builder = withPredicates(dynamicZoneMatcher)
//                .addFallbackPredicate(zoneAffinityMatcher);
//        RuleDescription favoriteZoneDescription = from(dynamicZoneMatcher)
//                .fallback(from(zoneAffinityMatcher));
//        DynamicZoneMatch upstreamZoneMatcher = new DynamicZoneMatch(propagationProperties.getUpStreamZone().getKey());
//        builder.addFallbackPredicate(upstreamZoneMatcher);
//        favoriteZoneDescription.fallback(from(upstreamZoneMatcher));
//        ZoneAffinityMatch fallbackZoneMatcher = new ZoneAffinityMatch(favoriteZoneProperties.getFallback());
//        builder.addFallbackPredicate(fallbackZoneMatcher);
//        favoriteZoneDescription.fallback(from(fallbackZoneMatcher));
//        CompositeStrategyMatch favoriteZonePredicate = builder
//                .addFallbackPredicate(withPredicates(zoneAvoidanceMatch, availabilityMatch).build())
//                .addFallbackPredicate(availabilityMatch)
//                .addFallbackPredicate(new AlwaysTrueStrategy())
//                .build();
//        rule.setMatcher(favoriteZonePredicate);
//        rule.setDescription(favoriteZoneDescription
//                .fallback(from(ZONE_AVOIDANCE_PREDICATE_DESCRIPTION).and(from(AVAILABILITY_PREDICATE_DESCRIPTION)))
//                .fallback(from(AVAILABILITY_PREDICATE_DESCRIPTION))
//                .fallback(from(ANY_PREDICATE_DESCRIPTION)));
//        log.info("Favorite zone enabled for client:'{}' using key:'{}' upstream-key:'{}' .",
//                clientConfig.getClientName(), favoriteZoneProperties.getKey());
//        return favoriteZonePredicate;
        return null;
    }

    @ConfigurationProperties(FAVORITE_ZONE_PREFIX)
    @Getter
    @Setter
    public static class FavoriteZoneProperties {
        private String key = DEFAULT_FAVORITE_ZONE_KEY;
        private String fallback = DEFAULT_EUREKA_ZONE;
    }
}
