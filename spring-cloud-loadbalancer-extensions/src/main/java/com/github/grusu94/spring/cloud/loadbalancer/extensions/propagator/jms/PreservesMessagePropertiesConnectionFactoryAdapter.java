package com.github.grusu94.spring.cloud.loadbalancer.extensions.propagator.jms;

import com.github.grusu94.spring.cloud.loadbalancer.extensions.context.ExecutionContext;
import com.github.grusu94.spring.cloud.loadbalancer.extensions.propagator.Filter;
import lombok.AllArgsConstructor;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import java.util.Map;

/**
 * Connection factory adapter that preserves the {@link ExecutionContext} by copying message propagationProperties from/to
 * the current {@link ExecutionContext} pre-filtering property names or entry keys using the provided {@link #filter}.
 */
@AllArgsConstructor
public class PreservesMessagePropertiesConnectionFactoryAdapter implements ConnectionFactory {
    /**
     * The delegate connection factory.
     */
    private final ConnectionFactory delegate;

    /**
     * The context entry key or message property name filter.
     */
    private final Filter<String> filter;

    /**
     * the extra static entries to copy.
     */
    private Map<String, String> extraStaticEntries;

    /**
     * The message property encoder.
     */
    private final MessagePropertyEncoder encoder;

    /**
     * {@inheritDoc}
     */
    @Override
    public JMSContext createContext() {
        return delegate.createContext();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JMSContext createContext(int sessionMode) {
        return delegate.createContext(sessionMode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JMSContext createContext(String userName, String password) {
        return delegate.createContext(userName, password);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JMSContext createContext(String userName, String password, int sessionMode) {
        return delegate.createContext(userName, password, sessionMode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Connection createConnection() throws JMSException {
        return new PreservesMessagePropertiesConnectionAdapter(
                delegate.createConnection(), filter, extraStaticEntries, encoder);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Connection createConnection(String userName, String password) throws JMSException {
        return new PreservesMessagePropertiesConnectionAdapter(
                delegate.createConnection(userName, password), filter, extraStaticEntries, encoder);
    }
}
