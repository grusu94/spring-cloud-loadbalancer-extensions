package com.example.load.balancer.extensions.examples.service2;

import com.example.load.balancer.extensions.examples.ribbon.RibbonClientsFavoriteZoneConfig;
import com.example.load.balancer.extensions.support.EnableContextPropagation;
import com.example.load.balancer.extensions.support.EnableHttpLogging;
import com.example.load.balancer.extensions.support.EurekaInstanceProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;


@SpringBootApplication
@EnableEurekaClient
@LoadBalancerClients(defaultConfiguration = {RibbonClientsFavoriteZoneConfig.class})
@EnableContextPropagation
@EnableConfigurationProperties(EurekaInstanceProperties.class)
@RestController
@EnableHttpLogging
public class Service2 {
    @Autowired
    protected EurekaInstanceProperties eurekaInstanceMetadataProperties;

    @RequestMapping(method = GET, value = "/service2/message")
    @ResponseStatus(HttpStatus.OK)
    public String getMessage(@RequestParam(value = "useCase") String useCase) {
        return eurekaInstanceMetadataProperties.getInstanceId();
    }

    public static void main(String[] args) {
        SpringApplication.run(Service2.class, args);
    }
}
