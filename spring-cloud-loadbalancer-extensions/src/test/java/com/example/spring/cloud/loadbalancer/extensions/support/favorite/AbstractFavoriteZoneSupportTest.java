package com.example.spring.cloud.loadbalancer.extensions.support.favorite;

import com.example.spring.cloud.loadbalancer.extensions.support.AbstractSupportTest;
import com.example.spring.cloud.loadbalancer.extensions.support.EnableContextPropagation;
import com.example.spring.cloud.loadbalancer.extensions.support.EnableHttpLogging;
import com.example.spring.cloud.loadbalancer.extensions.support.EnableLoadBalancerFavoriteZone;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpStatus.OK;

public abstract class AbstractFavoriteZoneSupportTest extends AbstractSupportTest {
    private final String favoriteZoneKey;

    public AbstractFavoriteZoneSupportTest(String favoriteZoneKey) {
        this.favoriteZoneKey = favoriteZoneKey;
    }

    @Test
    public void test_choose_server_in_zone1_using_favorite_zone_matcher() {
        given().when()
                .header(favoriteZoneKey, TestApplicationBase.TestControllerConstants.ZONE1)
                .get(TestApplicationBase.TestControllerConstants.MESSAGE_PATH)
                .then()
                .statusCode(OK.value())
                .body(equalTo(TestApplicationBase.TestControllerConstants.MESSAGE));
    }

    @Test
    public void test_choose_server_in_zone2_using_favorite_zone_matcher() {
        given().when()
                .header(favoriteZoneKey, TestApplicationBase.TestControllerConstants.ZONE2)
                .get(TestApplicationBase.TestControllerConstants.MESSAGE_PATH)
                .then()
                .statusCode(OK.value())
                .body(equalTo(TestApplicationBase.TestControllerConstants.MESSAGE));
    }


    @Test
    public void test_choose_server_in_zone1_using_zone_matcher() {
        given().when()
                .get(TestApplicationBase.TestControllerConstants.MESSAGE_PATH)
                .then()
                .statusCode(OK.value())
                .body(equalTo(TestApplicationBase.TestControllerConstants.MESSAGE));
    }

    @Test
    public void test_choose_any_server_even_if_unavailable() {
        server1.setAlive(false);
        server2.setAlive(false);
        given().when()
                .get(TestApplicationBase.TestControllerConstants.MESSAGE_PATH)
                .then()
                .statusCode(OK.value())
                .body(equalTo(TestApplicationBase.TestControllerConstants.MESSAGE));
    }

    @SpringBootApplication
    @EnableAsync
    @EnableFeignClients(basePackageClasses = TestApplicationResource.class)
    @EnableContextPropagation
    @LoadBalancerClients(defaultConfiguration = DefaultLoadBalancerClientsConfig.class,
            value = {@LoadBalancerClient(name = TestApplicationResource.SERVICE_ID, configuration = FavoriteZoneLoadBalancerClientsConfig.class)})
    @EnableHttpLogging
    public static class FavoriteZoneApplication extends TestApplicationBase {
        @Bean
        public ExecutorService executorService() {
            return Executors.newSingleThreadExecutor();
        }

        @Bean
        public TaskScheduler taskScheduler() {
            return new ThreadPoolTaskScheduler();
        }
    }

    @Configuration
    @EnableLoadBalancerFavoriteZone
    public static class FavoriteZoneLoadBalancerClientsConfig {
    }

}