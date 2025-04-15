package com.example.load.balancer.extensions.propagator.servlet;

import com.example.load.balancer.extensions.context.ExecutionContext;
import com.example.load.balancer.extensions.propagator.Filter;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.Set;

import static com.example.load.balancer.extensions.context.ExecutionContextHolder.current;
import static com.example.load.balancer.extensions.context.ExecutionContextHolder.remove;
import static java.util.Collections.list;

/**
 * Copies Http Headers to the current {@link ExecutionContext} pre-filtering the header names using the provided {@link #filter}.
 *
 * @author Nadim Benabdenbi
 */
@Component
@Slf4j
public class PreservesHttpHeadersInterceptor implements GlobalFilter {
    /**
     * The request header names filter
     */
    private final Filter<String> filter;

    /**
     * Sole Constructor.
     *
     * @param filter The request header names filter.
     */
    public PreservesHttpHeadersInterceptor(@NotNull Filter<String> filter) {
        this.filter = filter;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        try {
            ExecutionContext context = current();
            HttpHeaders headers = exchange.getRequest().getHeaders();
            headers.keySet()
                    .stream()
                    .filter(filter::accept)
                    .forEach(x -> context.put(x, headers.getFirst(x)));
            log.trace("Propagated inbound headers {} from url=[{}].", context.entrySet(), exchange.getRequest().getURI());
        } catch (Exception e) {
            log.debug("Failed to propagate http request header.", e);
        }
        return chain.filter(exchange).doFinally(signalType -> {
            remove();
            log.trace("Context cleaned up after: {}.", signalType);
        });
    }
}
