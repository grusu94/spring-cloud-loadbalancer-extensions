package com.example.spring.cloud.loadbalancer.extensions.propagator.gateway;

import com.example.spring.cloud.loadbalancer.extensions.propagator.Filter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Gateway headers enricher.
 */
@Component("gatewayHttpHeadersEnricher")
@Slf4j
public class GatewayHeadersEnricher implements GlobalFilter, Ordered {

    /**
     * The extra headers to propagate.
     */
    private final Map<String, String> headers;
    /**
     * The propagation filter.
     */
    private final Filter<String> filter;

    /**
     * Constructor
     *
     * @param filter  the propagation filter
     * @param headers the extra headers to propagate.
     */
    public GatewayHeadersEnricher(Filter<String> filter, Map<String, String> headers) {
        this.headers = headers;
        this.filter = filter;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        List<Entry<String, String>> result = new ArrayList<>();
        ServerHttpRequest mutatedRequest = exchange.getRequest()
                .mutate()
                .headers(httpHeaders -> headers.entrySet().stream()
                        .filter(x -> filter.accept(x.getKey()))
                        .forEach(x -> {
                            httpHeaders.add(x.getKey(), x.getValue());
                            result.add(x);
                        }))
                .build();
        ServerWebExchange mutatedExchange = exchange.mutate()
                .request(mutatedRequest)
                .build();
        log.trace("Propagated extra headers {}.", result);

        return chain.filter(mutatedExchange);
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE + 10;
    }
}
