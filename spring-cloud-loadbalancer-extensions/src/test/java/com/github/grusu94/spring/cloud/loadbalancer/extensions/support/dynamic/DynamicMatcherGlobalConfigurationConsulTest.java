package com.github.grusu94.spring.cloud.loadbalancer.extensions.support.dynamic;

import com.ecwid.consul.v1.ConsulClient;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {AbstractDynamicMatcherSupportTest.DynamicMatcherApplication.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "spring.application.name=dynamic-matcher-test",
                "endpoints.enabled=false",
                "eureka.client.enabled=false",
                "spring.cloud.consul.enabled=true",
                "spring.cloud.consul.discovery.catalog-services-watch.enabled=false",
                "spring.cloud.consul.discovery.instance-zone=zone1",
                "loadbalancer.extensions.propagation.keys[0]=instance-id",
                "loadbalancer.extensions.propagation.keys[1]=mykey",
                "loadbalancer.extensions.rule.dynamic-metadata-matcher.key=mykey",
                "loadbalancer.extensions.rule.dynamic-metadata-matcher.matchIfMissing=false",
                "spring.main.web-application-type=servlet",
                "spring.cloud.gateway.enabled=false",
                "spring.cloud.loadbalancer.cache.enabled=false"
        }
)
public class DynamicMatcherGlobalConfigurationConsulTest extends AbstractDynamicMatcherSupportTest {

    @MockBean
    private ConsulClient consulClient;

    public DynamicMatcherGlobalConfigurationConsulTest() {
        super("mykey", false);
    }

}