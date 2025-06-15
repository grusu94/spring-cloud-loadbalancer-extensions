package com.github.grusu94.spring.cloud.loadbalancer.extensions.support;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

/**
 * Enables spring http logging filter.
 */
@Configuration
@ConditionalOnWebApplication
@ConditionalOnProperty(value = "http.logging.enabled", matchIfMissing = true)
@EnableConfigurationProperties(HttpLoggingConfig.HttpLoggingProperties.class)
@Slf4j
public class HttpLoggingConfig {

    private final HttpLoggingProperties properties;

    public HttpLoggingConfig(HttpLoggingProperties properties) {
        this.properties = properties;
    }

    public CommonsRequestLoggingFilter requestLoggingFilter() {
        CommonsRequestLoggingFilter loggingFilter = new CommonsRequestLoggingFilter();
        loggingFilter.setIncludeClientInfo(properties.client);
        loggingFilter.setIncludeQueryString(properties.query);
        loggingFilter.setIncludePayload(properties.payload);
        loggingFilter.setIncludeHeaders(properties.headers);
        return loggingFilter;
    }

    @Bean
    @ConditionalOnClass(Logger.class)
    public CommonsRequestLoggingFilter springLogging() {
        Logger logger = (Logger) LoggerFactory.getLogger(CommonsRequestLoggingFilter.class);
        logger.setLevel(Level.DEBUG);
        log.info("Http logging enabled {}.", properties);
        return requestLoggingFilter();
    }

    @ConfigurationProperties("http.logging.include")
    @Getter
    @ToString
    public static class HttpLoggingProperties {
        private final boolean client = true;
        private final boolean query = true;
        private final boolean payload = true;
        private final boolean headers = true;
    }
}
