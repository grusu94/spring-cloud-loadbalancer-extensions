package com.github.grusu94.spring.cloud.loadbalancer.extensions.propagator.gateway;

import com.github.grusu94.spring.cloud.loadbalancer.extensions.context.DefaultExecutionContext;
import com.github.grusu94.spring.cloud.loadbalancer.extensions.context.ExecutionContext;
import com.github.grusu94.spring.cloud.loadbalancer.extensions.context.ReactorExecutionContext;
import com.github.grusu94.spring.cloud.loadbalancer.extensions.propagator.Filter;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * Copies Http Headers to the current {@link ExecutionContext} pre-filtering the header names using the provided
 * {@link #filter}.
 */
@Component("gatewayHttpHeadersToContext")
@Slf4j
public class PreservesGatewayHttpHeadersInterceptor implements GlobalFilter, Ordered {
    /**
     * The request header names filter
     */
    private final Filter<String> filter;

    /**
     * Constructor.
     *
     * @param filter The request header names filter.
     */
    public PreservesGatewayHttpHeadersInterceptor(@NotNull Filter<String> filter) {
        this.filter = filter;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        final Map<String, String> context = new HashMap<>();

        try {
            HttpHeaders headers = exchange.getRequest().getHeaders();
            headers.keySet()
                    .stream()
                    .filter(filter::accept)
                    .forEach(x -> context.put(x, headers.getFirst(x)));
            log.trace("Propagated inbound headers {} from url=[{}].", context.entrySet(),
                    exchange.getRequest().getURI());
        } catch (Exception e) {
            log.debug("Failed to propagate http request header.", e);
        }

        final ExecutionContext executionContext = new DefaultExecutionContext(context);
        return chain.filter(exchange)
                .contextWrite(ctx -> ReactorExecutionContext.put(ctx, executionContext));
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }
}
