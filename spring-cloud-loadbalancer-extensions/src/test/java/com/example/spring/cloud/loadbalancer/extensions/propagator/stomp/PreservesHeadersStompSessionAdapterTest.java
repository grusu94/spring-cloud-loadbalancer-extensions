package com.example.spring.cloud.loadbalancer.extensions.propagator.stomp;

import com.example.spring.cloud.loadbalancer.extensions.ArgumentHolder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.example.spring.cloud.loadbalancer.extensions.context.ExecutionContextHolder.current;
import static com.example.spring.cloud.loadbalancer.extensions.context.ExecutionContextHolder.remove;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class PreservesHeadersStompSessionAdapterTest {
    private final String destination = "destination";
    private final StompHeaders headers = new StompHeaders();
    private final Object payload = new Object();
    private final Set<String> keysToCopy = new HashSet<>(List.of("1", "2"));
    private final StompSession delegate = mock(StompSession.class);
    private final StompSessionHandler handler = mock(StompSessionHandler.class);
    private final PreservesHeadersStompSessionAdapter propagator =
            new PreservesHeadersStompSessionAdapter(delegate, keysToCopy::contains, new HashMap<>());

    @AfterEach
    public void after() {
        remove();
    }

    @Test
    public void testGetSessionId() {
        propagator.getSessionId();
        verify(delegate).getSessionId();
    }

    @Test
    public void testIsConnected() {
        propagator.isConnected();
        verify(delegate).isConnected();
    }

    @Test
    public void testSetAutoReceipt() {
        propagator.setAutoReceipt(true);
        verify(delegate).setAutoReceipt(true);
    }

    @Test
    public void testSendEmptyContext() {
        propagator.send(destination, payload);
        ArgumentHolder<StompHeaders> headers = new ArgumentHolder<>();
        verify(delegate).send(headers.eq(), eq(payload));
        keysToCopy.forEach(x -> assertThat(headers.getArgument().get(x), is(nullValue())));
    }

    @Test
    public void testNotEmptyContext() {
        asList("1", "3", "2").forEach(x -> current().put(x, x));
        propagator.send(destination, payload);
        ArgumentHolder<StompHeaders> headers = new ArgumentHolder<>();
        verify(delegate).send(headers.eq(), eq(payload));
        keysToCopy.forEach(x -> assertThat(headers.getArgument().get(x), equalTo(List.of(x))));
        assertThat(headers.getArgument().get("3"), is(nullValue()));
    }

    @Test
    public void testSendWithStompHeaders() {
        asList("1", "3", "2").forEach(x -> current().put(x, x));
        propagator.send(headers, payload);
        verify(delegate).send(headers, payload);
        keysToCopy.forEach(x -> assertThat(headers.get(x), equalTo(List.of(x))));
        assertThat(headers.get("3"), is(nullValue()));
    }

    @Test
    public void testSubscribe() {
        propagator.subscribe(destination, handler);
        verify(delegate).subscribe(eq(destination), any(PreservesHeadersStompFrameHandlerAdapter.class));
    }

    @Test
    public void testSubscribeWithStompHeaders() {
        propagator.subscribe(headers, handler);
        verify(delegate).subscribe(eq(headers), any(PreservesHeadersStompFrameHandlerAdapter.class));
    }

    @Test
    public void testAcknowledgeMessageId() {
        propagator.acknowledge("", true);
        verify(delegate).acknowledge("", true);
    }

    @Test
    public void testAcknowledgeHeaders() {
        propagator.acknowledge(new StompHeaders(), true);
        verify(delegate).acknowledge(new StompHeaders(), true);
    }

    @Test
    public void testDisconnect() {
        propagator.disconnect();
        verify(delegate).disconnect();
    }
}