package com.example.load.balancer.extensions.rule;

import com.example.load.balancer.extensions.matcher.LoadBalancingStrategy;
import com.netflix.loadbalancer.AbstractServerPredicate;
import com.netflix.loadbalancer.PredicateBasedRule;
import com.netflix.loadbalancer.Server;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


import static java.lang.String.format;

/**
 * Convenient support of predicate based rule.
 * <p>Defines a non final property predicate to satisfy the circular dependency between {@link PredicateBasedRule} and {@link AbstractServerPredicate}.
 *
 * @author Nadim Benabdenbi
 */
public class PredicateBasedRuleSupport {

    /**
     * the rule description.
     */
    @Setter
    private RuleDescription description;
    /**
     * the delegate predicate.
     */

    @Getter
    @Setter
    private LoadBalancingStrategy matcher;

    /**
     * Creates new instance of {@link PredicateBasedRuleSupport} class with specific predicate.
     *
     * @param matcher the server instances, can't be null
     * @throws IllegalArgumentException if {@code predicate} is {@code null}
     */
    public PredicateBasedRuleSupport(@NotNull LoadBalancingStrategy matcher) {
        this.matcher = matcher;
    }
}
