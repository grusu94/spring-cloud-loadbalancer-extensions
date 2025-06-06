package com.example.spring.cloud.loadbalancer.extensions.examples.api.service2;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

@FeignClient("service2")
@RequestMapping(value = "/service2")
public interface Service2Resource {
    @RequestMapping(value = "/message", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    String getMessage(@RequestParam(value = "useCase") String useCase);
}
