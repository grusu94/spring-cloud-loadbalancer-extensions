package com.github.grusu94.spring.cloud.loadbalancer.extensions.support;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

public class LoadBalancerExtensionsConstantsTest {

    @Test
    public void stupid_constructor_call() throws Exception {
        Constructor<LoadBalancerExtensionsConstants> constructor = LoadBalancerExtensionsConstants.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        constructor.newInstance();
    }
}