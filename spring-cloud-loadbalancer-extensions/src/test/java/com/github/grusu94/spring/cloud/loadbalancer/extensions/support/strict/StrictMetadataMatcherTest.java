package com.github.grusu94.spring.cloud.loadbalancer.extensions.support.strict;

import com.github.grusu94.spring.cloud.loadbalancer.extensions.propagator.concurrent.ContextAwareExecutorService;
import com.github.grusu94.spring.cloud.loadbalancer.extensions.support.AbstractSupportTest;
import com.github.grusu94.spring.cloud.loadbalancer.extensions.support.EnableContextPropagation;
import com.github.grusu94.spring.cloud.loadbalancer.extensions.support.EnableLoadBalancerStrictMetadataMatcher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {StrictMetadataMatcherTest.StrictMatcherApplication.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "spring.application.name=strict-meta-data-matcher-test",
                "endpoints.enabled=false",
                "eureka.client.enabled=false",
                "spring.cloud.consul.enabled=false",
                "eureka.instance.metadataMap.zone=zone1",
                "loadbalancer.extensions.propagation.keys[0]=key1",
                "spring.main.web-application-type=servlet",
                "spring.cloud.gateway.enabled=false",
                "spring.cloud.loadbalancer.cache.enabled=false"
        }
)
public class StrictMetadataMatcherTest extends AbstractSupportTest {

    @Test
    public void test_choose_server1() {
        server1.getMetadata().put("key1", "value1");
        given().when()
                .header("key1", "value1")
                .get(TestApplicationBase.TestControllerConstants.MESSAGE_PATH)
                .then()
                .statusCode(OK.value())
                .body(equalTo(TestApplicationBase.TestControllerConstants.MESSAGE));
    }

    @Test
    public void test_choose_server2() {
        server2.getMetadata().put("key1", "value1");
        given().when()
                .header("key1", "value1")
                .get(TestApplicationBase.TestControllerConstants.MESSAGE_PATH)
                .then()
                .statusCode(OK.value())
                .body(equalTo(TestApplicationBase.TestControllerConstants.MESSAGE));
    }

    @Test
    public void test_can_not_choose_any_server() {
        given().when()
                .header("key1", "value1")
                .get(TestApplicationBase.TestControllerConstants.MESSAGE_PATH)
                .then()
                .statusCode(INTERNAL_SERVER_ERROR.value());
    }

    @SpringBootApplication
    @EnableFeignClients(basePackageClasses = TestApplicationResource.class)
    @EnableContextPropagation
    @LoadBalancerClients(defaultConfiguration = DefaultLoadBalancerClientsConfig.class,
            value = {@LoadBalancerClient(name = TestApplicationResource.SERVICE_ID, configuration = StrictMatcherLoadBalancerClientsConfig.class)})
    public static class StrictMatcherApplication extends TestApplicationBase {
        @Bean
        public ExecutorService executorService() {
            return new ContextAwareExecutorService(Executors.newSingleThreadExecutor());
        }
    }

    @Configuration
    @EnableLoadBalancerStrictMetadataMatcher
    public static class StrictMatcherLoadBalancerClientsConfig {
    }

}