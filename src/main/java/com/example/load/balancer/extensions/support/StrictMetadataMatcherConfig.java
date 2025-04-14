package com.example.load.balancer.extensions.support;

import com.example.load.balancer.extensions.matcher.StrictMetadataMatch;
import com.example.load.balancer.extensions.rule.PredicateBasedRuleSupport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.loadbalancer.LoadBalancerAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.example.load.balancer.extensions.rule.RuleDescription.from;
import static com.example.load.balancer.extensions.support.RibbonExtensionsConstants.STRICT_METADATA_MATCHER_RULE_CLIENT_ENABLED_EXPRESSION;
import static com.example.load.balancer.extensions.support.RibbonExtensionsConstants.STRICT_METADATA_MATCHER_RULE_ENABLED;

/**
 * The strict metadata matcher load balancing rule definition.
 * <p>Should not be imported directly for further compatibility reason: please use {@link EnableRibbonStrictMetadataMatcher}.
 *
 * @author Nadim Benabdenbi
 * @see StrictMetadataMatch
 */
@Configuration
@AutoConfigureBefore(LoadBalancerAutoConfiguration.class)
@ConditionalOnProperty(value = STRICT_METADATA_MATCHER_RULE_ENABLED, matchIfMissing = true)
@ConditionalOnExpression(value = STRICT_METADATA_MATCHER_RULE_CLIENT_ENABLED_EXPRESSION)
@Slf4j
public class StrictMetadataMatcherConfig extends RuleBaseConfig {

    /**
     * @param rule         the predicate rule support
     * @return an instance of {@link StrictMetadataMatch}
     */
    @Bean
    public StrictMetadataMatch strictMetadataMatcher(PredicateBasedRuleSupport rule) {
        StrictMetadataMatch strictMetadataMatcher = new StrictMetadataMatch();
        rule.setMatcher(strictMetadataMatcher);
        rule.setDescription(from(strictMetadataMatcher));
        log.info("Strict metadata matcher enabled for client.");
        return strictMetadataMatcher;
    }
}
