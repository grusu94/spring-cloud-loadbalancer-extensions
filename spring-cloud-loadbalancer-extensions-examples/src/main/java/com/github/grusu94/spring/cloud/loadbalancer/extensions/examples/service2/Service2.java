package com.github.grusu94.spring.cloud.loadbalancer.extensions.examples.service2;

import com.github.grusu94.spring.cloud.loadbalancer.extensions.examples.loadbalancer.LoadBalancerClientsFavoriteZoneConfig;
import com.github.grusu94.spring.cloud.loadbalancer.extensions.support.EnableContextPropagation;
import com.github.grusu94.spring.cloud.loadbalancer.extensions.support.EnableHttpLogging;
import com.github.grusu94.spring.cloud.loadbalancer.extensions.support.EurekaInstanceProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@SpringBootApplication
@LoadBalancerClients(defaultConfiguration = {LoadBalancerClientsFavoriteZoneConfig.class})
@EnableContextPropagation
@EnableConfigurationProperties(EurekaInstanceProperties.class)
@RestController
@EnableHttpLogging
public class Service2 {

    protected EurekaInstanceProperties eurekaInstanceMetadataProperties;

    public Service2(EurekaInstanceProperties eurekaInstanceMetadataProperties) {
        this.eurekaInstanceMetadataProperties = eurekaInstanceMetadataProperties;
    }

    @RequestMapping(method = GET, value = "/service2/message")
    @ResponseStatus(HttpStatus.OK)
    public String getMessage() {
        return eurekaInstanceMetadataProperties.getInstanceId();
    }

    public static void main(String[] args) {
        SpringApplication.run(Service2.class, args);
    }
}
