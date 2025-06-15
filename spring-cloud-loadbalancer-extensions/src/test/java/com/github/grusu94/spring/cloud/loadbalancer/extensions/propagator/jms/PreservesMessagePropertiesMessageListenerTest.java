package com.github.grusu94.spring.cloud.loadbalancer.extensions.propagator.jms;

import org.junit.jupiter.api.Test;

import javax.jms.Message;
import javax.jms.MessageListener;
import java.util.Collections;

import static org.mockito.Mockito.*;

public class PreservesMessagePropertiesMessageListenerTest {
    private final MessageListener delegate = mock(MessageListener.class);
    private final PreservesMessagePropertiesMessageListener listener =
            new PreservesMessagePropertiesMessageListener(delegate, o -> false, new EchoMessagePropertyEncoder());
    private final Message message = mock(Message.class);

    @Test
    public void onMessage() throws Exception {
        when(message.getPropertyNames()).thenReturn(Collections.emptyEnumeration());
        listener.onMessage(message);
        verify(delegate).onMessage(message);
    }

}