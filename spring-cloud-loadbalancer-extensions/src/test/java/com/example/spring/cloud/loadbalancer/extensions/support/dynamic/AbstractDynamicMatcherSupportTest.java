package com.example.spring.cloud.loadbalancer.extensions.support.dynamic;

import com.example.spring.cloud.loadbalancer.extensions.support.AbstractSupportTest;
import com.example.spring.cloud.loadbalancer.extensions.support.EnableContextPropagation;
import com.example.spring.cloud.loadbalancer.extensions.support.EnableHttpLogging;
import com.example.spring.cloud.loadbalancer.extensions.support.EnableLoadBalancerDynamicMetadataMatcher;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;

public abstract class AbstractDynamicMatcherSupportTest extends AbstractSupportTest {
    private final String dynamicAttributeKey;
    private final boolean matchIfMissing;

    public AbstractDynamicMatcherSupportTest(String dynamicAttributeKey, boolean matchIfMissing) {
        this.dynamicAttributeKey = dynamicAttributeKey;
        this.matchIfMissing = matchIfMissing;
    }

    @Test
    @Disabled("will fail")
    @Override
    public void test_default() {
    }

    @Test
    public void test_choose_server_instanceid1() {
        server1.getMetadata().put(TestApplicationBase.TestControllerConstants.INSTANCE_ID, TestApplicationBase.TestControllerConstants.INSTANCE_ID1);
        server2.getMetadata().put(TestApplicationBase.TestControllerConstants.INSTANCE_ID, TestApplicationBase.TestControllerConstants.INSTANCE_ID2);
        given().when()
                .header(dynamicAttributeKey, TestApplicationBase.TestControllerConstants.INSTANCE_ID)
                .header(TestApplicationBase.TestControllerConstants.INSTANCE_ID, TestApplicationBase.TestControllerConstants.INSTANCE_ID1)
                .get(TestApplicationBase.TestControllerConstants.MESSAGE_PATH)
                .then()
                .statusCode(OK.value())
                .body(equalTo(TestApplicationBase.TestControllerConstants.MESSAGE));
    }

    @Test
    public void test_choose_server_instanceid2() {
        server1.getMetadata().put(TestApplicationBase.TestControllerConstants.INSTANCE_ID, TestApplicationBase.TestControllerConstants.INSTANCE_ID1);
        server2.getMetadata().put(TestApplicationBase.TestControllerConstants.INSTANCE_ID, TestApplicationBase.TestControllerConstants.INSTANCE_ID2);
        given().when()
                .header(dynamicAttributeKey, TestApplicationBase.TestControllerConstants.INSTANCE_ID)
                .header(TestApplicationBase.TestControllerConstants.INSTANCE_ID, TestApplicationBase.TestControllerConstants.INSTANCE_ID2)
                .get(TestApplicationBase.TestControllerConstants.MESSAGE_PATH)
                .then()
                .statusCode(OK.value())
                .body(equalTo(TestApplicationBase.TestControllerConstants.MESSAGE));
    }

    @Test
    public void test_can_not_choose_any_server() {
        given().when()
                .header(dynamicAttributeKey, TestApplicationBase.TestControllerConstants.INSTANCE_ID)
                .header(TestApplicationBase.TestControllerConstants.INSTANCE_ID, TestApplicationBase.TestControllerConstants.INSTANCE_ID1)
                .get(TestApplicationBase.TestControllerConstants.MESSAGE_PATH)
                .then()
                .statusCode(INTERNAL_SERVER_ERROR.value());
    }

    @SpringBootApplication
    @EnableAsync
    @EnableFeignClients(basePackageClasses = TestApplicationResource.class)
    @EnableContextPropagation
    @LoadBalancerClients(defaultConfiguration = DynamicMatcherClientsConfig.class)
    @EnableHttpLogging
    public static class DynamicMatcherApplication extends TestApplicationBase {
        @Bean
        public ExecutorService executorService() {
            return Executors.newSingleThreadExecutor();
        }
    }

    @Configuration
    @EnableLoadBalancerDynamicMetadataMatcher
    public static class DynamicMatcherClientsConfig {
    }

}