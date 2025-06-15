package com.github.grusu94.spring.cloud.loadbalancer.extensions.propagator;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.regex.Pattern.compile;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class PatternFilterTest {
    private final String value1 = "12";
    private final String value2 = "23";
    private final Pattern pattern1 = compile(value1);
    private final Pattern pattern2 = compile(value2);

    @Test
    public void accept() {
        MatcherAssert.assertThat(new PatternFilter().accept(value1), Matchers.is(true));
        assertThat(new PatternFilter(singletonList(pattern1), emptyList()).accept(value1), Matchers.is(true));
        assertThat(new PatternFilter(singletonList(pattern1), emptyList()).accept(value2), Matchers.is(false));
        assertThat(new PatternFilter(singletonList(pattern1), singletonList(pattern2)).accept(value1), Matchers.is(true));
        assertThat(new PatternFilter(singletonList(pattern1), singletonList(pattern2)).accept(value2), Matchers.is(false));
        assertThat(new PatternFilter(singletonList(pattern1), singletonList(pattern2)).accept(value1 + value2), Matchers.is(false));
        assertThat(new PatternFilter(asList(pattern1, pattern2), emptyList()).accept(value1), Matchers.is(true));
        assertThat(new PatternFilter(asList(pattern1, pattern2), emptyList()).accept(value2), Matchers.is(true));
        assertThat(new PatternFilter(asList(pattern1, pattern2), emptyList()).accept(value1 + value2), Matchers.is(true));
        assertThat(new PatternFilter(asList(pattern1, pattern2), emptyList()).accept(""), Matchers.is(false));
    }

    @Test
    public void getIncludes() {
        assertThat(new PatternFilter().getIncludes().size(), is(1));
    }

    @Test
    public void getExcludes() {
        assertThat(new PatternFilter().getExcludes(), equalTo(emptyList()));
    }
}