package com.github.grusu94.spring.cloud.loadbalancer.extensions.examples.service2;

import com.github.grusu94.spring.cloud.loadbalancer.extensions.examples.loadbalancer.LoadBalancerClientsFavoriteZoneConfig;
import com.github.grusu94.spring.cloud.loadbalancer.extensions.support.EnableContextPropagation;
import com.github.grusu94.spring.cloud.loadbalancer.extensions.support.EnableHttpLogging;
import com.github.grusu94.spring.cloud.loadbalancer.extensions.support.InstanceProperties;
import com.github.grusu94.spring.cloud.loadbalancer.extensions.support.InstancePropertiesConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@SpringBootApplication
@EnableEurekaClient
@LoadBalancerClients(defaultConfiguration = {LoadBalancerClientsFavoriteZoneConfig.class})
@EnableContextPropagation
@Import(InstancePropertiesConfig.class)
@RestController
@EnableHttpLogging
public class Service2 {

    protected InstanceProperties instanceMetadataProperties;

    public Service2(InstanceProperties instanceMetadataProperties) {
        this.instanceMetadataProperties = instanceMetadataProperties;
    }

    @RequestMapping(method = GET, value = "/service2/message")
    @ResponseStatus(HttpStatus.OK)
    public String getMessage() {
        return instanceMetadataProperties.getInstanceId();
    }

    public static void main(String[] args) {
        SpringApplication.run(Service2.class, args);
    }
}
