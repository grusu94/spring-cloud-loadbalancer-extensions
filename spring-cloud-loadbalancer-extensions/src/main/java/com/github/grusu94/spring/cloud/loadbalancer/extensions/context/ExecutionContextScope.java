package com.github.grusu94.spring.cloud.loadbalancer.extensions.context;

public interface ExecutionContextScope extends AutoCloseable {

    @Override
    void close();

}
