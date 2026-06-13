package com.github.grusu94.spring.cloud.loadbalancer.extensions.context;

import lombok.extern.slf4j.Slf4j;

/**
 * Factory for creating the appropriate {@link ContextAccessor} implementation
 * based on the application type (servlet or reactive).
 *
 * Based on spring.main.web-application-type detects whether the application is reactive or servlet
 * and selects the appropriate strategy:
 * - {@link ReactorContextAccessor} for reactive applications
 * - {@link ThreadLocalContextAccessor} for servlet applications
 */
@Slf4j
public class ContextAccessorFactory {

    private static final String WEB_APPLICATION_TYPE_PROPERTY = "spring.main.web-application-type";

    private static final ContextAccessor CONTEXT_ACCESSOR = createContextAccessor();

    private ContextAccessorFactory() {
    }

    /**
     * Creates the appropriate context accessor based on application type.
     *
     * @return ReactorContextAccessor if is reactive application, ThreadLocalContextAccessor otherwise
     */
    private static ContextAccessor createContextAccessor() {
        if (isReactiveApplication()) {
            return ReactorContextAccessor.getInstance();
        } else {
            return ThreadLocalContextAccessor.getInstance();
        }
    }

    /**
     * Gets the appropriate context accessor for the current application.
     *
     * @return the context accessor instance
     */
    public static ContextAccessor getContextAccessor() {
        return CONTEXT_ACCESSOR;
    }

    /**
     * Retrieves the web application type from Spring properties.
     *
     * @return the value of spring.main.web-application-type, or "SERVLET" as default
     */
    public static boolean isReactiveApplication() {
        final String webType = System.getProperty(WEB_APPLICATION_TYPE_PROPERTY);
        return "REACTIVE".equalsIgnoreCase(webType);
    }

}
