package com.github.grusu94.spring.cloud.loadbalancer.extensions.support;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Enables spring http logging filter.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(HttpLoggingConfig.class)
public @interface EnableHttpLogging {
}
