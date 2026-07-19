package com.github.grusu94.spring.cloud.loadbalancer.extensions.support.gateway;

import com.ecwid.consul.v1.ConsulClient;
import com.github.grusu94.spring.cloud.loadbalancer.extensions.support.EnableContextPropagation;
import com.github.grusu94.spring.cloud.loadbalancer.extensions.support.EnableHttpLogging;
import com.github.grusu94.spring.cloud.loadbalancer.extensions.support.PropagationProperties;
import feign.RequestInterceptor;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.inject.Inject;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {ContextPropagationGatewayConsulTest.Application.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "eureka.client.enabled=false",
                "spring.cloud.consul.enabled=true",
                "spring.cloud.consul.discovery.catalog-services-watch.enabled=false",
                "spring.cloud.consul.discovery.instance-zone=zone1",
                "spring.main.web-application-type=servlet",
                "spring.cloud.gateway.enabled=false",
        }
)
@EnableHttpLogging
public class ContextPropagationGatewayConsulTest {

    @MockBean
    private ConsulClient consulClient;

    @Inject
    RequestInterceptor requestInterceptor;
    @Inject
    PropagationProperties properties;

    @SpringBootApplication
    @EnableContextPropagation
    public static class Application {
        @RequestMapping(method = GET)
        public String getMessage() {
            return "Message";
        }
    }
}