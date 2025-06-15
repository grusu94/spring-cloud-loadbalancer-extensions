package com.github.grusu94.spring.cloud.loadbalancer.extensions.support.dynamic;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {AbstractDynamicMatcherSupportTest.DynamicMatcherApplication.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "spring.application.name=dynamic-matcher-test",
                "endpoints.enabled=false",
                "eureka.client.enabled=false",
                "loadbalancer.extensions.propagation.keys[0]=dynamic-matcher-key",
                "loadbalancer.extensions.propagation.keys[1]=instance-id",
                "spring.main.web-application-type=servlet",
                "spring.cloud.gateway.enabled=false",
                "spring.cloud.loadbalancer.cache.enabled=false"
        }
)
public class DynamicMatcherDefaultConfigurationTest extends AbstractDynamicMatcherSupportTest {
    public DynamicMatcherDefaultConfigurationTest() {
        super("dynamic-matcher-key", true);
    }

}