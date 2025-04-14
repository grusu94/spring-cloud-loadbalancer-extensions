package com.example.load.balancer.extensions.rule;

import com.example.load.balancer.extensions.matcher.LoadBalancingStrategy;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.*;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import reactor.core.publisher.Mono;

import java.util.List;

public abstract class AbstractLoadBalancer implements ReactorServiceInstanceLoadBalancer {

    private final ServiceInstanceListSupplier supplier;

    public AbstractLoadBalancer(ServiceInstanceListSupplier supplier) {
        this.supplier = supplier;
    }

    @Override
    public Mono<Response<ServiceInstance>> choose(Request request) {
        // already round-robin ordered if supplier is reactive-aware
        return supplier.get().next().map(allInstances -> {
            List<ServiceInstance> filtered = applyStrategies(allInstances);
            if (filtered.isEmpty()) {
                return new EmptyResponse();
            }

            return new DefaultResponse(filtered.get(0));
        });
    }

    private List<ServiceInstance> applyStrategies(List<ServiceInstance> instances) {
        for(LoadBalancingStrategy strategy : getStrategies()) {
            List<ServiceInstance> instanceList = strategy.apply(instances);
            if (!instanceList.isEmpty()) {
                return instanceList;
            }
        }
        return instances;
    }

    protected abstract List<LoadBalancingStrategy> getStrategies();
}
