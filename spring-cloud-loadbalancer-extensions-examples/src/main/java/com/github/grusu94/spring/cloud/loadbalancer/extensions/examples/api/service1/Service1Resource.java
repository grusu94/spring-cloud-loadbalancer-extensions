package com.github.grusu94.spring.cloud.loadbalancer.extensions.examples.api.service1;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

@FeignClient("service1")
@RequestMapping(value = "/service1")
public interface Service1Resource {
    @RequestMapping(value = "/message", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    String getMessage(@RequestBody String useCase);
}
