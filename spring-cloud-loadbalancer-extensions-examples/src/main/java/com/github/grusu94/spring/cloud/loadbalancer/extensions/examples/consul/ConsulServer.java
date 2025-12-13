package com.github.grusu94.spring.cloud.loadbalancer.extensions.examples.consul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
public class ConsulServer {
    public static void main(String[] args) {
        SpringApplication.run(ConsulServer.class, "--spring.config.name=consul");
    }
}
