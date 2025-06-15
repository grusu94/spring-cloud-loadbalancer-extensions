package com.github.grusu94.spring.cloud.loadbalancer.extensions.propagator.jms;

import org.junit.jupiter.api.Test;

import javax.jms.Connection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class PreservesMessagePropertiesConnectionAdapterTest {
    private final Connection delegate = mock(Connection.class);
    private final PreservesMessagePropertiesConnectionAdapter propagator =
            new PreservesMessagePropertiesConnectionAdapter(delegate, null, null,
                    new EchoMessagePropertyEncoder());

    @Test
    public void createSession() throws Exception {
        assertThat(propagator.createSession().getClass(), equalTo(PreservesMessagePropertiesSessionAdapter.class));
        verify(delegate).createSession();
    }

    @Test
    public void createSession1() throws Exception {
        assertThat(propagator.createSession(0).getClass(), equalTo(PreservesMessagePropertiesSessionAdapter.class));
        verify(delegate).createSession(0);
    }

    @Test
    public void createSession2() throws Exception {
        assertThat(propagator.createSession(true, 1).getClass(),
                equalTo(PreservesMessagePropertiesSessionAdapter.class));
        verify(delegate).createSession(true, 1);
    }

    @Test
    public void getClientID() throws Exception {
        propagator.getClientID();
        verify(delegate).getClientID();
    }

    @Test
    public void setClientID() throws Exception {
        propagator.setClientID(null);
        verify(delegate).setClientID(null);
    }

    @Test
    public void getMetaData() throws Exception {
        propagator.getMetaData();
        verify(delegate).getMetaData();
    }

    @Test
    public void getExceptionListener() throws Exception {
        propagator.getExceptionListener();
        verify(delegate).getExceptionListener();
    }

    @Test
    public void setExceptionListener() throws Exception {
        propagator.setExceptionListener(null);
        verify(delegate).setExceptionListener(null);
    }

    @Test
    public void start() throws Exception {
        propagator.start();
        verify(delegate).start();
    }

    @Test
    public void stop() throws Exception {
        propagator.stop();
        verify(delegate).stop();
    }

    @Test
    public void close() throws Exception {
        propagator.close();
        verify(delegate).close();
    }

    @Test
    public void createConnectionConsumer() throws Exception {
        propagator.createConnectionConsumer(null, null, null, 0);
        verify(delegate).createConnectionConsumer(null, null, null, 0);
    }

    @Test
    public void createDurableConnectionConsumer() throws Exception {
        propagator.createDurableConnectionConsumer(null, null, null,
                null, 0);
        verify(delegate).createDurableConnectionConsumer(null, null, null, null, 0);
    }

    @Test
    public void createSharedConnectionConsumer() throws Exception {
        propagator.createSharedConnectionConsumer(null, null, null,
                null, 0);
        verify(delegate).createSharedConnectionConsumer(null, null, null, null, 0);
    }

    @Test
    public void createSharedDurableConnectionConsumer() throws Exception {
        propagator.createSharedDurableConnectionConsumer(null, null, null,
                null, 0);
        verify(delegate).createSharedDurableConnectionConsumer(null, null, null, null, 0);
    }
}
