package com.github.grusu94.spring.cloud.loadbalancer.extensions.support.strict;

import com.github.grusu94.spring.cloud.loadbalancer.extensions.support.StrictMetadataMatcherConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import jakarta.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = StrictMetadataMatcherConfig.class, properties = {
        "eureka.client.enabled=false",
        "spring.cloud.consul.enabled=false",
        "spring.cloud.consul.discovery.instance-zone=zone1",
        "loadbalancer.client.name=application",
        "loadbalancer.extensions.client.application.rule.strict-metadata-matcher.enabled=false"})
public class StrictMetadataMatcherClientDisabledConsulTest {

    @Inject
    ApplicationContext applicationContext;

    @Test()
    public void should_not_be_instantiated() {
        assertThrows(NoSuchBeanDefinitionException.class,
                () -> applicationContext.getBean(ServiceInstanceListSupplier.class));
    }
}