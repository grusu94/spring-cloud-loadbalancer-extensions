package com.github.grusu94.spring.cloud.loadbalancer.extensions.examples.service1;

import com.github.grusu94.spring.cloud.loadbalancer.extensions.examples.api.service2.Service2Resource;
import com.github.grusu94.spring.cloud.loadbalancer.extensions.examples.loadbalancer.LoadBalancerClientsFavoriteZoneConfig;
import com.github.grusu94.spring.cloud.loadbalancer.extensions.support.EnableContextPropagation;
import com.github.grusu94.spring.cloud.loadbalancer.extensions.support.EnableHttpLogging;
import com.github.grusu94.spring.cloud.loadbalancer.extensions.support.InstanceProperties;
import com.github.grusu94.spring.cloud.loadbalancer.extensions.support.InstancePropertiesConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

import static java.lang.String.format;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@SpringBootApplication
@Import(InstancePropertiesConfig.class)
@EnableEurekaClient
@EnableContextPropagation
@LoadBalancerClients(defaultConfiguration = LoadBalancerClientsFavoriteZoneConfig.class)
@EnableFeignClients(basePackageClasses = Service2Resource.class)
@RestController
@EnableHttpLogging
public class Service1 {
    @Inject
    protected InstanceProperties instanceMetadataProperties;

    @Inject
    Service2Resource service2;

    @RequestMapping(method = GET, value = "/service1/message")
    @ResponseStatus(HttpStatus.OK)
    public String getMessage(@RequestBody String useCase) {
        return format("%s->%s", instanceMetadataProperties.getInstanceId(), service2.getMessage(useCase));
    }

    public static void main(String[] args) {
        SpringApplication.run(Service1.class, args);
    }
}
