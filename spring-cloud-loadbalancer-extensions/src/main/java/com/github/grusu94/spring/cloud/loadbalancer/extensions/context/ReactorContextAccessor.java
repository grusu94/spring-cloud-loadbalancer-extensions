package com.github.grusu94.spring.cloud.loadbalancer.extensions.context;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.Map;

import static com.github.grusu94.spring.cloud.loadbalancer.extensions.propagator.gateway.PreservesGatewayHttpHeadersInterceptor.REACTOR_CONTEXT_KEY;

/**
 * Execution CONTEXT accessor for reactive applications using Project Reactor.
 */
@Slf4j
public class ReactorContextAccessor implements ContextAccessor {

    /**
     * Singleton instance of ReactorContextAccessor.
     */
    private static final ReactorContextAccessor INSTANCE = new ReactorContextAccessor();

    /**
     * utility class should not be instantiated
     */
    private ReactorContextAccessor() {
    }

    /**
     * Gets the singleton instance.
     *
     * @return the singleton instance
     */
    public static ReactorContextAccessor getInstance() {
        return INSTANCE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExecutionContext current() {
        try {
            // Retrieve the ExecutionContext from Reactor's current context
            ExecutionContext context = Mono.deferContextual(ctx -> {
                if (ctx.hasKey(REACTOR_CONTEXT_KEY)) {
                    Map<String, String> contextMap = ctx.get(REACTOR_CONTEXT_KEY);
                    return Mono.just(new DefaultExecutionContext(contextMap));
                }
                return Mono.empty();
            }).block();

            if (context != null) {
                return context;
            }
        } catch (Exception e) {
            log.trace("Unable to retrieve context from Reactor Context: {}", e.getMessage());
        }

        // Fallback to default context if not in a reactive pipeline or on error
        return new DefaultExecutionContext();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExecutionContext switchTo(ExecutionContext context) {
        // Context switching is done via contextWrite() operator
        return context;
    }

    /**
     * {@inheritDoc}
     *
     * @return the CONTEXT that have been removed.
     */
    @Override
    public ExecutionContext remove() {
        // The context is typically cleared by the reactive chain or subscription end
        return current();
    }
}
