package com.example.load.balancer.extensions.support;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.ribbon.RibbonClientConfiguration;
import org.springframework.cloud.netflix.ribbon.ZonePreferenceServerListFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * Convenient configuration initializing the ribbon client config that is required for defining custom rules.
 * <p>Disables server list filter: ({@link ZonePreferenceServerListFilter})
 * otherwise we may experience some weird error as {@link NullPointerException} logging.
 * <p>Replaces the default {@link ZoneAwareLoadBalancer} with its super class {@link DynamicServerListLoadBalancer}
 * because of the strong dependency with the {@link ZoneAvoidancePredicate} that leads to worst performance.
 *
 * @author Nadim Benabdenbi
 * @see RibbonClientConfiguration
 */
@Configuration
@EnableConfigurationProperties(EurekaInstanceProperties.class)
@Slf4j
public class RuleBaseConfig {
    /**
     * The eureka instance propagationProperties.
     */
    @Autowired
    @Getter
    private EurekaInstanceProperties eurekaInstanceProperties;

    /**
     * The load balancing rule definition.
     *
     * @return the predicate base rule: expects a single predicate defined on the context.
     */
    @Bean
    @Lazy
    public PredicateBasedRuleSupport rule() {
        return new PredicateBasedRuleSupport();
    }

//    /**
//     * The load balancer definition.
//     *
//     * @param config            the client config.
//     * @param serverList        the server list.
//     * @param serverListFilter  the server list filter.
//     * @param rule              the load balancing rule.
//     * @param ping              the ping strategy.
//     * @param serverListUpdater the server list updater.
//     * @return The Dynamic Server List Load Balancer.
//     */
//    @Bean
//    @ConditionalOnMissingBean
//    public ILoadBalancer loadBalancer(IClientConfig config,
//                                      ServerList<Server> serverList,
//                                      ServerListFilter<Server> serverListFilter,
//                                      IRule rule, IPing ping,
//                                      ServerListUpdater serverListUpdater) {
//        log.debug("dynamic server list load balancer enabled.");
//        return new DynamicServerListLoadBalancer<>(config, rule, ping, serverList,
//                serverListFilter, serverListUpdater);
//    }
//
//    /**
//     * The server list filter definition.
//     *
//     * @return a pass-through filter.
//     */
//    @Bean
//    @ConditionalOnMissingBean
//    public ServerListFilter<Server> serverListFilter() {
//        log.debug("ribbon discovery server list filter disabled.");
//        return x -> x;
//    }
}
