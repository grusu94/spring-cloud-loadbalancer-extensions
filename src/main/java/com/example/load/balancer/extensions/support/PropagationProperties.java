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
package com.example.load.balancer.extensions.support;

import com.example.load.balancer.extensions.propagator.Filter;
import com.example.load.balancer.extensions.propagator.PatternFilter;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.example.load.balancer.extensions.support.RibbonExtensionsConstants.DEFAULT_UPSTREAM_ZONE_KEY;
import static com.example.load.balancer.extensions.support.RibbonExtensionsConstants.PROPAGATION_PREFIX;

/**
 * The propagation properties.
 */
@ConfigurationProperties(prefix = PROPAGATION_PREFIX)
@Component
@Getter
public class PropagationProperties {

    /**
     * the up stream zone propagationProperties.
     */
    private final UpStreamZoneProperties upStreamZone = new UpStreamZoneProperties();
    /**
     * the entry keys to propagate.
     */
    private final List<String> keys = new ArrayList<>();

    /**
     * the extra static entries.
     */
    private final Map<String, String> extraStaticEntries = new HashMap<>();

    /**
     * The executor inclusion.
     */
    private final PatternFilter executor = new PatternFilter();

    /**
     * The executor inclusion.
     */
    private final PatternFilter feign = new PatternFilter();

    /**
     * The executor inclusion.
     */
    private final PatternFilter jms = new PatternFilter();

    /**
     * The executor inclusion.
     */
    private final PatternFilter stomp = new PatternFilter();

    /**
     * @return the propagation entries filter
     */
    public Filter<String> buildEntriesFilter() {
        return new HashSet<>(getKeys())::contains;
    }

    /**
     * @param eurekaInstanceProperties the eureka instance properties.
     * @return the extra static entries
     */
    public Map<String, String> buildExtraStaticEntries(EurekaInstanceProperties eurekaInstanceProperties) {
        if (upStreamZone.isEnabled()) {
            getExtraStaticEntries().put(upStreamZone.getKey(), eurekaInstanceProperties.getZone());
        }
        return extraStaticEntries;
    }

    @Getter
    @Setter
    public static class UpStreamZoneProperties {
        /**
         * the upstream zone propagation indicator
         */
        private boolean enabled = true;
        /**
         * the upstream zone key.
         */
        private String key = DEFAULT_UPSTREAM_ZONE_KEY;
    }
}
