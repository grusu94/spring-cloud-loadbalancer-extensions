//package com.example.load_balancer_extensions.support.strategy;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.cloud.netflix.zuul.web.ZuulHandlerMapping;
//import org.springframework.context.annotation.Configuration;
//
///**
// * Default Zuul propagation strategy based on http request headers copy to the execution context.
// */
//@Configuration
//@ConditionalOnClass(ZuulHandlerMapping.class)
//@ConditionalOnProperty(value = "ribbon.extensions.propagation.zuul.enabled", matchIfMissing = true)
//@ConditionalOnExpression(value = "${ribbon.extensions.propagation.enabled:true}")
//@Slf4j
//public class PreservesHttpHeadersZuulStrategy extends InstantiationAwareBeanPostProcessorAdapter {
//    @Autowired
//    private PropagationProperties properties;
//    @Autowired
//    private EurekaInstanceProperties eurekaInstanceProperties;
//
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public boolean postProcessAfterInstantiation(Object bean, String beanName) {
//        if (bean instanceof ZuulHandlerMapping) {
//            ZuulHandlerMapping zuulHandlerMapping = (ZuulHandlerMapping) bean;
//            zuulHandlerMapping.setInterceptors(new PreservesHttpHeadersInterceptor(properties.buildEntriesFilter()));
//            zuulHandlerMapping.setInterceptors(new ZuulHeadersEnricher(properties.buildEntriesFilter(), properties.buildExtraStaticEntries(eurekaInstanceProperties)));
//            log.info("Context propagation enabled for zuul handler[{}] on keys={}.", beanName, properties.getKeys());
//        }
//        return super.postProcessAfterInstantiation(bean, beanName);
//    }
//}
