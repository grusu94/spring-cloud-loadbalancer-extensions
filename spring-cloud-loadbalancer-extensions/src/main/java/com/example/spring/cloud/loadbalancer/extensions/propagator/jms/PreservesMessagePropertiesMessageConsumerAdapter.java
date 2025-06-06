package com.example.spring.cloud.loadbalancer.extensions.propagator.jms;

import com.example.spring.cloud.loadbalancer.extensions.context.ExecutionContext;
import com.example.spring.cloud.loadbalancer.extensions.propagator.Filter;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;

/**
 * Message consumer adapter that copies message propagationProperties to the current
 * {@link ExecutionContext} pre-filtering property names using the provided.
 */
@Slf4j
public class PreservesMessagePropertiesMessageConsumerAdapter extends AbstractPreservesMessageProperties
        implements MessageConsumer {
    /**
     * the delegate {@link MessageListener}
     */
    private final MessageConsumer delegate;

    /**
     * constructor.
     *
     * @param delegate the delegate message consumer.
     * @param filter   the message property name filter.
     * @param encoder  the message property encoder.
     */
    public PreservesMessagePropertiesMessageConsumerAdapter(@NotNull MessageConsumer delegate,
                                                            @NotNull Filter<String> filter,
                                                            @NotNull MessagePropertyEncoder encoder) {
        super(filter, encoder);
        this.delegate = delegate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMessageSelector() throws JMSException {
        return delegate.getMessageSelector();
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
        delegate.setMessageListener(new PreservesMessagePropertiesMessageListener(listener, getFilter(), getEncoder()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Message receive() throws JMSException {
        Message message = delegate.receive();
        if (!(delegate.getMessageListener() instanceof PreservesMessagePropertiesMessageListener)) {
            copyFromMessage(message);
        }
        return message;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Message receive(long timeout) throws JMSException {
        Message message = delegate.receive(timeout);
        if (!(delegate.getMessageListener() instanceof PreservesMessagePropertiesMessageListener)) {
            copyFromMessage(message);
        }
        return message;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Message receiveNoWait() throws JMSException {
        Message message = delegate.receiveNoWait();
        if (!(delegate.getMessageListener() instanceof PreservesMessagePropertiesMessageListener)) {
            copyFromMessage(message);
        }
        return message;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() throws JMSException {
        delegate.close();
    }
}
