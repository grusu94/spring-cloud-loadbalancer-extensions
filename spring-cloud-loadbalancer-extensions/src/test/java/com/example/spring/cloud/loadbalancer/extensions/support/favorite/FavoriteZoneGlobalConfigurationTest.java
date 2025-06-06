package com.example.spring.cloud.loadbalancer.extensions.support.favorite;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {AbstractFavoriteZoneSupportTest.FavoriteZoneApplication.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "spring.application.name=favorite-zone-test",
                "endpoints.enabled=false",
                "eureka.client.enabled=false",
                "eureka.instance.metadataMap.zone=zone1",
                "loadbalancer.extensions.propagation.executor.excludes[0]=taskScheduler",
                "loadbalancer.extensions.propagation.keys[0]=my-favorite-zone",
                "loadbalancer.extensions.propagation.keys[1]=upstream-zone",
                "loadbalancer.extensions.rule.favorite-zone.key=my-favorite-zone",
                "loadbalancer.extensions.rule.favorite-zone.fallback=zone",
                "spring.main.web-application-type=servlet",
                "spring.cloud.gateway.enabled=false",
                "spring.cloud.loadbalancer.cache.enabled=false"
        }
)
public class FavoriteZoneGlobalConfigurationTest extends AbstractFavoriteZoneSupportTest {

    public FavoriteZoneGlobalConfigurationTest() {
        super("favorite-zone");
    }
}