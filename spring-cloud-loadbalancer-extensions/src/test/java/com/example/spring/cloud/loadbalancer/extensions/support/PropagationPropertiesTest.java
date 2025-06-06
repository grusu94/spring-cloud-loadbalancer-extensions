package com.example.spring.cloud.loadbalancer.extensions.support;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class PropagationPropertiesTest {

    @Test
    public void buildEntriesFilter() {
        PropagationProperties properties = new PropagationProperties();
        assertThat(properties.buildEntriesFilter().accept(""), is(false));
    }

    @Test
    public void buildExtraStaticEntries() {
        PropagationProperties properties = new PropagationProperties();
        assertThat(properties.buildExtraStaticEntries(new EurekaInstanceProperties()).get("upstream-zone"), is("default"));

        properties = new PropagationProperties();
        properties.getUpStreamZone().setKey("new");
        assertThat(properties.buildExtraStaticEntries(new EurekaInstanceProperties()).get("new"), is("default"));

        properties = new PropagationProperties();
        properties.getUpStreamZone().setEnabled(false);
        assertThat(properties.buildExtraStaticEntries(new EurekaInstanceProperties()).get("upstream-zone"), is(nullValue()));
    }
}