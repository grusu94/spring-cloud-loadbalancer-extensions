package com.example.load.balancer.extensions.examples.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
@EnableDiscoveryClient
public class EurekaServer {
    public static void main(String[] args) {
        SpringApplication.run(EurekaServer.class, "--spring.config.name=eureka");
    }
}
