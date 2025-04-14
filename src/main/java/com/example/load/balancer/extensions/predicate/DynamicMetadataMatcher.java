//package com.example.load.balancer.extensions.predicate;
//
//import jakarta.validation.constraints.NotNull;
//import lombok.extern.slf4j.Slf4j;
//
//import java.util.Map;
//
//import static com.example.load.balancer.extensions.context.ExecutionContextHolder.current;
//
///**
// * Filters Servers that does have the desired metadata entry.
// * <p>The metadata key is fetched from the context entry with the desired key {@link #dynamicEntryKey}
// * <p>Concrete use case with a µservice that share a point to point connection with external system (this connection is established only once): market access / FIX connection.
// * This µservice should add the connection up marker to its metadata so that the clients can target the instance holding the connection.
// *
// * @author Nadim Benabdenbi
// */
//@Slf4j
//public class DynamicMetadataMatcher extends DiscoveryEnabledServerPredicate {
//    /**
//     * the dynamic entry key. used to get the metadata key to match.
//     */
//    private final String dynamicEntryKey;
//
//    /**
//     * matches result when dynamic entry key is missing.
//     */
//    private final boolean matchIfMissing;
//
//    /**
//     * Sole constructor.
//     *
//     * @param dynamicEntryKey the dynamic metadata key.
//     * @param matchIfMissing  the result when dynamic entry key is not defined
//     */
//    public DynamicMetadataMatcher(@NotNull String dynamicEntryKey, boolean matchIfMissing) {
//        this.dynamicEntryKey = dynamicEntryKey;
//        this.matchIfMissing = matchIfMissing;
//    }
//
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    protected boolean doApply(DiscoveryEnabledServer server) {
//        String metadataKey = current().get(dynamicEntryKey);
//        Map<String, String> metadata = server.getInstanceInfo().getMetadata();
//        if (metadataKey != null) {
//            String expected = current().get(metadataKey);
//            String actual = metadata.get(metadataKey);
//            boolean accept = (expected == null && actual == null) || (expected != null && expected.equals(actual));
//            log.trace("Expected [{}={}] vs {}:{}{} => {}",
//                    metadataKey,
//                    expected,
//                    server.getHostPort(),
//                    server.getMetaInfo().getAppName(),
//                    metadata,
//                    accept);
//            return accept;
//        } else {
//            log.trace("[{}] not defined! : {}{} => %b",
//                    dynamicEntryKey,
//                    server.getHostPort(),
//                    metadata,
//                    matchIfMissing);
//            return matchIfMissing;
//        }
//    }
//
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public String toString() {
//        String metadataKey = current().get(dynamicEntryKey);
//        return format("DynamicMetadataMatcher[(%s=%s)=%s,matchIfMissing=%b]", dynamicEntryKey, metadataKey, metadataKey == null ? null : current().get(metadataKey), matchIfMissing);
//    }
//}
