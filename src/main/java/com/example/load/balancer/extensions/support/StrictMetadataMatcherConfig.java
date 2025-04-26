package com.example.load.balancer.extensions.support;

import com.example.load.balancer.extensions.matcher.StrictMetadataMatch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.loadbalancer.LoadBalancerAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
public class StrictMetadataMatcherConfig {

    /**
     * @return an instance of {@link StrictMetadataMatch}
     */
    @Bean
    public StrictMetadataMatch strictMetadataMatcher() {
        StrictMetadataMatch strictMetadataMatcher = new StrictMetadataMatch();
        log.info("Strict metadata matcher enabled for client.");
        return strictMetadataMatcher;
    }
}
