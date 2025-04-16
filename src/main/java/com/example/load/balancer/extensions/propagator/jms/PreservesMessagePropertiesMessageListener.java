package com.example.load.balancer.extensions.propagator.jms;

import com.example.load.balancer.extensions.context.ExecutionContext;
import com.example.load.balancer.extensions.propagator.Filter;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

import javax.jms.Message;
import javax.jms.MessageListener;

/**
 * Message listener adapter that copies message propagationProperties to the current {@link ExecutionContext} pre-filtering property names using the provided.
 *
 * @author Nadim Benabdenbi
 */
@Slf4j
public class PreservesMessagePropertiesMessageListener extends AbstractPreservesMessageProperties implements MessageListener {
    /**
     * the delegate {@link MessageListener}
     */
    private final MessageListener delegate;

    /**
     * constructor.
     *
     * @param delegate the delegate {@link MessageListener}.
     * @param filter   the message property name filter.
     * @param encoder  the message property encoder.
     */
    public PreservesMessagePropertiesMessageListener(@NotNull MessageListener delegate,
                                                     @NotNull Filter<String> filter,
                                                     @NotNull MessagePropertyEncoder encoder) {
        super(filter, encoder);
        this.delegate = delegate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onMessage(Message message) {
        delegate.onMessage(copyFromMessage(message));
    }
}
