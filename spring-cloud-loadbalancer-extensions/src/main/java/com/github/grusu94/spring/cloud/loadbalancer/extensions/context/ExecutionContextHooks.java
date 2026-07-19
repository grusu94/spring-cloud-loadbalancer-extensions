package com.github.grusu94.spring.cloud.loadbalancer.extensions.context;

import reactor.core.publisher.Hooks;
import reactor.core.publisher.Operators;

public final class ExecutionContextHooks {

    public static final String HOOK_NAME = "execution-context";

    public static void register() {
        Hooks.onEachOperator(HOOK_NAME,
                Operators.lift((scannable, subscriber) -> new ExecutionContextSubscriber<>(subscriber)));
    }

    public static void unregister() {
        Hooks.resetOnEachOperator(HOOK_NAME);
    }
}
