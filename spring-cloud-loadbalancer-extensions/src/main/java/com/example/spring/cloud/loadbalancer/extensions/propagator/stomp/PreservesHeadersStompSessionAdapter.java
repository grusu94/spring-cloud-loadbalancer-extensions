package com.example.spring.cloud.loadbalancer.extensions.propagator.stomp;

import com.example.spring.cloud.loadbalancer.extensions.context.ExecutionContext;
import com.example.spring.cloud.loadbalancer.extensions.propagator.AbstractExecutionContextCopy;
import com.example.spring.cloud.loadbalancer.extensions.propagator.Filter;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Stomp session adapter that preserves the {@link ExecutionContext} by copying stomp headers
 * from/to the current {@link ExecutionContext} entry pre-filtering header names or entry keys using the provided.
 */
@Slf4j
public class PreservesHeadersStompSessionAdapter extends AbstractExecutionContextCopy<StompHeaders>
        implements StompSession {
    /**
     * the delegate {@link StompSession}.
     */
    private final StompSession delegate;

    /**
     * constructor.
     *
     * @param delegate           the delegate stomp session.
     * @param filter             the context entry key filter.
     * @param extraStaticEntries The extra static entries to copy.
     */
    public PreservesHeadersStompSessionAdapter(@NotNull StompSession delegate,
                                               @NotNull Filter<String> filter,
                                               @NotNull Map<String, String> extraStaticEntries) {
        super(filter, StompHeaders::set, extraStaticEntries);
        this.delegate = delegate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NonNull
    public String getSessionId() {
        return delegate.getSessionId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isConnected() {
        return delegate.isConnected();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAutoReceipt(boolean enabled) {
        delegate.setAutoReceipt(enabled);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NonNull
    public Receiptable send(@NonNull String destination, @NonNull Object payload) {
        StompHeaders headers = new StompHeaders();
        headers.setDestination(destination);
        return send(headers, payload);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NonNull
    public Receiptable send(@NonNull StompHeaders headers, @NonNull Object payload) {
        Set<Entry<String, String>> entries = copy(headers);
        log.trace("Execution context copied to stomp headers: {}.", entries);
        return delegate.send(headers, payload);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NonNull
    public Subscription subscribe(@NonNull String destination, @NonNull StompFrameHandler handler) {
        return delegate.subscribe(destination, new PreservesHeadersStompFrameHandlerAdapter(handler, getFilter()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NonNull
    public Subscription subscribe(@NonNull StompHeaders headers, @NonNull StompFrameHandler handler) {
        return delegate.subscribe(headers, new PreservesHeadersStompFrameHandlerAdapter(handler, getFilter()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NonNull
    public Receiptable acknowledge(@NonNull String messageId, boolean consumed) {
        return delegate.acknowledge(messageId, consumed);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NonNull
    public Receiptable acknowledge(@NonNull StompHeaders headers, boolean consumed) {
        return delegate.acknowledge(headers,consumed);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void disconnect() {
        delegate.disconnect();
    }

    @Override
    public void disconnect(@NonNull StompHeaders headers) {
        delegate.disconnect(headers);
    }
}
