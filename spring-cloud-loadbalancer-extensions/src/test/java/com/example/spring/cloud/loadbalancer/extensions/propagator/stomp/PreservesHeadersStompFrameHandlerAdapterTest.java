package com.example.spring.cloud.loadbalancer.extensions.propagator.stomp;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.example.spring.cloud.loadbalancer.extensions.context.ExecutionContextHolder.remove;
import static java.util.Arrays.asList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class PreservesHeadersStompFrameHandlerAdapterTest {
    private final Object payload = new Object();
    private final Set<String> attributes = new HashSet<>(List.of("1", "2"));
    private final StompFrameHandler delegate = mock(StompFrameHandler.class);
    private final PreservesHeadersStompFrameHandlerAdapter propagator =
            new PreservesHeadersStompFrameHandlerAdapter(delegate, attributes::contains);

    @AfterEach
    public void After() {
        remove();
    }

    @Test
    public void testGetPayloadType() {
        propagator.getPayloadType(null);
        verify(delegate).getPayloadType(null);
    }

    @Test
    public void testHandleFrame() {
        StompHeaders headers = new StompHeaders();
        asList("1", "2", "3").forEach(x -> headers.set(x, x));
        propagator.handleFrame(headers, payload);
        verify(delegate).handleFrame(headers, payload);
    }
}