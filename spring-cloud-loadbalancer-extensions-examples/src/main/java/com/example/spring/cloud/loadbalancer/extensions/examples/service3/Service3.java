package com.example.spring.cloud.loadbalancer.extensions.examples.service3;

import com.example.spring.cloud.loadbalancer.extensions.examples.api.service2.Service2Resource;
import com.example.spring.cloud.loadbalancer.extensions.examples.loadbalancer.LoadBalancerClientsStrictMetadataMatcherConfig;
import com.example.spring.cloud.loadbalancer.extensions.support.EnableContextPropagation;
import com.example.spring.cloud.loadbalancer.extensions.support.EnableHttpLogging;
import com.example.spring.cloud.loadbalancer.extensions.support.EurekaInstanceProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

import static java.lang.String.format;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@SpringBootApplication
@EnableEurekaClient
@EnableContextPropagation
@LoadBalancerClients(defaultConfiguration = LoadBalancerClientsStrictMetadataMatcherConfig.class)
@EnableFeignClients(basePackageClasses = Service2Resource.class)
@EnableConfigurationProperties(EurekaInstanceProperties.class)
@RestController
@EnableHttpLogging
public class Service3 {
    @Inject
    protected EurekaInstanceProperties eurekaInstanceMetadataProperties;

    @Inject
    Service2Resource service2;

    @RequestMapping(method = GET, value = "/service3/message")
    @ResponseStatus(HttpStatus.OK)
    public String getMessage(@RequestParam(value = "useCase") String useCase) {
        return format("%s->%s", eurekaInstanceMetadataProperties.getInstanceId(), service2.getMessage(useCase));
    }

    public static void main(String[] args) {
        SpringApplication.run(Service3.class, args);
    }
}
