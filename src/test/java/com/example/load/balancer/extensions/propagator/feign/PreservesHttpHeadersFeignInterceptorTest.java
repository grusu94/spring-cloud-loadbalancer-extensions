package com.example.load.balancer.extensions.propagator.feign;

import com.example.load.balancer.extensions.propagator.PatternFilter;
import feign.Request;
import feign.RequestTemplate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.regex.Pattern;

import static com.example.load.balancer.extensions.context.ExecutionContextHolder.current;
import static com.example.load.balancer.extensions.context.ExecutionContextHolder.remove;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class PreservesHttpHeadersFeignInterceptorTest {
    Set<String> keys = new HashSet<>(asList("1", "2"));
    String excludedUrl = "/some";
    PreservesHttpHeadersFeignInterceptor propagator = new PreservesHttpHeadersFeignInterceptor(new PatternFilter(asList(Pattern.compile(".*")), asList(Pattern.compile(excludedUrl))), keys::contains, new HashMap<>());
    RequestTemplate requestTemplate = new RequestTemplate();

    @BeforeEach
    public void before() {
        requestTemplate.target("http://dummy");
        requestTemplate.method(Request.HttpMethod.GET);
    }

    @AfterEach
    public void after() {
        remove();
    }

    @Test
    public void do_nothing_on_empty_context() throws Exception {
        propagator.apply(requestTemplate);
        assertThat(requestTemplate.headers().size(), is(0));
    }

    @Test
    public void do_nothing_on_excluded_url() throws Exception {
        asList("1", "3", "2").forEach(x -> current().put(x, x));
        requestTemplate.append(excludedUrl);
        propagator.apply(requestTemplate);
        assertThat(requestTemplate.headers().size(), is(0));
    }

    @Test
    public void copy_headers() throws Exception {
        asList("1", "3", "2").forEach(x -> current().put(x, x));
        propagator.apply(requestTemplate);
        Map<String, Collection<String>> headers = requestTemplate.headers();
        asList("1", "2").forEach(x -> assertThat(headers.get(x), equalTo(asList(x))));
        assertThat(headers.containsKey("3"), is(false));
    }
}