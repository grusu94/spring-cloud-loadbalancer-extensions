package com.example.load.balancer.extensions.propagator.jms;

import com.example.load.balancer.extensions.context.ExecutionContext;
import com.example.load.balancer.extensions.propagator.Filter;
import com.example.load.balancer.extensions.propagator.AbstractExecutionContextCopy;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

import javax.jms.*;
import java.io.Serializable;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Session adapter that preserves the {@link ExecutionContext} by copying message propagationProperties from/to the current {@link ExecutionContext} entry pre-filtering property names or entry keys using the provided.
 *
 * @author Nadim Benabdenbi
 */
@Slf4j
public class PreservesMessagePropertiesSessionAdapter extends AbstractExecutionContextCopy<Message>
        implements Session {
    /**
     * the delegate session
     */
    private final Session delegate;

    /**
     * The message property encoder.
     */
    private final MessagePropertyEncoder encoder;

    /**
     * Constructor
     *
     * @param delegate           the delegate session
     * @param filter             the context entry key filter
     * @param extraStaticEntries The extra static entries to copy.
     * @param encoder            the message property encoder.
     */
    public PreservesMessagePropertiesSessionAdapter(@NotNull Session delegate,
                                                    @NotNull Filter<String> filter,
                                                    @NotNull Map<String, String> extraStaticEntries,
                                                    @NotNull MessagePropertyEncoder encoder) {
        super(filter, (message, key, value) -> message.setStringProperty(encoder.encode(key), value), extraStaticEntries);
        this.delegate = delegate;
        this.encoder = encoder;
    }

    /**
     * copies the current execution context to message propagationProperties.
     *
     * @param message the target message.
     * @param <T>     the type of the message
     * @return the message
     * @throws JMSException thrown by the delegate
     */
    private <T extends Message> T copyExecutionContextToMessageProperties(T message) throws JMSException {
        Set<Entry<String, String>> entries = copy(message);
        log.trace("propagated {}", entries);
        return message;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BytesMessage createBytesMessage() throws JMSException {
        return copyExecutionContextToMessageProperties(delegate.createBytesMessage());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MapMessage createMapMessage() throws JMSException {
        return copyExecutionContextToMessageProperties(delegate.createMapMessage());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Message createMessage() throws JMSException {
        return copyExecutionContextToMessageProperties(delegate.createMessage());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectMessage createObjectMessage() throws JMSException {
        return copyExecutionContextToMessageProperties(delegate.createObjectMessage());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectMessage createObjectMessage(Serializable object) throws JMSException {
        return copyExecutionContextToMessageProperties(delegate.createObjectMessage(object));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StreamMessage createStreamMessage() throws JMSException {
        return copyExecutionContextToMessageProperties(delegate.createStreamMessage());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TextMessage createTextMessage() throws JMSException {
        return copyExecutionContextToMessageProperties(delegate.createTextMessage());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TextMessage createTextMessage(String text) throws JMSException {
        return copyExecutionContextToMessageProperties(delegate.createTextMessage(text));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getTransacted() throws JMSException {
        return delegate.getTransacted();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getAcknowledgeMode() throws JMSException {
        return delegate.getAcknowledgeMode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void commit() throws JMSException {
        delegate.commit();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void rollback() throws JMSException {
        delegate.rollback();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() throws JMSException {
        delegate.close();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void recover() throws JMSException {
        delegate.recover();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MessageListener getMessageListener() throws JMSException {
        return delegate.getMessageListener();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMessageListener(MessageListener listener) throws JMSException {
        delegate.setMessageListener(listener instanceof PreservesMessagePropertiesMessageListener ? listener : new PreservesMessagePropertiesMessageListener(listener, getFilter(), encoder));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
        delegate.run();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MessageProducer createProducer(Destination destination) throws JMSException {
        return new PreservesMessagePropertiesMessageProducerAdapter(delegate.createProducer(destination), getFilter(), getExtraStaticEntries(), encoder);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MessageConsumer createConsumer(Destination destination) throws JMSException {
        return new PreservesMessagePropertiesMessageConsumerAdapter(delegate.createConsumer(destination), getFilter(), encoder);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MessageConsumer createConsumer(Destination destination, String messageSelector) throws JMSException {
        return new PreservesMessagePropertiesMessageConsumerAdapter(delegate.createConsumer(destination, messageSelector), getFilter(), encoder);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MessageConsumer createConsumer(Destination destination, String messageSelector, boolean noLocal) throws JMSException {
        return new PreservesMessagePropertiesMessageConsumerAdapter(delegate.createConsumer(destination, messageSelector, noLocal), getFilter(), encoder);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Queue createQueue(String queueName) throws JMSException {
        return delegate.createQueue(queueName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Topic createTopic(String topicName) throws JMSException {
        return delegate.createTopic(topicName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TopicSubscriber createDurableSubscriber(Topic topic, String name) throws JMSException {
        return delegate.createDurableSubscriber(topic, name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TopicSubscriber createDurableSubscriber(Topic topic, String name, String messageSelector, boolean noLocal) throws JMSException {
        return delegate.createDurableSubscriber(topic, name, messageSelector, noLocal);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public QueueBrowser createBrowser(Queue queue) throws JMSException {
        return delegate.createBrowser(queue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public QueueBrowser createBrowser(Queue queue, String messageSelector) throws JMSException {
        return delegate.createBrowser(queue, messageSelector);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TemporaryQueue createTemporaryQueue() throws JMSException {
        return delegate.createTemporaryQueue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TemporaryTopic createTemporaryTopic() throws JMSException {
        return delegate.createTemporaryTopic();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void unsubscribe(String name) throws JMSException {
        delegate.unsubscribe(name);
    }

    @Override
    public MessageConsumer createSharedConsumer(Topic topic, String sharedSubscriptionName) throws JMSException {
        return new PreservesMessagePropertiesMessageConsumerAdapter(delegate.createSharedConsumer(topic, sharedSubscriptionName), getFilter(), encoder);
    }

    @Override
    public MessageConsumer createSharedConsumer(Topic topic, String sharedSubscriptionName, String messageSelector) throws JMSException {
        return new PreservesMessagePropertiesMessageConsumerAdapter(delegate.createSharedConsumer(topic, sharedSubscriptionName, messageSelector), getFilter(), encoder);
    }

    @Override
    public MessageConsumer createDurableConsumer(Topic topic, String name) throws JMSException {
        return new PreservesMessagePropertiesMessageConsumerAdapter(delegate.createDurableConsumer(topic, name), getFilter(), encoder);
    }

    @Override
    public MessageConsumer createDurableConsumer(Topic topic, String name, String messageSelector, boolean noLocal) throws JMSException {
        return new PreservesMessagePropertiesMessageConsumerAdapter(delegate.createDurableConsumer(topic, name, messageSelector, noLocal), getFilter(), encoder);
    }

    @Override
    public MessageConsumer createSharedDurableConsumer(Topic topic, String name) throws JMSException {
        return new PreservesMessagePropertiesMessageConsumerAdapter(delegate.createSharedDurableConsumer(topic, name), getFilter(), encoder);
    }

    @Override
    public MessageConsumer createSharedDurableConsumer(Topic topic, String name, String messageSelector) throws JMSException {
        return new PreservesMessagePropertiesMessageConsumerAdapter(delegate.createSharedDurableConsumer(topic, name, messageSelector), getFilter(), encoder);
    }
}
