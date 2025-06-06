package com.example.spring.cloud.loadbalancer.extensions.propagator.gateway;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.*;

import static com.example.spring.cloud.loadbalancer.extensions.context.ExecutionContextHolder.current;
import static com.example.spring.cloud.loadbalancer.extensions.context.ExecutionContextHolder.remove;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PreservesGatewayHttpHeadersInterceptorTest {

    private GatewayFilterChain chain;
    private MockServerWebExchange exchange;
    private MockServerHttpRequest.BodyBuilder requestBuilder;
    private final Map<String, List<String>> headers = new HashMap<>() {
        {
            put("1", List.of("1"));
            put("2", List.of("2"));
            put("3", List.of("3"));
        }
    };
    private final Set<String> attributes = new HashSet<>(asList("1", "2"));
    private PreservesGatewayHttpHeadersInterceptor propagator;

    @BeforeEach
    public void setUp() {
        chain = mock(GatewayFilterChain.class);
        requestBuilder = (MockServerHttpRequest.BodyBuilder) MockServerHttpRequest
                .get("/testFilter");
        when(chain.filter(any(ServerWebExchange.class))).thenReturn(Mono.empty());
        propagator = new PreservesGatewayHttpHeadersInterceptor(attributes::contains);
    }

    @AfterEach
    public void after() {
        remove();
    }

    @Test
    public void should_skip_propagate_request_headers() {
       requestBuilder.headers(CollectionUtils.toMultiValueMap(headers));
       exchange = MockServerWebExchange.from(requestBuilder);

       Mono<Void> result = propagator.filter(exchange, e -> {
           attributes.forEach(x -> assertThat(current().get(x), equalTo(x)));
           return Mono.empty();
       });

        StepVerifier.create(result).verifyComplete();
    }

    @Test
    public void should_skip_propagation_on_null_request_headers() {
        requestBuilder.headers(new HttpHeaders()).build();
        exchange = MockServerWebExchange.from(requestBuilder);
        when(chain.filter(any(ServerWebExchange.class))).thenReturn(Mono.empty());

        Mono<Void> result = propagator.filter(exchange, e -> {
            assertThat(current().entrySet().size(), equalTo(0));
            return Mono.empty();
        });

        StepVerifier.create(result).verifyComplete();
    }

    @Test
    public void testAfterCompletion() {
        current().put("1", "1");
        requestBuilder.headers(new HttpHeaders()).build();
        exchange = MockServerWebExchange.from(requestBuilder);

        Mono<Void> result = propagator.filter(exchange, chain);
        StepVerifier.create(result).verifyComplete();

        assertThat(current().entrySet().size(), is(0));
    }
}