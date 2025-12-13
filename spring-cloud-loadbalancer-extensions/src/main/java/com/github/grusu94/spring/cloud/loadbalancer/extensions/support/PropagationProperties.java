package com.github.grusu94.spring.cloud.loadbalancer.extensions.support;

import com.github.grusu94.spring.cloud.loadbalancer.extensions.propagator.Filter;
import com.github.grusu94.spring.cloud.loadbalancer.extensions.propagator.PatternFilter;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * The propagation properties.
 */
@ConfigurationProperties(prefix = LoadBalancerExtensionsConstants.PROPAGATION_PREFIX)
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
     * @param instanceProperties the instance properties.
     * @return the extra static entries
     */
    public Map<String, String> buildExtraStaticEntries(InstanceProperties instanceProperties) {
        if (upStreamZone.isEnabled()) {
            getExtraStaticEntries().put(upStreamZone.getKey(), instanceProperties.getZone());
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
        private String key = LoadBalancerExtensionsConstants.DEFAULT_UPSTREAM_ZONE_KEY;
    }
}
