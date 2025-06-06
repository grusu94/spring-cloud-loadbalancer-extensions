package com.example.spring.cloud.loadbalancer.extensions.matcher;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import reactor.core.publisher.Flux;

import java.util.List;

public class ServiceInstanceListSupplierMatcher implements ServiceInstanceListSupplier {

    private final ServiceInstanceListSupplier delegate;
    private final LoadBalancingStrategyMatcher matcher;


    public ServiceInstanceListSupplierMatcher(ServiceInstanceListSupplier delegate, LoadBalancingStrategyMatcher matcher) {
        this.delegate = delegate;
        this.matcher = matcher;
    }

    @Override
    public String getServiceId() {
        return delegate.getServiceId();
    }

    @Override
    public Flux<List<ServiceInstance>> get(Request request) {
        return delegate.get(request).map(matcher::apply);
    }

    @Override
    public Flux<List<ServiceInstance>> get() {
        return delegate.get().map(matcher::apply);
    }
}
