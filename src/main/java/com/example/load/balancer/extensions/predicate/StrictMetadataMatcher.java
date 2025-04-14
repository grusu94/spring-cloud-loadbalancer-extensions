//package com.example.load.balancer.extensions.predicate;
//
//import com.example.load.balancer.extensions.support.StrictMetadataMatcherConfig;
//import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;
//import lombok.extern.slf4j.Slf4j;
//
//import java.util.Map;
//import java.util.Map.Entry;
//import java.util.Set;
//
//import static com.example.load.balancer.extensions.context.ExecutionContextHolder.current;
//import static java.lang.String.format;
//
///**
// * Strict server metadata matcher over all the execution context entries.
// *
// * @author Nadim Benabdenbi
// * @see StrictMetadataMatcherConfig for a concrete usage
// */
//@Slf4j
//public class StrictMetadataMatcher extends DiscoveryEnabledServerPredicate {
//
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    protected boolean doApply(DiscoveryEnabledServer server) {
//        Set<Entry<String, String>> expected = current().entrySet();
//        Map<String, String> actual = server.getInstanceInfo().getMetadata();
//        boolean accept = actual.entrySet().containsAll(expected);
//        log.trace("Expected {} vs {}:{}{} => {}",
//                expected,
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
//        return format("StrictMetadataMatcher%s", current().entrySet());
//    }
//}
