package com.example.spring.cloud.loadbalancer.extensions.propagator.jms;

import org.junit.jupiter.api.Test;

import javax.jms.Message;
import javax.jms.MessageProducer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.example.spring.cloud.loadbalancer.extensions.context.ExecutionContextHolder.current;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class PreservesMessagePropertiesMessageProducerAdapterTest {
    private final Set<String> keys = new HashSet<>(List.of("1"));
    private final MessageProducer delegate = mock(MessageProducer.class);
    private final PreservesMessagePropertiesMessageProducerAdapter propagator =
            new PreservesMessagePropertiesMessageProducerAdapter(delegate, keys::contains, new HashMap<>(),
                    new EchoMessagePropertyEncoder());
    private final Message message = mock(Message.class);

    @Test
    public void setDeliveryDelay() throws Exception {
        propagator.setDeliveryDelay(0);
        verify(delegate).setDeliveryDelay(0);
    }

    @Test
    public void getDeliveryDelay() throws Exception {
        propagator.getDeliveryDelay();
        verify(delegate).getDeliveryDelay();
    }

    @Test
    public void setDisableMessageID() throws Exception {
        propagator.setDisableMessageID(true);
        verify(delegate).setDisableMessageID(true);
    }

    @Test
    public void getDisableMessageID() throws Exception {
        propagator.getDisableMessageID();
        verify(delegate).getDisableMessageID();
    }

    @Test
    public void setDisableMessageTimestamp() throws Exception {
        propagator.setDisableMessageTimestamp(true);
        verify(delegate).setDisableMessageTimestamp(true);
    }

    @Test
    public void getDisableMessageTimestamp() throws Exception {
        propagator.getDisableMessageTimestamp();
        verify(delegate).getDisableMessageTimestamp();
    }

    @Test
    public void setDeliveryMode() throws Exception {
        propagator.setDeliveryMode(1);
        verify(delegate).setDeliveryMode(1);
    }

    @Test
    public void getDeliveryMode() throws Exception {
        propagator.getDeliveryMode();
        verify(delegate).getDeliveryMode();
    }

    @Test
    public void setPriority() throws Exception {
        propagator.setPriority(1);
        verify(delegate).setPriority(1);
    }

    @Test
    public void getPriority() throws Exception {
        propagator.getPriority();
        verify(delegate).getPriority();
    }

    @Test
    public void setTimeToLive() throws Exception {
        propagator.setTimeToLive(1);
        verify(delegate).setTimeToLive(1);
    }

    @Test
    public void getTimeToLive() throws Exception {
        propagator.getTimeToLive();
        verify(delegate).getTimeToLive();
    }

    @Test
    public void getDestination() throws Exception {
        propagator.getDestination();
        verify(delegate).getDestination();
    }

    @Test
    public void close() throws Exception {
        propagator.close();
        verify(delegate).close();
    }

    @Test
    public void send() throws Exception {
        keys.forEach(x -> current().put(x, x));
        propagator.send(message);
        verify(delegate).send(message);
        verify(message).setStringProperty("1", "1");
    }

    @Test
    public void send1() throws Exception {
        propagator.send(message, 1, 1, 1);
        verify(delegate).send(message, 1, 1, 1);
    }

    @Test
    public void send2() throws Exception {
        propagator.send(null, message);
        verify(delegate).send(null, message);
    }

    @Test
    public void send3() throws Exception {
        propagator.send(null, message, 1, 1, 1);
        verify(delegate).send(null, message, 1, 1, 1);
    }

    @Test
    public void send4() throws Exception {
        propagator.send(message, null);
        verify(delegate).send(message, null);
    }

    @Test
    public void send5() throws Exception {
        propagator.send(null, message, null);
        verify(delegate).send(null, message, null);
    }

    @Test
    public void send6() throws Exception {
        propagator.send(message, 0, 0, 0, null);
        verify(delegate).send(message, 0, 0, 0, null);
    }

    @Test
    public void send7() throws Exception {
        propagator.send(null, message, 0, 0, 0, null);
        verify(delegate).send(null, message, 0, 0, 0, null);
    }
}