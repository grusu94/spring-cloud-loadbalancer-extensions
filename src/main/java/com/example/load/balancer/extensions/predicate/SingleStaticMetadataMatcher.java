//package com.example.load.balancer.extensions.predicate;
//
//import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;
//import jakarta.validation.constraints.NotNull;
//import lombok.extern.slf4j.Slf4j;
//
//import java.util.Map;
//
//import static java.lang.String.format;
//
///**
// * Filters Servers against a specific {@link #entryKey} that matches the {@link #entryValue}.
// *
// * @author Nadim Benabdenbi
// */
//@Slf4j
//public class SingleStaticMetadataMatcher extends DiscoveryEnabledServerPredicate {
//    /**
//     * the entry key to match.
//     */
//    private final String entryKey;
//    /**
//     * the expected entry value
//     */
//    private final String entryValue;
//
//    /**
//     * Sole constructor
//     *
//     * @param entryKey   the attribute key to match.
//     * @param entryValue the expected entry value
//     */
//    public SingleStaticMetadataMatcher(@NotNull String entryKey, @NotNull String entryValue) {
//        this.entryKey = entryKey;
//        this.entryValue = entryValue;
//    }
//
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    protected boolean doApply(DiscoveryEnabledServer server) {
//        Map<String, String> metadata = server.getInstanceInfo().getMetadata();
//        String actual = metadata.get(entryKey);
//        boolean accept = entryValue.equals(actual);
//        log.trace("Expected [{}={}] vs {}:{}{} => {}",
//                entryKey,
//                entryValue,
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
//        return format("StrictMetadataMatcher[%s=%s]", entryKey, entryValue);
//    }
//}
