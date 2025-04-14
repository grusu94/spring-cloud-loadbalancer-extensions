//package com.example.load.balancer.extensions.predicate;
//
//import com.github.enadim.spring.cloud.ribbon.context.ExecutionContext;
//import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;
//import lombok.extern.slf4j.Slf4j;
//
//import java.util.Map;
//
//import static com.example.load.balancer.extensions.context.ExecutionContextHolder.current;
//import static java.lang.String.format;
//
///**
// * Filters Servers metadata against a specific {@link #metadataKey} defined in {@link ExecutionContext}.
// *
// * @author Nadim Benabdenbi
// */
//@Slf4j
//public class SingleMetadataMatcher extends DiscoveryEnabledServerPredicate {
//    /**
//     * the metadata key to test against
//     */
//    private final String metadataKey;
//
//    /**
//     * Sole Constructor.
//     *
//     * @param metadataKey the metadata key.
//     */
//    public SingleMetadataMatcher(String metadataKey) {
//        this.metadataKey = metadataKey;
//    }
//
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    protected boolean doApply(DiscoveryEnabledServer server) {
//        String expected = current().get(metadataKey);
//        Map<String, String> metadata = server.getInstanceInfo().getMetadata();
//        String actual = metadata.get(metadataKey);
//        boolean accept = (expected == null && actual == null) || (expected != null && expected.equals(actual));
//        log.trace("Expected {}=[{}] vs {}:{}{} => {}",
//                metadataKey,
//                expected,
//                server.getHostPort(),
//                server.getMetaInfo().getAppName(),
//                metadata,
//                accept);
//        return accept;
//    }
//
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public String toString() {
//        return format("SingleMetadataMatcher[%s=%s]", metadataKey, current().get(metadataKey));
//    }
//}
