package com.example.spring.cloud.loadbalancer.extensions.support;

import com.example.spring.cloud.loadbalancer.extensions.matcher.ServiceInstanceListSupplierMatcher;
import com.example.spring.cloud.loadbalancer.extensions.matcher.StrictMetadataMatcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.loadbalancer.LoadBalancerAutoConfiguration;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.example.spring.cloud.loadbalancer.extensions.support.LoadBalancerExtensionsConstants.STRICT_METADATA_MATCHER_RULE_CLIENT_ENABLED_EXPRESSION;
import static com.example.spring.cloud.loadbalancer.extensions.support.LoadBalancerExtensionsConstants.STRICT_METADATA_MATCHER_RULE_ENABLED;

/**
 * The strict metadata matcher load balancing rule definition.
 * <p>Should not be imported directly for further compatibility reason: please use {@link EnableLoadBalancerStrictMetadataMatcher}.
 * @see StrictMetadataMatcher
 */
@Configuration
@AutoConfigureBefore(LoadBalancerAutoConfiguration.class)
@ConditionalOnProperty(value = STRICT_METADATA_MATCHER_RULE_ENABLED, matchIfMissing = true)
@ConditionalOnExpression(value = STRICT_METADATA_MATCHER_RULE_CLIENT_ENABLED_EXPRESSION)
@Slf4j
public class StrictMetadataMatcherConfig {

    /**
     * @return an instance of {@link StrictMetadataMatcher}
     */
    @Bean
    public ServiceInstanceListSupplier strictMetadataMatcher(ConfigurableApplicationContext context) {

        final ServiceInstanceListSupplier delegate = ServiceInstanceListSupplier.builder()
                .withDiscoveryClient()
                .build(context);

        log.info("Strict metadata matcher enabled for client.");
        return new ServiceInstanceListSupplierMatcher(delegate, new StrictMetadataMatcher());
    }
}
