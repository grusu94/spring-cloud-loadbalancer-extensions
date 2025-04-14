package com.example.load.balancer.extensions.propagator.stomp;

import com.example.load.balancer.extensions.context.ExecutionContext;
import com.example.load.balancer.extensions.propagator.Filter;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import static com.example.load.balancer.extensions.context.ExecutionContextHolder.current;
import static com.example.load.balancer.extensions.context.ExecutionContextHolder.remove;

/**
 * Stomp frame handler adapter that copies stomp headers to the current {@link ExecutionContext} pre-filtering header names using the provided {@link #filter}.
 *
 * @author Nadim Benabdenbi
 */
@Slf4j
public class PreservesHeadersStompFrameHandlerAdapter implements StompFrameHandler {
    /**
     * The {@link StompFrameHandler} delegate.
     */
    private final StompFrameHandler delegate;
    /**
     * The stomp header names filter.
     */
    private final Filter<String> filter;

    /**
     * Sole constructor.
     *
     * @param delegate the delegate {@link StompFrameHandler}
     * @param filter   the stomp header names filter.
     */
    public PreservesHeadersStompFrameHandlerAdapter(@NotNull StompFrameHandler delegate, @NotNull Filter<String> filter) {
        this.delegate = delegate;
        this.filter = filter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Type getPayloadType(StompHeaders headers) {
        return delegate.getPayloadType(headers);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        ExecutionContext context = current();
        List<Entry<String, String>> eligibleHeaders = headers.toSingleValueMap().entrySet().stream()
                .filter(x -> filter.accept(x.getKey()))
                .collect(Collectors.toList());
        eligibleHeaders.forEach(x -> context.put(x.getKey(), x.getValue()));
        log.trace("Stomp Headers copied to execution context: {}.", eligibleHeaders);
        delegate.handleFrame(headers, payload);
        remove();
    }
}
