package com.github.grusu94.spring.cloud.loadbalancer.extensions.config;

import org.springframework.boot.autoconfigure.AutoConfigurationImportFilter;
import org.springframework.boot.autoconfigure.AutoConfigurationMetadata;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.lang.NonNull;

public class ConsulAutoConfig implements AutoConfigurationImportFilter, EnvironmentAware {

    private Environment environment;

    @Override
    public boolean[] match(final String[] autoConfigurationClasses,
                           final AutoConfigurationMetadata autoConfigurationMetadata) {
        boolean consulAutoConfigEnabled = environment.getProperty("consul.autoconfig.enabled", Boolean.class, false);
        boolean[] matches = new boolean[autoConfigurationClasses.length];

        for (int i = 0; i < autoConfigurationClasses.length; i++) {
            final String autoConfigClass = autoConfigurationClasses[i];
            if (autoConfigClass != null && isConsulAutoConfig(autoConfigClass)) {
                matches[i] = consulAutoConfigEnabled;
            } else {
                matches[i] = true; // Keep other auto-configurations enabled
            }

        }
        return matches;
    }

    private boolean isConsulAutoConfig(final String autoConfigClass) {
        return autoConfigClass.contains("org.springframework.cloud.consul");
    }

    @Override
    public void setEnvironment(@NonNull Environment environment) {
        this.environment = environment;
    }
}


