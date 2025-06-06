package com.example.spring.cloud.loadbalancer.extensions;

import lombok.Getter;
import org.mockito.ArgumentMatcher;

import java.io.Serializable;

import static org.mockito.internal.progress.ThreadSafeMockingProgress.mockingProgress;

@Getter
public class ArgumentHolder<T> implements ArgumentMatcher<T>, Serializable {

    T argument;

    @Override
    public boolean matches(T argument) {
        this.argument = argument;
        return true;
    }

    public T eq() {
        mockingProgress().getArgumentMatcherStorage().reportMatcher(this);
        return argument;
    }
}
