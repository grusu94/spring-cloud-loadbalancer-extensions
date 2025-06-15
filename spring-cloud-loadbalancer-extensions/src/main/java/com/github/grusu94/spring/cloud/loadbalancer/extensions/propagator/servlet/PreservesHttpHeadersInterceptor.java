package com.github.grusu94.spring.cloud.loadbalancer.extensions.propagator.servlet;

import com.github.grusu94.spring.cloud.loadbalancer.extensions.context.ExecutionContext;
import com.github.grusu94.spring.cloud.loadbalancer.extensions.context.ExecutionContextHolder;
import com.github.grusu94.spring.cloud.loadbalancer.extensions.propagator.Filter;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Enumeration;

import static java.util.Collections.list;

/**
 * Copies Http Headers to the current {@link ExecutionContext} pre-filtering the header names using
 * the provided {@link #filter}.
 */
@Component
@Slf4j
public class PreservesHttpHeadersInterceptor implements HandlerInterceptor {
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


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler) {
        try {
            ExecutionContext context = ExecutionContextHolder.current();
            Enumeration<String> headerNames = request.getHeaderNames();
            if (headerNames != null) {
                list(headerNames)
                        .stream()
                        .filter(filter::accept)
                        .forEach(x -> context.put(x, request.getHeader(x)));
            }
            log.trace("Propagated inbound headers {} from url=[{}].", context.entrySet(), request.getRequestURL());
        } catch (Exception e) {
            log.debug("Failed to propagate http request header.", e);
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postHandle(@NonNull HttpServletRequest request,
                           @NonNull HttpServletResponse response,
                           @NonNull Object handler,
                           ModelAndView modelAndView) {
        //nothing to do at this stage
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void afterCompletion(@NonNull HttpServletRequest request,
                                @NonNull HttpServletResponse response,
                                @NonNull Object handler,
                                Exception ex) {
        //clean up thread local
        ExecutionContextHolder.remove();
    }
}
