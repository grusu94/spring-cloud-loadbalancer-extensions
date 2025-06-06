package com.example.spring.cloud.loadbalancer.extensions.propagator.jms;

import org.junit.jupiter.api.Test;

import javax.jms.MessageConsumer;
import javax.jms.MessageListener;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PreservesMessagePropertiesMessageConsumerAdapterTest {
    private final MessageConsumer delegate = mock(MessageConsumer.class);
    private final PreservesMessagePropertiesMessageConsumerAdapter propagator =
            new PreservesMessagePropertiesMessageConsumerAdapter(delegate, null,
                    new EchoMessagePropertyEncoder());

    @Test
    public void getMessageSelector() throws Exception {
        propagator.getMessageSelector();
        verify(delegate).getMessageSelector();
    }

    @Test
    public void getMessageListener() throws Exception {
        propagator.getMessageListener();
        verify(delegate).getMessageListener();
    }

    @Test
    public void setMessageListener() throws Exception {
        propagator.setMessageListener(null);
        verify(delegate).setMessageListener(any(PreservesMessagePropertiesMessageListener.class));
    }

    @Test
    public void receive() throws Exception {
        when(delegate.getMessageListener()).thenReturn(mock(PreservesMessagePropertiesMessageListener.class));
        propagator.receive();
        verify(delegate).receive();
        reset(delegate);
        when(delegate.getMessageListener()).thenReturn(mock(MessageListener.class));
        propagator.receive();
        verify(delegate).receive();
    }

    @Test
    public void receive1() throws Exception {
        when(delegate.getMessageListener()).thenReturn(mock(PreservesMessagePropertiesMessageListener.class));
        propagator.receive(0);
        verify(delegate).receive(0);
        reset(delegate);
        when(delegate.getMessageListener()).thenReturn(mock(MessageListener.class));
        propagator.receive(0);
        verify(delegate).receive(0);
    }

    @Test
    public void receiveNoWait() throws Exception {
        when(delegate.getMessageListener()).thenReturn(mock(PreservesMessagePropertiesMessageListener.class));
        propagator.receiveNoWait();
        verify(delegate).receiveNoWait();
        reset(delegate);
        when(delegate.getMessageListener()).thenReturn(mock(MessageListener.class));
        propagator.receiveNoWait();
        verify(delegate).receiveNoWait();
    }

    @Test
    public void close() throws Exception {
        propagator.close();
        verify(delegate).close();
    }
}
