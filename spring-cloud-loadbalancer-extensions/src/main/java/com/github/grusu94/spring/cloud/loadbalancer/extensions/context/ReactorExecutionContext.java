package com.github.grusu94.spring.cloud.loadbalancer.extensions.context;

import reactor.util.context.Context;
import reactor.util.context.ContextView;

import java.util.Optional;

public final class ReactorExecutionContext {

    public static final Class<ExecutionContext> REACTOR_CONTEXT_KEY = ExecutionContext.class;

    private ReactorExecutionContext() {
    }

    public static Context put(Context context, ExecutionContext executionContext) {
        return context.put(REACTOR_CONTEXT_KEY, executionContext);
    }

    public static Optional<ExecutionContext> get(ContextView contextView) {
        return contextView.getOrEmpty(REACTOR_CONTEXT_KEY);
    }
}
