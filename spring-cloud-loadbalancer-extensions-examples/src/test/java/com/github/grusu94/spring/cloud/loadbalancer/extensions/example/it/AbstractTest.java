package com.github.grusu94.spring.cloud.loadbalancer.extensions.example.it;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class AbstractTest {
    protected int maxConcurrentTasks = 10;
    protected ExecutorService executorService = Executors.newFixedThreadPool(maxConcurrentTasks * 2);
    private final String basePath;
    private final String applicationName;
    protected boolean parrallelRunEnabled = true;

    public AbstractTest(String basePath, String applicationName) {
        this.basePath = basePath;
        this.applicationName = applicationName;
    }

    @BeforeEach
    public final void abstractBefore() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.basePath = basePath;
        RestAssured.port = getApplicationPort(applicationName);
    }

    private int getApplicationPort(String name) {
        return switch (name) {
            case "eureka" -> 8000;
            case "consul" -> 8500;
            case "gateway" -> 8001;
            case "service1-zone1" -> 8011;
            case "service1-zone2" -> 8012;
            case "service1-developer" -> 8019;
            case "service2-zone1" -> 8021;
            case "service2-zone2" -> 8022;
            case "service3-zone1" -> 8031;
            default -> throw new IllegalArgumentException(name);
        };
    }

    protected void parallelRun(Runnable r) {
        if (parrallelRunEnabled) {
            AtomicInteger errors = new AtomicInteger();
            List<Future<?>> futures = new ArrayList<>(maxConcurrentTasks);
            for (int i = 0; i < maxConcurrentTasks; i++) {
                futures.add(
                        executorService.submit(() -> {
                            try {
                                r.run();
                            } catch (AssertionError e) {
                                errors.incrementAndGet();
                            }
                        }));
            }
            futures.forEach(x -> {
                try {
                    x.get();
                } catch (Exception e) {
                    errors.incrementAndGet();
                }
            });
            if (errors.get() > 0) {
                throw new IllegalStateException();
            }
        }
    }

    @Test()
    public void parallelRunTest() {
        assertThrows(IllegalStateException.class,
                () -> {
                    parallelRun(Assertions::fail);
                    if (!parrallelRunEnabled) {
                        throw new IllegalStateException();
                    }
                });
    }
}
