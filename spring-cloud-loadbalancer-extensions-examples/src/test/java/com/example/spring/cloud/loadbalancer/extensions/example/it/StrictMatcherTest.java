package com.example.spring.cloud.loadbalancer.extensions.example.it;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;

public class StrictMatcherTest extends AbstractTest {
    public StrictMatcherTest() {
        super("service3", "service3-zone1");
    }

    @Test
    public void should_choose_any() {
        given().log().uri().log().headers()
                .param("useCase", "should_choose_any")
                .when()
                .get("message")
                .then()
                .statusCode(OK.value())
                .body(startsWith("service3-zone1->service2-zone"));
    }

    @Test
    public void should_choose_any_concurrent() {
        parallelRun(this::should_choose_any);
    }

    @Test
    public void should_choose_service2_zone1() {
        given().log().uri().log().headers()
                .header("version", "1.0.0")
                .param("useCase", "should_choose_service2_zone1")
                .when()
                .get("message")
                .then()
                .statusCode(OK.value())
                .body(is("service3-zone1->service2-zone1"));
    }

    @Test
    public void should_choose_service2_zone1_concurrent() {
        parallelRun(this::should_choose_service2_zone1);
    }

    @Test
    public void should_choose_service2_zone2() {
        given().log().uri().log().headers()
                .header("version", "2.0.0")
                .param("useCase", "should_choose_service2_zone2")
                .when()
                .get("message")
                .then()
                .statusCode(OK.value())
                .body(is("service3-zone1->service2-zone2"));
    }

    @Test
    public void should_choose_service2_zone2_concurrent() {
        parallelRun(this::should_choose_service2_zone2);
    }

    @Test
    public void should_fail_no_available_server() {
        given().log().uri().log().headers()
                .header("version", "9.9.9")
                .param("useCase", "should_fail_no_available_server")
                .when()
                .get("message")
                .then()
                .statusCode(INTERNAL_SERVER_ERROR.value());
    }

    @Test
    public void should_fail_no_available_concurrent() {
        parallelRun(this::should_fail_no_available_server);
    }
}