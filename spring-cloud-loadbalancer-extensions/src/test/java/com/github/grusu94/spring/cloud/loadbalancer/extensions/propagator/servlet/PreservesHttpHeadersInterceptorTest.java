package com.github.grusu94.spring.cloud.loadbalancer.extensions.propagator.servlet;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;

import static com.github.grusu94.spring.cloud.loadbalancer.extensions.context.ExecutionContextHolder.current;
import static com.github.grusu94.spring.cloud.loadbalancer.extensions.context.ExecutionContextHolder.remove;
import static java.util.Arrays.asList;
import static java.util.Collections.enumeration;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PreservesHttpHeadersInterceptorTest {
    private final Set<String> attributes = new HashSet<>(asList("1", "2"));
    private final PreservesHttpHeadersInterceptor propagator = new PreservesHttpHeadersInterceptor(attributes::contains);
    private final HttpServletRequest request = mock(HttpServletRequest.class);

    @AfterEach
    public void after() {
        remove();
    }

    @Test
    public void should_skip_propagate_request_headers() {
        when(request.getHeaderNames()).thenReturn(enumeration(asList("1", "2", "3")));
        attributes.forEach(x -> when(request.getHeader(x)).thenReturn(x));
        propagator.preHandle(request, null, null);
        attributes.forEach(x -> assertThat(current().get(x), equalTo(x)));
    }

    @Test
    public void should_skip_propagation_on_null_request_headers() {
        when(request.getHeaderNames()).thenReturn(null);
        attributes.forEach(x -> when(request.getHeader(x)).thenReturn(x));
        propagator.preHandle(request, null, null);
        assertThat(current().entrySet().size(), equalTo(0));
    }

    @Test
    public void fail_silent() {
        propagator.preHandle(null, null, null);
    }

    @Test
    public void testPostHandle() {
        propagator.postHandle(null, null, null, null);
        //nothing to test
    }

    @Test
    public void testAfterCompletion() {
        current().put("1", "1");
        propagator.afterCompletion(null, null, null, null);
        assertThat(current().entrySet().size(), is(0));
    }
}