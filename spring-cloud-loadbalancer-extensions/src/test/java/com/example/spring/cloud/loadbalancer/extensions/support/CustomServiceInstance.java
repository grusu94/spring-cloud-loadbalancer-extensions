package com.example.spring.cloud.loadbalancer.extensions.support;

import lombok.Getter;
import lombok.Setter;
import org.springframework.cloud.client.DefaultServiceInstance;

import java.util.Map;

@Getter
@Setter
public class CustomServiceInstance extends DefaultServiceInstance {

    private boolean alive;

    public CustomServiceInstance(String instanceId, String serviceId, String host, int port, boolean secure, Map<String, String> metadata, boolean alive) {
        super(instanceId, serviceId, host, port, secure, metadata);
        this.alive = alive;
    }
}
