package com.example.spring.cloud.loadbalancer.extensions.propagator.jms;

import com.example.spring.cloud.loadbalancer.extensions.context.ExecutionContext;
import com.example.spring.cloud.loadbalancer.extensions.context.ExecutionContextHolder;
import com.example.spring.cloud.loadbalancer.extensions.propagator.Filter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.jms.JMSException;
import javax.jms.Message;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import static java.util.Collections.list;

/**
 * Copies message propagationProperties to the current {@link ExecutionContext} pre-filtering property names using the
 * provided {@link #filter}.
 * <p>Defines the utility method {@link #copyFromMessage(Message)} to be used by implementation
 */
@Slf4j
@Getter
@AllArgsConstructor
public abstract class AbstractPreservesMessageProperties {
    /**
     * The message property name filter.
     */
    private final Filter<String> filter;

    /**
     * The message property encoder.
     */
    private final MessagePropertyEncoder encoder;

    /**
     * Copies message propagationProperties to the current {@link ExecutionContext}.
     *
     * @param message the message to process.
     * @return the same message after process
     */
    protected Message copyFromMessage(Message message) {
        if (message != null) {
            try {
                ExecutionContext context = ExecutionContextHolder.current();
                List<String> eligiblePropertyNames = new ArrayList<>();
                list((Enumeration<String>) message.getPropertyNames())
                        .forEach(x -> copy(context, message, x, eligiblePropertyNames));
                log.trace("Message Properties copied {}", eligiblePropertyNames);
            } catch (JMSException e) {
                log.debug("Failed to copy message properties", e);
            }
        }
        return message;
    }

    /**
     * Copies the message property to the current execution context. failing silently when an exception is thrown.
     *
     * @param context      the context
     * @param message      the jms message
     * @param propertyName the property name to copy
     * @param collected    the propagationProperties that have been copied
     */
    private void copy(ExecutionContext context, Message message, String propertyName, List<String> collected) {
        try {
            String decoded = encoder.decode(propertyName);
            if (filter.accept(decoded)) {
                String value = message.getStringProperty(propertyName);
                context.put(decoded, value);
                collected.add(decoded);
            }
        } catch (JMSException e) {
            log.debug("Failed to copy message property [{}]", propertyName);
        }
    }
}
