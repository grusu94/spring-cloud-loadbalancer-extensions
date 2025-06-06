package com.example.spring.cloud.loadbalancer.extensions.examples.loadbalancer;

import com.example.spring.cloud.loadbalancer.extensions.support.EnableLoadBalancerStrictMetadataMatcher;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableLoadBalancerStrictMetadataMatcher
public class LoadBalancerClientsStrictMetadataMatcherConfig {
}
