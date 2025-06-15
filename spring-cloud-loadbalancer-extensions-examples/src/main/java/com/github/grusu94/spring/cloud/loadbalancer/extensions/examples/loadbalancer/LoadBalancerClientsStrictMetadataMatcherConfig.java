package com.github.grusu94.spring.cloud.loadbalancer.extensions.examples.loadbalancer;

import com.github.grusu94.spring.cloud.loadbalancer.extensions.support.EnableLoadBalancerStrictMetadataMatcher;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableLoadBalancerStrictMetadataMatcher
public class LoadBalancerClientsStrictMetadataMatcherConfig {
}
