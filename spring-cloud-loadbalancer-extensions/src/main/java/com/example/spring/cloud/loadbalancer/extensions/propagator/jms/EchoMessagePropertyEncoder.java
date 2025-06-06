package com.example.spring.cloud.loadbalancer.extensions.propagator.jms;

/**
 * Echo encoder.
 */
public class EchoMessagePropertyEncoder implements MessagePropertyEncoder {
    @Override
    public String encode(String value) {
        return value;
    }

    @Override
    public String decode(String value) {
        return value;
    }
}
