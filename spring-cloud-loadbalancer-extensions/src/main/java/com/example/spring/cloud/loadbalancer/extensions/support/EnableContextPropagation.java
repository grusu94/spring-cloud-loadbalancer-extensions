package com.example.spring.cloud.loadbalancer.extensions.support;

import com.example.spring.cloud.loadbalancer.extensions.context.ExecutionContext;
import com.example.spring.cloud.loadbalancer.extensions.context.ExecutionContextHolder;
import com.example.spring.cloud.loadbalancer.extensions.support.strategy.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;
import java.util.concurrent.ExecutorService;

/**
 * Enables {@link ExecutionContext} attributes propagation.
 * <p>Use it carefully due to {@link ExecutionContextHolder} usage of the {@link InheritableThreadLocal}:
 * This strategy will not cover all the use cases and you may encounter unexpected behaviours (Use it at your own risk):
 * <ul>
 * <li>Verify that {@link ExecutorService} are declared as beans and not hidden by some implementation.
 * <li>Verify that {@link org.springframework.jms.core.JmsTemplate} are declared as beans and not hidden by some implementation.
 * <li>Verify that {@link org.springframework.messaging.simp.stomp.StompSession} are declared as beans and not hidden by some implementation.
 * If you meet those requirements (and every dependency on your project) it will work perfectly.
 * </ul>
 * <p>To be used at spring boot configuration level. For example:
 * <blockquote><pre>
 * &#064;EnableContextPropagation
 * &#064;SpringBootApplication
 * public class Application{
 *  ...
 * }
 * </pre></blockquote>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@EnableConfigurationProperties({PropagationProperties.class, EurekaInstanceProperties.class})
@Import(ExecutionContextPropagationImport.class)
public @interface EnableContextPropagation {
    /**
     * default value is {@code true}.
     *
     * @return {@code true} when servlet propagation should be enabled otherwise {@code false}.
     */
    boolean inboundHttpRequest() default true;

    /**
     * default is {@link PreservesHeadersInboundHttpRequestStrategy}
     *
     * @return the servlet propagation strategy.
     */
    Class<?> inboundHttpRequestStrategy() default PreservesHeadersInboundHttpRequestStrategy.class;

    /**
     * default value is {@code true}.
     *
     * @return {@code true} when feign propagation should be enabled otherwise {@code false}.
     */
    boolean feign() default true;

    /**
     * default is {@link PreservesHttpHeadersFeignStrategy}
     *
     * @return the feign propagation strategy.
     */
    Class<?> feignStrategy() default PreservesHttpHeadersFeignStrategy.class;

    /**
     * default value is {@code true}.
     *
     * @return {@code true} when executor propagation should be enabled otherwise {@code false}.
     */
    boolean executor() default true;

    /**
     * default is {@link PreservesExecutionContextExecutorStrategy}.
     *
     * @return the executors propagation strategy.
     */
    Class<?> executorStrategy() default PreservesExecutionContextExecutorStrategy.class;

    /**
     * default value is {@code true}.
     *
     * @return {@code true} when zuul propagation should be enabled otherwise {@code false}.
     */
    boolean gateway() default true;

    /**
     * default is {@link PreservesHttpHeadersGatewayStrategy}
     *
     * @return the gateway propagation strategy.
     */
    Class<?> gatewayStrategy() default PreservesHttpHeadersGatewayStrategy.class;

    /**
     * default value is {@code true}.
     *
     * @return {@code true} when Resilience4j propagation should be enabled otherwise {@code false}.
     */
    boolean resilience4j() default true;

    /**
     * default strategy is {@link PreservesExecutionContextResilience4jStrategy}
     *
     * @return the hystrix propagation strategy.
     */
    Class<?> resilience4jStrategy() default PreservesExecutionContextResilience4jStrategy.class;

    /**
     * default value is {@code true}.
     *
     * @return {@code true} when jms propagation should be enabled otherwise {@code false}.
     */
    boolean jms() default true;

    /**
     * default strategy is {@link PreservesJmsMessagePropertiesStrategy}.
     *
     * @return the jms propagation strategy.
     */
    Class<?> jmsStrategy() default PreservesJmsMessagePropertiesStrategy.class;

    /**
     * default value is {@code true}.
     *
     * @return {@code true} when stomp propagation should be enabled otherwise {@code false}.
     */
    boolean stomp() default true;

    /**
     * default is {@link PreservesStompHeadersStrategy}
     *
     * @return the stomp propagation strategy.
     */
    Class<?> stompStrategy() default PreservesStompHeadersStrategy.class;
}
