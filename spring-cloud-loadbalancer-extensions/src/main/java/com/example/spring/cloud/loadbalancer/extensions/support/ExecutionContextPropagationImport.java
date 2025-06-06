package com.example.spring.cloud.loadbalancer.extensions.support;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.apache.commons.lang.StringUtils.*;

/**
 * Registers the propagation strategies defined within {@link EnableContextPropagation}.
 *
 * @author Nadim Benabdenbi
 */
@Configuration
@Slf4j
public class ExecutionContextPropagationImport implements ImportSelector {
    /**
     * class name separators
     */
    private static final String[] CLASS_NAME_SEPARATORS = new String[]{"$", "."};
    /**
     * class name separators
     */
    private static final List<String> ATTRIBUTES = asList("inboundHttpRequest", "feign", "executor", "gateway", "resilience4j", "jms", "stomp");

    /**
     * {@inheritDoc}
     */
    @Override
    @NonNull
    public String[] selectImports(AnnotationMetadata metadata) {
        List<String> imports = new ArrayList<>();
        Map<String, Object> attributes = metadata.getAnnotationAttributes(EnableContextPropagation.class.getName(), true);
        if (attributes != null) {
            ATTRIBUTES.forEach(x -> importStrategy(imports, attributes, x));
        }
        return imports.toArray(new String[imports.size()]);
    }

    /**
     * Adds the strategy to the import list when enabled.
     *
     * @param imports    the import list
     * @param attributes the annotation attributes
     * @param name       the strategy name
     */
    private void importStrategy(List<String> imports, Map<String, Object> attributes, String name) {
        if ((boolean) attributes.getOrDefault(name, false)) {
            String strategy = (String) attributes.get(name + "Strategy");
            imports.add(strategy);
            log.info("Context propagation: IMPORTING {} strategy [{}].",
                    join(splitByCharacterTypeCamelCase(name), ' ').toLowerCase(Locale.ENGLISH),
                    strategy.substring(lastIndexOfAny(strategy, CLASS_NAME_SEPARATORS) + 1));
        }
    }
}
