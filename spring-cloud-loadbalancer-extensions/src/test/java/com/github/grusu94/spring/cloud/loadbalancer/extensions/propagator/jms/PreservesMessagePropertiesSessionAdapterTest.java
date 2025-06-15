package com.github.grusu94.spring.cloud.loadbalancer.extensions.propagator.jms;

import org.junit.jupiter.api.Test;

import jakarta.jms.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.github.grusu94.spring.cloud.loadbalancer.extensions.context.ExecutionContextHolder.current;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PreservesMessagePropertiesSessionAdapterTest {
    private final Set<String> keys = new HashSet<>(List.of("1"));
    private final Session delegate = mock(Session.class);
    private final PreservesMessagePropertiesSessionAdapter propagator =
            new PreservesMessagePropertiesSessionAdapter(delegate, keys::contains, new HashMap<>(),
                    new EchoMessagePropertyEncoder());

    @Test
    public void createBytesMessage() throws Exception {
        BytesMessage message = mock(BytesMessage.class);
        keys.forEach(x -> current().put(x, x));
        when(delegate.createBytesMessage()).thenReturn(message);
        propagator.createBytesMessage();
        verify(delegate).createBytesMessage();
        verify(message).setStringProperty("1", "1");
    }

    @Test
    public void createMapMessage() throws Exception {
        MapMessage message = mock(MapMessage.class);
        keys.forEach(x -> current().put(x, x));
        when(delegate.createMapMessage()).thenReturn(message);
        propagator.createMapMessage();
        verify(delegate).createMapMessage();
        verify(message).setStringProperty("1", "1");
    }

    @Test
    public void createMessage() throws Exception {
        Message message = mock(Message.class);
        keys.forEach(x -> current().put(x, x));
        when(delegate.createMessage()).thenReturn(message);
        propagator.createMessage();
        verify(delegate).createMessage();
        verify(message).setStringProperty("1", "1");
    }

    @Test
    public void createObjectMessage() throws Exception {
        ObjectMessage message = mock(ObjectMessage.class);
        keys.forEach(x -> current().put(x, x));
        when(delegate.createObjectMessage()).thenReturn(message);
        propagator.createObjectMessage();
        verify(delegate).createObjectMessage();
        verify(message).setStringProperty("1", "1");
    }

    @Test
    public void createObjectMessage1() throws Exception {
        ObjectMessage message = mock(ObjectMessage.class);
        keys.forEach(x -> current().put(x, x));
        when(delegate.createObjectMessage(null)).thenReturn(message);
        propagator.createObjectMessage(null);
        verify(delegate).createObjectMessage(null);
        verify(message).setStringProperty("1", "1");
    }

    @Test
    public void createStreamMessage() throws Exception {
        StreamMessage message = mock(StreamMessage.class);
        keys.forEach(x -> current().put(x, x));
        when(delegate.createStreamMessage()).thenReturn(message);
        propagator.createStreamMessage();
        verify(delegate).createStreamMessage();
        verify(message).setStringProperty("1", "1");
    }

    @Test
    public void createTextMessage() throws Exception {
        TextMessage message = mock(TextMessage.class);
        keys.forEach(x -> current().put(x, x));
        when(delegate.createTextMessage()).thenReturn(message);
        propagator.createTextMessage();
        verify(delegate).createTextMessage();
        verify(message).setStringProperty("1", "1");

    }

    @Test
    public void createTextMessage1() throws Exception {
        TextMessage message = mock(TextMessage.class);
        keys.forEach(x -> current().put(x, x));
        when(delegate.createTextMessage(null)).thenReturn(message);
        propagator.createTextMessage(null);
        verify(delegate).createTextMessage(null);
        verify(message).setStringProperty("1", "1");
    }

    @Test
    public void getTransacted() throws Exception {
        propagator.getTransacted();
        verify(delegate).getTransacted();
    }

    @Test
    public void getAcknowledgeMode() throws Exception {
        propagator.getAcknowledgeMode();
        verify(delegate).getAcknowledgeMode();
    }

    @Test
    public void commit() throws Exception {
        propagator.commit();
        verify(delegate).commit();
    }

    @Test
    public void rollback() throws Exception {
        propagator.rollback();
        verify(delegate).rollback();
    }

    @Test
    public void close() throws Exception {
        propagator.close();
        verify(delegate).close();
    }

    @Test
    public void recover() throws Exception {
        propagator.recover();
        verify(delegate).recover();
    }

    @Test
    public void getMessageListener() throws Exception {
        propagator.getMessageListener();
        verify(delegate).getMessageListener();
    }

    @Test
    public void setMessageListener() throws Exception {
        propagator.setMessageListener(mock(MessageListener.class));
        verify(delegate).setMessageListener(any(PreservesMessagePropertiesMessageListener.class));
        reset(delegate);
        propagator.setMessageListener(new PreservesMessagePropertiesMessageListener(mock(MessageListener.class),
                null, new EchoMessagePropertyEncoder()));
        verify(delegate).setMessageListener(any(PreservesMessagePropertiesMessageListener.class));
    }

    @Test
    public void run() {
        propagator.run();
        verify(delegate).run();
    }

    @Test
    public void createProducer() throws Exception {
        propagator.createProducer(null);
        verify(delegate).createProducer(null);
    }

    @Test
    public void createConsumer() throws Exception {
        propagator.createConsumer(null);
        verify(delegate).createConsumer(null);
    }

    @Test
    public void createConsumer1() throws Exception {
        propagator.createConsumer(null, null);
        verify(delegate).createConsumer(null, null);
    }

    @Test
    public void createConsumer2() throws Exception {
        propagator.createConsumer(null, null, true);
        verify(delegate).createConsumer(null, null, true);
    }

    @Test
    public void createQueue() throws Exception {
        propagator.createQueue(null);
        verify(delegate).createQueue(null);
    }

    @Test
    public void createTopic() throws Exception {
        propagator.createTopic(null);
        verify(delegate).createTopic(null);
    }

    @Test
    public void createDurableSubscriber() throws Exception {
        propagator.createDurableSubscriber(null, null);
        verify(delegate).createDurableSubscriber(null, null);
    }

    @Test
    public void createDurableSubscriber1() throws Exception {
        propagator.createDurableSubscriber(null, null, null, true);
        verify(delegate).createDurableSubscriber(null, null, null, true);
    }

    @Test
    public void createBrowser() throws Exception {
        propagator.createBrowser(null);
        verify(delegate).createBrowser(null);
    }

    @Test
    public void createBrowser1() throws Exception {
        propagator.createBrowser(null, null);
        verify(delegate).createBrowser(null, null);
    }

    @Test
    public void createTemporaryQueue() throws Exception {
        propagator.createTemporaryQueue();
        verify(delegate).createTemporaryQueue();
    }

    @Test
    public void createTemporaryTopic() throws Exception {
        propagator.createTemporaryTopic();
        verify(delegate).createTemporaryTopic();
    }

    @Test
    public void unsubscribe() throws Exception {
        propagator.unsubscribe(null);
        verify(delegate).unsubscribe(null);
    }

    @Test
    public void createSharedConsumer2() throws Exception {
        propagator.createSharedConsumer(null, null);
        verify(delegate).createSharedConsumer(null, null);
    }

    @Test
    public void createSharedConsumer3() throws Exception {
        propagator.createSharedConsumer(null, null, null);
        verify(delegate).createSharedConsumer(null, null, null);
    }

    @Test
    public void createDurableConsumer2() throws Exception {
        propagator.createDurableConsumer(null, null);
        verify(delegate).createDurableConsumer(null, null);
    }

    @Test
    public void createDurableConsumer4() throws Exception {
        propagator.createDurableConsumer(null, null, null, true);
        verify(delegate).createDurableConsumer(null, null, null, true);
    }

    @Test
    public void createSharedDurableConsumer2() throws Exception {
        propagator.createSharedDurableConsumer(null, null);
        verify(delegate).createSharedDurableConsumer(null, null);
    }

    @Test
    public void createSharedDurableConsumer3() throws Exception {
        propagator.createSharedDurableConsumer(null, null, null);
        verify(delegate).createSharedDurableConsumer(null, null, null);
    }
}