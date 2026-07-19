package com.github.grusu94.spring.cloud.loadbalancer.extensions.config;

import org.springframework.boot.autoconfigure.AutoConfigurationImportFilter;
import org.springframework.boot.autoconfigure.AutoConfigurationMetadata;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.lang.NonNull;

public class EurekaAutoConfig implements AutoConfigurationImportFilter, EnvironmentAware {

    private Environment environment;

    @Override
    public boolean[] match(final String[] autoConfigurationClasses,
                           final AutoConfigurationMetadata autoConfigurationMetadata) {
        boolean eurekaAutoConfigEnabled = environment.getProperty("eureka.autoconfig.enabled", Boolean.class, true);
        boolean[] matches = new boolean[autoConfigurationClasses.length];

        for (int i = 0; i < autoConfigurationClasses.length; i++) {
            final String autoConfigClass = autoConfigurationClasses[i];
            if (autoConfigClass != null && isEurekaAutoConfig(autoConfigClass)) {
                matches[i] = eurekaAutoConfigEnabled;
            } else {
                matches[i] = true; // Keep other auto-configurations enabled
            }

        }
        return matches;
    }

    private boolean isEurekaAutoConfig(final String autoConfigClass) {
        return autoConfigClass.contains("org.springframework.cloud.netflix.eureka");
    }

    @Override
    public void setEnvironment(@NonNull Environment environment) {
        this.environment = environment;
    }
}


