package com.github.grusu94.spring.cloud.loadbalancer.extensions.support.favorite;

import com.github.grusu94.spring.cloud.loadbalancer.extensions.support.FavoriteZoneConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = FavoriteZoneConfig.class, properties = {
        "loadbalancer.extensions.rule.favorite-zone.enabled=false"})
public class FavoriteZoneGloballyDisabledTest {

    @Inject
    ApplicationContext applicationContext;

    @Test()
    public void should_not_be_instantiated() {
        assertThrows(NoSuchBeanDefinitionException.class,
                () -> applicationContext.getBean(ServiceInstanceListSupplier.class));
    }
}