package com.github.grusu94.spring.cloud.loadbalancer.extensions.support.affinity;

import com.github.grusu94.spring.cloud.loadbalancer.extensions.propagator.concurrent.ContextAwareExecutorService;
import com.github.grusu94.spring.cloud.loadbalancer.extensions.support.AbstractSupportTest;
import com.github.grusu94.spring.cloud.loadbalancer.extensions.support.EnableContextPropagation;
import com.github.grusu94.spring.cloud.loadbalancer.extensions.support.EnableHttpLogging;
import com.github.grusu94.spring.cloud.loadbalancer.extensions.support.EnableLoadBalancerZoneAffinity;
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
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {ZoneAffinityTest.ZoneAffinityApplication.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "spring.application.name=zone-affinity-test",
                "endpoints.enabled=false",
                "eureka.client.enabled=false",
                "eureka.instance.metadataMap.zone=zone1",
                "spring.main.web-application-type=servlet",
                "spring.cloud.gateway.enabled=false",
                "spring.cloud.loadbalancer.cache.enabled=false"
        }
)
public class ZoneAffinityTest extends AbstractSupportTest {

    @Test
    public void test_choose_server1() {
        given().when()
                .get(TestApplicationBase.TestControllerConstants.MESSAGE_PATH)
                .then()
                .statusCode(OK.value())
                .body(equalTo(TestApplicationBase.TestControllerConstants.MESSAGE));
    }

    @Test
    public void test_choose_server2() {
        server1.setAlive(false);
        given().when()
                .get(AbstractSupportTest.TestApplicationBase.TestControllerConstants.MESSAGE_PATH)
                .then()
                .statusCode(OK.value())
                .body(equalTo(TestApplicationBase.TestControllerConstants.MESSAGE));
    }

    @SpringBootApplication
    @EnableFeignClients(basePackageClasses = TestApplicationResource.class)
    @EnableContextPropagation
    @LoadBalancerClients(defaultConfiguration = DefaultLoadBalancerClientsConfig.class,
            value = {@LoadBalancerClient(name = TestApplicationResource.SERVICE_ID, configuration = ZoneAffinityLoadBalancerClientsConfig.class)})
    @EnableHttpLogging
    public static class ZoneAffinityApplication extends TestApplicationBase {
        @Bean
        public ExecutorService executorService() {
            return new ContextAwareExecutorService(Executors.newSingleThreadExecutor());
        }
    }

    @Configuration
    @EnableLoadBalancerZoneAffinity
    public static class ZoneAffinityLoadBalancerClientsConfig {
    }

}