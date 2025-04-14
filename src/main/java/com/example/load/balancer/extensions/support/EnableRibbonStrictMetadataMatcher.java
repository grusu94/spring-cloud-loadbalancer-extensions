package com.example.load.balancer.extensions.support;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Enables strict metadata matcher load balancing rule.
 * <p>To be used at ribbon configuration level. For example:
 * <blockquote><pre>
 * &#064;RibbonClients(defaultConfiguration = RibbonClientsConfig.class)
 * &#064;SpringBootApplication
 * public class Application{
 *  ...
 * }
 * &#064;Configuration
 * &#064;EnableRibbonStrictMetadataMatcher
 * public class RibbonClientsConfig {
 * }
 * </pre></blockquote>
 *
 * @author Nadim Benabdenbi
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(StrictMetadataMatcherConfig.class)
public @interface EnableRibbonStrictMetadataMatcher {
}
