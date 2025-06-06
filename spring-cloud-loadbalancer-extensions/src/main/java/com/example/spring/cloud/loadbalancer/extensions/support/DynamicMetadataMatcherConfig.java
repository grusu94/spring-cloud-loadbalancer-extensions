package com.example.spring.cloud.loadbalancer.extensions.support;

import com.example.spring.cloud.loadbalancer.extensions.matcher.DynamicMetadataMatcher;
import com.example.spring.cloud.loadbalancer.extensions.matcher.LoadBalancingStrategyMatcher;
import com.example.spring.cloud.loadbalancer.extensions.matcher.ServiceInstanceListSupplierMatcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.loadbalancer.LoadBalancerAutoConfiguration;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The dynamic attribute metadata matcher load balancing rule definition.
 * <p>Should not be imported directly for further compatibility reason: please use {@link EnableLoadBalancerDynamicMetadataMatcher}.
 * @see DynamicMetadataMatcher
 */
@Configuration
@AutoConfigureBefore(LoadBalancerAutoConfiguration.class)
@ConditionalOnProperty(value = LoadBalancerExtensionsConstants.DYNAMIC_METADATA_MATCHER_RULE_ENABLED, matchIfMissing = true)
@ConditionalOnExpression(value = LoadBalancerExtensionsConstants.DYNAMIC_METADATA_MATCHER_RULE_CLIENT_ENABLED_EXPRESSION)
@Slf4j
public class DynamicMetadataMatcherConfig {

    /**
     * The dynamic key
     */
    @Value("${loadbalancer.extensions.client.${loadbalancer.client.name}.rule.dynamic-metadata-matcher.key:${loadbalancer.extensions.rule.dynamic-metadata-matcher.key:dynamic-matcher-key}}")
    private String key;

    /**
     * The dynamic key
     */
    @Value("${loadbalancer.extensions.client.${loadbalancer.client.name}.rule.dynamic-metadata-matcher.matchIfMissing:${loadbalancer.extensions.rule.dynamic-metadata-matcher.matchIfMissing:true}}")
    private boolean matchIfMissing;

    /**
     * @return an instance of {@link DynamicMetadataMatcher}
     */
    @Bean
    public ServiceInstanceListSupplier dynamicMetadataMatcher(ConfigurableApplicationContext context) {
        final ServiceInstanceListSupplier delegate = ServiceInstanceListSupplier.builder()
                .withDiscoveryClient()
                .build(context);

        final LoadBalancingStrategyMatcher dynamicMetadataMatcher = new DynamicMetadataMatcher(key, matchIfMissing);
        log.info("Dynamic matcher rule enabled for client using dynamic key[{}].", key);

        return new ServiceInstanceListSupplierMatcher(delegate, dynamicMetadataMatcher);
    }
}
