package com.example.load.balancer.extensions.propagator.servlet;

import com.example.load.balancer.extensions.context.ExecutionContext;
import com.example.load.balancer.extensions.propagator.Filter;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

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
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        try {
            ExecutionContext context = current();
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
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler,
                           ModelAndView modelAndView) throws Exception {
        //nothing to do at this stage
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) throws Exception {
        //clean up thread local
        remove();
    }
}
