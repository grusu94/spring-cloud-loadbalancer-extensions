//package com.example.load.balancer.extensions.predicate;
//
//import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
//import org.springframework.cloud.client.ServiceInstance;
//
///**
// * Convenient class for predicates that are based on {@link DiscoveryEnabledServer} created by the {@link DiscoveryEnabledNIWSServerList}.
// * {@link DiscoveryEnabledNIWSServerList}.
// * <p>Concrete implementation needs to implement the {@link #doApply(DiscoveryEnabledServer)} method.
// *
// * @author Nadim Benabdenbi
// */
//public abstract class DiscoveryEnabledServerPredicate extends NullSafeServerPredicate {
//
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    @SuppressFBWarnings("BC_UNCONFIRMED_CAST_OF_RETURN_VALUE")
//    protected boolean doApply(PredicateKey input) {
//        return input.getServer() instanceof DiscoveryEnabledServer
//                && doApply((ServiceInstance) input.getServer());
//    }
//
//    /**
//     * Tests if {@link DiscoveryEnabledServer} matches this predicate.
//     *
//     * @param server the discovered server
//     * @return {@code true} if the server matches the predicate otherwise {@code false}
//     * @see #apply(PredicateKey)
//     */
//    protected abstract boolean doApply(DiscoveryEnabledServer server);
//}
