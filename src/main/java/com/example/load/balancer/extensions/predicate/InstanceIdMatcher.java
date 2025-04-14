//package com.example.load.balancer.extensions.predicate;
//
//import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;
//import jakarta.validation.constraints.NotNull;
//import lombok.extern.slf4j.Slf4j;
//
//import static java.lang.String.format;
//
///**
// * Filters servers against the {@link #expectedInstanceId}.
// *
// * @author Nadim Benabdenbi
// */
//@Slf4j
//public class InstanceIdMatcher extends DiscoveryEnabledServerPredicate {
//    /**
//     * The expected instance id to match.
//     */
//    private final String expectedInstanceId;
//
//    /**
//     * Sole constructor.
//     *
//     * @param expectedInstanceId The expected instance id to match.
//     */
//    public InstanceIdMatcher(@NotNull String expectedInstanceId) {
//        this.expectedInstanceId = expectedInstanceId;
//    }
//
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    protected boolean doApply(DiscoveryEnabledServer server) {
//        String actual = server.getInstanceInfo().getInstanceId();
//        boolean accept = expectedInstanceId.equals(actual);
//        log.trace("Expected [{}] vs {}:{}[{}] => {}",
//                expectedInstanceId,
//                server.getHostPort(),
//                server.getMetaInfo().getAppName(),
//                actual,
//                accept);
//        return accept;
//    }
//
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public String toString() {
//        return format("InstanceIdMatcher[instanceId=%s]", expectedInstanceId);
//    }
//}
