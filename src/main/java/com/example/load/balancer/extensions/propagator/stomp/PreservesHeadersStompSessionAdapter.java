package com.example.load.balancer.extensions.propagator.stomp;

import com.example.load.balancer.extensions.context.ExecutionContext;
import com.example.load.balancer.extensions.propagator.AbstractExecutionContextCopy;
import com.example.load.balancer.extensions.propagator.Filter;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Stomp session adapter that preserves the {@link ExecutionContext} by copying stomp headers from/to the current {@link ExecutionContext} entry pre-filtering header names or entry keys using the provided.
 */
@Slf4j
public class PreservesHeadersStompSessionAdapter extends AbstractExecutionContextCopy<StompHeaders> implements StompSession {
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
    public Receiptable send(String destination, Object payload) {
        StompHeaders headers = new StompHeaders();
        headers.setDestination(destination);
        return send(headers, payload);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Receiptable send(StompHeaders headers, Object payload) {
        Set<Entry<String, String>> entries = copy(headers);
        log.trace("Execution context copied to stomp headers: {}.", entries);
        return delegate.send(headers, payload);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Subscription subscribe(String destination, StompFrameHandler handler) {
        return delegate.subscribe(destination, new PreservesHeadersStompFrameHandlerAdapter(handler, getFilter()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Subscription subscribe(StompHeaders headers, StompFrameHandler handler) {
        return delegate.subscribe(headers, new PreservesHeadersStompFrameHandlerAdapter(handler, getFilter()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Receiptable acknowledge(String messageId, boolean consumed) {
        return delegate.acknowledge(messageId, consumed);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Receiptable acknowledge(StompHeaders headers, boolean consumed) {
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
    public void disconnect(StompHeaders headers) {
        delegate.disconnect(headers);
    }
}
