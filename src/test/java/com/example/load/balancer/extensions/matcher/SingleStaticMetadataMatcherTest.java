/*
 * Copyright (c) 2017 the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.load.balancer.extensions.matcher;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.cloud.client.ServiceInstance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.load.balancer.extensions.context.ExecutionContextHolder.remove;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SingleStaticMetadataMatcherTest {
    private String attributeKey = "name";
    private String expectedValue = "value";

    private Map<String, String> metadata = new HashMap<>();

    ServiceInstance instance = mock(ServiceInstance.class);

    private SingleStaticMetadataMatch singleStaticMetadataMatcher = new SingleStaticMetadataMatch(attributeKey, expectedValue);

    @BeforeEach
    public void before() {
        when(instance.getMetadata()).thenReturn(metadata);
    }

    @AfterEach
    public void after() {
        remove();
    }

    @Test
    public void should_not_filter_server_with_expected_attribute_value() {
        metadata.put(attributeKey, expectedValue);
        assertThat(singleStaticMetadataMatcher.apply(List.of(instance)).size(), is(1));
        assertThat(singleStaticMetadataMatcher.toString(), is("StrictMetadataMatcher[name=value]"));
    }

    @Test
    public void should_filter_server_with_no_attribute() {
        assertThat(singleStaticMetadataMatcher.apply(List.of(instance)).size(), is(0));
    }

    @Test
    public void should_filter_server_with_different_attribute_value() {
        metadata.put(attributeKey, attributeKey);
        assertThat(singleStaticMetadataMatcher.apply(List.of(instance)).size(), is(0));
    }
}