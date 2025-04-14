//package com.example.load.balancer.extensions.predicate;
//
//import com.example.load.balancer.extensions.context.ExecutionContextHolder;
//import com.example.load.balancer.extensions.support.FavoriteZoneConfig;
//import com.netflix.loadbalancer.PredicateKey;
//import com.netflix.loadbalancer.Server;
//import jakarta.validation.constraints.NotNull;
//import lombok.extern.slf4j.Slf4j;
//
///**
// * Filters Servers having the same zone as the context entry value associated with the {@link #zoneEntryKey}.
// *
// * @author Nadim Benabdenbi
// * @see FavoriteZoneConfig for a concrete usage
// */
//@Slf4j
//public class DynamicZoneMatcher extends NullSafeServerPredicate {
//    /**
//     * the zone entry key.
//     */
//    private final String zoneEntryKey;
//
//    /**
//     * Sole Constructor.
//     *
//     * @param zoneEntryKey the favorite zone entry key.
//     */
//    public DynamicZoneMatcher(@NotNull String zoneEntryKey) {
//        this.zoneEntryKey = zoneEntryKey;
//    }
//
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    protected boolean doApply(PredicateKey input) {
//        Server server = input.getServer();
//        String expected = ExecutionContextHolder.current().get(zoneEntryKey);
//        String actual = server.getZone();
//        boolean accept = expected != null && expected.equals(actual);
//        log.trace("Expected [{}={}] vs {}:{}[zone={}] => {}",
//                zoneEntryKey,
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
//        return format("DynamicZoneMatcher[%s=%s]", zoneEntryKey, ExecutionContextHolder.current().get(zoneEntryKey));
//    }
//}
