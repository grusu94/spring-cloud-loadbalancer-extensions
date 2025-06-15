package com.github.grusu94.spring.cloud.loadbalancer.extensions.support;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Enables favorite zone load balancing rule.
 * <p>To be used at configuration level. For example:
 * <blockquote><pre>
 * &#064;LoadBalancerClients(defaultConfiguration = LoadBalancerClientsConfig.class)
 * &#064;SpringBootApplication
 * public class Application{
 *  ...
 * }
 * &#064;Configuration
 * &#064;EnableLoadBalancerDynamicMetadataMatcher
 * public class LoadBalancerClientsConfig {
 * }
 * </pre></blockquote>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(DynamicMetadataMatcherConfig.class)
public @interface EnableLoadBalancerDynamicMetadataMatcher {
}
