package com.example.load.balancer.extensions.rule;

import com.example.load.balancer.extensions.matcher.LoadBalancingStrategy;

import static java.lang.String.format;

/**
 * Rule Convenient Description
 *
 * @see PredicateBasedRuleSupport for concrete usage.
 */
@FunctionalInterface
public interface RuleDescription {
    /**
     * @return the rule description
     */
    String describe();

    /**
     * Constructs a rule description from a given static parameter.
     *
     * @param description the static rule description.
     * @return the rule description.
     */
    static RuleDescription from(String description) {
        return () -> description;
    }

    /**
     * Constructs a rule description from a matcher.
     *
     * @param matcherStrategy the strategy.
     * @return the predicate rule description.
     */
    static RuleDescription from(LoadBalancingStrategy matcherStrategy) {
        return matcherStrategy::toString;
    }

    /**
     * Derive a Rule description with an AND operator.
     *
     * @param rightOperand the right operand description.
     * @return the composite AND rule description.
     */
    default RuleDescription and(RuleDescription rightOperand) {
        return () -> format("(%s && %s)", describe(), rightOperand.describe());
    }

    /**
     * Derive a Rule description with a fallback operator.
     *
     * @param fallback the fallback description.
     * @return the composite fallback rule description.
     */
    default RuleDescription fallback(RuleDescription fallback) {
        return () -> format("%s -> %s", describe(), fallback.describe());
    }
}
