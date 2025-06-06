package com.example.spring.cloud.loadbalancer.extensions.propagator.feign;

import com.example.spring.cloud.loadbalancer.extensions.propagator.PatternFilter;
import feign.Request;
import feign.RequestTemplate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.regex.Pattern;

import static com.example.spring.cloud.loadbalancer.extensions.context.ExecutionContextHolder.current;
import static com.example.spring.cloud.loadbalancer.extensions.context.ExecutionContextHolder.remove;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class PreservesHttpHeadersFeignInterceptorTest {
    private final Set<String> keys = new HashSet<>(asList("1", "2"));
    private final String excludedUrl = "/some";
    private final PreservesHttpHeadersFeignInterceptor propagator = new PreservesHttpHeadersFeignInterceptor(
            new PatternFilter(List.of(Pattern.compile(".*")), List.of(Pattern.compile(excludedUrl))),
            keys::contains,
            new HashMap<>());
    private final RequestTemplate requestTemplate = new RequestTemplate();

    @BeforeEach
    public void before() {
        requestTemplate.target("http://dummy");
        requestTemplate.method(Request.HttpMethod.GET);
        requestTemplate.resolve(new HashMap<>());
    }

    @AfterEach
    public void after() {
        remove();
    }

    @Test
    public void do_nothing_on_empty_context() {
        propagator.apply(requestTemplate);
        assertThat(requestTemplate.headers().size(), is(0));
    }

    @Test
    public void do_nothing_on_excluded_url() {
        asList("1", "3", "2").forEach(x -> current().put(x, x));
        requestTemplate.append(excludedUrl);
        propagator.apply(requestTemplate);
        assertThat(requestTemplate.headers().size(), is(0));
    }

    @Test
    public void copy_headers() {
        asList("1", "3", "2").forEach(x -> current().put(x, x));
        propagator.apply(requestTemplate);
        Map<String, Collection<String>> headers = requestTemplate.headers();
        asList("1", "2").forEach(x -> assertThat(headers.get(x), equalTo(List.of(x))));
        assertThat(headers.containsKey("3"), is(false));
    }
}