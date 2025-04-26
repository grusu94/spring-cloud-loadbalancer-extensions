package com.example.load.balancer.extensions.support;

import com.example.load.balancer.extensions.matcher.DynamicMetadataMatch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.loadbalancer.LoadBalancerAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The dynamic attribute metadata matcher load balancing rule definition.
 * <p>Should not be imported directly for further compatibility reason: please use {@link EnableRibbonDynamicMetadataMatcher}.
 *
 * @author Nadim Benabdenbi
 * @see DynamicMetadataMatch
 */
@Configuration
@AutoConfigureBefore(LoadBalancerAutoConfiguration.class)
@ConditionalOnProperty(value = RibbonExtensionsConstants.DYNAMIC_METADATA_MATCHER_RULE_ENABLED, matchIfMissing = true)
@ConditionalOnExpression(value = RibbonExtensionsConstants.DYNAMIC_METADATA_MATCHER_RULE_CLIENT_ENABLED_EXPRESSION)
@Slf4j
public class DynamicMetadataMatcherConfig {

    /**
     * The dynamic key
     */
    @Value("${ribbon.extensions.client.${ribbon.client.name}.rule.dynamic-metadata-matcher.key:${ribbon.extensions.rule.dynamic-metadata-matcher.key:dynamic-matcher-key}}")
    private String key;

    /**
     * The dynamic key
     */
    @Value("${ribbon.extensions.client.${ribbon.client.name}.rule.dynamic-metadata-matcher.matchIfMissing:${ribbon.extensions.rule.dynamic-metadata-matcher.matchIfMissing:true}}")
    private boolean matchIfMissing;

    /**
     * @return an instance of {@link DynamicMetadataMatch}
     */
    @Bean
    public DynamicMetadataMatch dynamicMetadataMatcher() {
        DynamicMetadataMatch dynamicMetadataMatcher = new DynamicMetadataMatch(key, matchIfMissing);
        log.info("Dynamic matcher rule enabled for client using dynamic key[{}].", key);
        return dynamicMetadataMatcher;
    }
}
