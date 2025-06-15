package com.github.grusu94.spring.cloud.loadbalancer.extensions.support;

import io.restassured.RestAssured;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static java.lang.String.format;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@ExtendWith(SpringExtension.class)
public abstract class AbstractSupportTest {

    @LocalServerPort
    int port;

    @Inject
    @Named("server1")
    protected CustomServiceInstance server1;
    @Inject
    @Named("server2")
    protected CustomServiceInstance server2;

    static {
        RestAssured.baseURI = "http://localhost";
        RestAssured.basePath = TestApplicationBase.TestControllerConstants.BASE_PATH;
    }

    @BeforeEach
    public void before() {
        RestAssured.port = port;
        server1.setPort(port);
        server2.setPort(port);
    }

    public void reset(CustomServiceInstance server) {
        server.getMetadata().clear();
    }

    @AfterEach
    public void after() {
        reset(server1);
        reset(server2);
    }

    @Test
    public void test_default() {
        given().when()
                .get(TestApplicationBase.TestControllerConstants.MESSAGE_PATH)
                .then()
                .statusCode(OK.value())
                .body(equalTo(TestApplicationBase.TestControllerConstants.MESSAGE));
    }

    @TestConfiguration
    public static class TestApplicationBase {

        public interface TestControllerConstants {
            String BASE_PATH = "application";
            String HELLO_PATH = "hello";
            String HELLO_MESSAGE = "Hello";
            String WORLD_MESSAGE = "World!";
            String WORLD_PATH = "world";
            String WORLD_FULL_PATH = BASE_PATH + "/" + WORLD_PATH;
            String MESSAGE_PATH = "message";
            String MESSAGE = HELLO_MESSAGE + " " + WORLD_MESSAGE;
            String ZONE1 = "zone1";
            String ZONE2 = "zone2";
            String INSTANCE_ID = "instance-id";
            String INSTANCE_ID1 = "InstanceId1";
            String INSTANCE_ID2 = "InstanceId2";
        }

        public CustomServiceInstance createServer(String zone) {
            final Map<String, String> metadata = new HashMap<>();
            metadata.put("zone", zone);

            return new CustomServiceInstance(
                    TestApplicationResource.SERVICE_ID,
                    TestApplicationResource.SERVICE_ID,
                    "127.0.0.1",
                    7001,
                    false,
                    metadata,
                    true
            );
        }

        @Bean("server1")
        public CustomServiceInstance server1() {
            return createServer(TestControllerConstants.ZONE1);
        }

        @Bean("server2")
        public CustomServiceInstance server2() {
            return createServer(TestControllerConstants.ZONE2);
        }

        @Autowired
        private ApplicationContext context;

        @Bean
        public ReactiveDiscoveryClient testDiscoveryClient() {
            return new ReactiveDiscoveryClient() {
                @Override
                public String description() {
                    return "Fake test discovery client";
                }

                @Override
                public Flux<ServiceInstance> getInstances(String serviceId) {
                    if (!TestApplicationResource.SERVICE_ID.equals(serviceId)) {
                        return Flux.empty();
                    }

                    final List<CustomServiceInstance> instanceList =
                            Stream.of("server1", "server2")
                                    .map(name -> context.getBean(name, CustomServiceInstance.class))
                                    .filter(CustomServiceInstance::isAlive)
                                    .toList();
                    /*
                     * Logic for some test cases where it is supposed to have all the instances DOWN and still have a fallback.
                     * In real scenario don't think it is possible.
                     */
                    final Optional<CustomServiceInstance> serverFallback =
                            Optional.of("server1")
                                    .map(name -> context.getBean(name, CustomServiceInstance.class));

                    if (instanceList.isEmpty()) {
                        return Flux.just(serverFallback.get());
                    }

                    return Flux.just(instanceList.toArray(CustomServiceInstance[]::new));
                }

                @Override
                public Flux<String> getServices() {
                    return Flux.just(TestApplicationResource.SERVICE_ID);
                }
            };
        }

        @RestController
        @RequestMapping(TestControllerConstants.BASE_PATH)
        @Slf4j
        public static class TestController {
            @Inject
            private TestApplicationResource testApplicationResource;

            @Inject
            private ExecutorService executorService;

            @RequestMapping(method = GET, value = TestControllerConstants.MESSAGE_PATH)
            public String getMessage() {
                try {
                    return format("%s %s", TestControllerConstants.HELLO_MESSAGE, executorService.submit(() -> testApplicationResource.getWorldMessage()).get());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            @RequestMapping(method = GET, value = TestControllerConstants.HELLO_PATH)
            public String getHelloMessage() {
                return TestControllerConstants.HELLO_MESSAGE;
            }

            @RequestMapping(method = GET, value = TestControllerConstants.WORLD_PATH)
            public String getWorldMessage() {
                return TestControllerConstants.WORLD_MESSAGE;
            }
        }
    }

    @FeignClient(TestApplicationResource.SERVICE_ID)
    public interface TestApplicationResource {
        String SERVICE_ID = "application";

        @RequestMapping(value = TestApplicationBase.TestControllerConstants.WORLD_FULL_PATH, method = RequestMethod.GET)
        @ResponseStatus(HttpStatus.OK)
        String getWorldMessage();
    }

    @Configuration
    public static class DefaultLoadBalancerClientsConfig {
    }
}