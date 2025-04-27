package com.example.load.balancer.extensions.examples.ribbon;

import com.example.load.balancer.extensions.support.EnableRibbonStrictMetadataMatcher;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRibbonStrictMetadataMatcher
public class RibbonClientsStrictMetadataMatcherConfig {
}
