package com.example.spring.cloud.loadbalancer.extensions.propagator.jms;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import javax.jms.ConnectionFactory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class PreservesMessagePropertiesConnectionFactoryAdapterTest {
    private final ConnectionFactory delegate = mock(ConnectionFactory.class);
    private final PreservesMessagePropertiesConnectionFactoryAdapter propagator =
            new PreservesMessagePropertiesConnectionFactoryAdapter(delegate, null, null,
                    new EchoMessagePropertyEncoder());

    @Test
    public void createContext() {
        propagator.createContext();
        verify(delegate).createContext();
    }

    @Test
    public void createContext1() {
        propagator.createContext(0);
        verify(delegate).createContext(0);
    }

    @Test
    public void createContext2() {
        propagator.createContext(null, null);
        verify(delegate).createContext(null, null);
    }

    @Test
    public void createContext3() {
        propagator.createContext(null, null, 0);
        verify(delegate).createContext(null, null, 0);
    }

    @Test
    public void createConnection() throws Exception {
        assertThat(propagator.createConnection().getClass(),
                Matchers.equalTo(PreservesMessagePropertiesConnectionAdapter.class));
        verify(delegate).createConnection();
    }

    @Test
    public void createConnectionWithCredential() throws Exception {
        assertThat(propagator.createConnection(null, null).getClass(),
                Matchers.equalTo(PreservesMessagePropertiesConnectionAdapter.class));
        verify(delegate).createConnection(null, null);
    }

}