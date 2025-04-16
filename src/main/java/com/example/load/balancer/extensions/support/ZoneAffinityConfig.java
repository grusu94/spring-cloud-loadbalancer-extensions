package com.example.load.balancer.extensions.support;

import com.example.load.balancer.extensions.matcher.*;
import com.example.load.balancer.extensions.predicate.ZoneAffinityMatcher;
import com.netflix.loadbalancer.AvailabilityPredicate;
import com.netflix.loadbalancer.CompositePredicate;
import com.netflix.loadbalancer.DynamicServerListLoadBalancer;
import com.netflix.loadbalancer.ZoneAvoidancePredicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.loadbalancer.LoadBalancerAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.example.load.balancer.extensions.rule.RuleDescription.from;
import static com.example.load.balancer.extensions.support.RibbonExtensionsConstants.*;

/**
 * The Zone Affinity load balancing rule configuration.
 * <p>Should not be imported directly for further compatibility reason: please use {@link EnableRibbonZoneAffinity}
 * <p>Zone Affinity Rule Definition
 * <ul>
 * <li>Fallbacks to {@link ZoneAffinityMatcher}: choose a server in the same zone as the current instance.
 * <li>Fallbacks to {@link ZoneAvoidancePredicate} &amp; {@link AvailabilityPredicate}: choose an available server avoiding blacklisted zones.
 * <li>Fallbacks to {@link AvailabilityPredicate}: choose an available server.
 * <li>Fallbacks to any server
 * </ul>
 * <p><strong>Warning:</strong> Unless mastering the load balancing rules, do not mix with {@link ZonePreferenceServerListFilter} which is used by {@link DynamicServerListLoadBalancer} @see {@link #serverListFilter()}
 *
 * @author Nadim Benabdenbi
 * @see EnableRibbonZoneAffinity
 */
@Configuration
@AutoConfigureBefore(LoadBalancerAutoConfiguration.class)
@ConditionalOnProperty(value = ZONE_AFFINITY_RULE_ENABLED, matchIfMissing = true)
@ConditionalOnExpression(value = ZONE_AFFINITY_RULE_CLIENT_ENABLED_EXPRESSION)
@Slf4j
public class ZoneAffinityConfig extends RuleBaseConfig {

    /**
     * Zone affinity rule.
     *
     * @param rule         the predicate rule support
     * @return the zone affinity rule.
     */
    @Bean
    public CompositeStrategyMatch zoneAffinity(PredicateBasedRuleSupport rule) {
        AvailabilityMatch availabilityMatch = new AvailabilityMatch();
        ZoneAvoidanceMatch zoneAvoidanceMatch = new ZoneAvoidanceMatch(0.8);
        ZoneAffinityMatch zoneAffinityMatcher = new ZoneAffinityMatch(getEurekaInstanceProperties().getZone());
        CompositeStrategyMatch predicate = withPredicates(zoneAffinityMatcher)
                .addFallbackPredicate(withPredicates(zoneAvoidanceMatch, availabilityMatch).build())
                .addFallbackPredicate(availabilityMatch)
                .addFallbackPredicate(new AlwaysTrueStrategy())
                .build();
        rule.setMatcher(predicate);
        rule.setDescription(from(zoneAffinityMatcher)
                .fallback(from(ZONE_AVOIDANCE_PREDICATE_DESCRIPTION).and(from(AVAILABILITY_PREDICATE_DESCRIPTION)))
                .fallback(from(AVAILABILITY_PREDICATE_DESCRIPTION))
                .fallback(from(ANY_PREDICATE_DESCRIPTION)));
        log.info("Zone affinity enabled for client.");
        return predicate;
    }
}
