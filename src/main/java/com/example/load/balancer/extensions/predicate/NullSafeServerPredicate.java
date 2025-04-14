//package com.example.load.balancer.extensions.predicate;
//
//import com.netflix.loadbalancer.AbstractServerPredicate;
//import com.netflix.loadbalancer.PredicateKey;
//import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;
//
//import javax.annotation.Nullable;
//
///**
// * Convenient decorator avoiding the delegate to run against a null {@link PredicateKey} or a null {@link PredicateKey#getServer()}.
// *
// * @author Nadim Benabdenbi
// */
//public abstract class NullSafeServerPredicate extends AbstractServerPredicate {
//
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public final boolean apply(@Nullable PredicateKey input) {
//        return input != null && input.getServer() != null && doApply(input);
//    }
//
//    /**
//     * Tests if {@link DiscoveryEnabledServer} matches this predicate.
//     *
//     * @param input the current server
//     * @return {@code true} if the server matches the predicate otherwise {@code false}
//     * @see #apply(PredicateKey)
//     */
//    protected abstract boolean doApply(PredicateKey input);
//
//
//}
