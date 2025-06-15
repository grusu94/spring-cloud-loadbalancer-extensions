package com.github.grusu94.spring.cloud.loadbalancer.extensions.propagator.jms;

/**
 * Jms Message property encoder/decoder.
 */
public interface MessagePropertyEncoder {
    /**
     * encodes a message property.
     *
     * @param value the message property to encode.
     * @return the encoded message property verifying java identifier naming constraints.
     */
    String encode(String value);

    /**
     * decodes an encoded message property.
     *
     * @param value the encoded message property.
     * @return the decoded message property.
     */
    String decode(String value);
}
