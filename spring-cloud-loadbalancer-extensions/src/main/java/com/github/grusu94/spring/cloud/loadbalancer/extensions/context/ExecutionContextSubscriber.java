package com.github.grusu94.spring.cloud.loadbalancer.extensions.context;

import org.reactivestreams.Subscription;
import org.springframework.lang.NonNull;
import reactor.core.CoreSubscriber;
import reactor.util.context.Context;

import java.util.Optional;

public class ExecutionContextSubscriber<T> implements CoreSubscriber<T> {

    private final CoreSubscriber<? super T> delegate;

    public ExecutionContextSubscriber(final CoreSubscriber<? super T> delegate) {
        this.delegate = delegate;
    }

    @Override
    @NonNull
    public Context currentContext() {
        return delegate.currentContext();
    }

    @Override
    public void onSubscribe(@NonNull Subscription subscription) {
        delegate.onSubscribe(subscription);
    }

    @Override
    public void onNext(T t) {
        execute(() -> delegate.onNext(t));
    }

    @Override
    public void onError(Throwable throwable) {
        execute(() -> delegate.onError(throwable));
    }

    @Override
    public void onComplete() {
        execute(delegate::onComplete);
    }

    private void execute(final Runnable runnable) {
        final Optional<ExecutionContext> executionContext = ReactorExecutionContext.get(currentContext());
        if (executionContext.isPresent()) {
            try (ExecutionContextScope ignored = ExecutionContextHolder.openScope(executionContext.get())) {
                runnable.run();
            }
        } else {
            runnable.run();
        }
    }
}
