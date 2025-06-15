package com.github.grusu94.spring.cloud.loadbalancer.extensions.examples.gateway;

import com.github.grusu94.spring.cloud.loadbalancer.extensions.examples.api.service1.Service1Resource;
import com.github.grusu94.spring.cloud.loadbalancer.extensions.examples.api.service2.Service2Resource;
import com.github.grusu94.spring.cloud.loadbalancer.extensions.examples.loadbalancer.LoadBalancerClientsFavoriteZoneConfig;
import com.github.grusu94.spring.cloud.loadbalancer.extensions.support.EnableContextPropagation;
import com.github.grusu94.spring.cloud.loadbalancer.extensions.support.EnableHttpLogging;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient
@EnableContextPropagation
@EnableFeignClients(basePackageClasses = {Service1Resource.class, Service2Resource.class})
@LoadBalancerClients(defaultConfiguration = LoadBalancerClientsFavoriteZoneConfig.class)
@EnableHttpLogging
@Slf4j
public class GatewayServer {

    public static void main(String[] args) {
        SpringApplication.run(GatewayServer.class, "--spring.config.name=gateway");
    }
}
