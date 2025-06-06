package com.example.spring.cloud.loadbalancer.extensions.propagator.gateway;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class GatewayHeadersEnricherTest {
    private GatewayFilterChain chain;
    private MockServerWebExchange exchange;
    private final Set<String> keys = new HashSet<>(List.of("1"));
    private final HashMap<String, String> extraHeaders = new HashMap<>() {
        {
            put("1", "1");
            put("2", "2");
        }
    };
    private GatewayHeadersEnricher enricher;

    @BeforeEach
    public void setUp() {
        chain = mock(GatewayFilterChain.class);
        MockServerHttpRequest.BodyBuilder requBuilder = (MockServerHttpRequest.BodyBuilder) MockServerHttpRequest
                .get("/testFilter");
        exchange = MockServerWebExchange.from(requBuilder);
        when(chain.filter(any(ServerWebExchange.class))).thenReturn(Mono.empty());
        enricher = new GatewayHeadersEnricher(keys::contains, extraHeaders);
    }

    @Test
    public void filter() {
        Mono<Void> result = enricher.filter(exchange, chain);

        StepVerifier.create(result).verifyComplete();

        ArgumentCaptor<ServerWebExchange> exchangeCaptor = ArgumentCaptor.forClass(ServerWebExchange.class);
        verify(chain).filter(exchangeCaptor.capture());

        ServerWebExchange mutatedExchange = exchangeCaptor.getValue();
        HttpHeaders mutatedHeaders = mutatedExchange.getRequest().getHeaders();

        assertThat(mutatedHeaders.getFirst("1"), is("1"));
        assertThat(mutatedHeaders.containsKey("2"), is(false));
    }
}