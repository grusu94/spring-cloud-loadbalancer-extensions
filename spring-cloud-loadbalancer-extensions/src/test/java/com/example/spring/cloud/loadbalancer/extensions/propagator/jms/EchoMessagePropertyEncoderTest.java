package com.example.spring.cloud.loadbalancer.extensions.propagator.jms;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EchoMessagePropertyEncoderTest {

    private final EchoMessagePropertyEncoder passthrough = new EchoMessagePropertyEncoder();

    @Test
    public void test() {
        String expected = "hello-world";
        String encoded = passthrough.encode(expected);
        String actual = passthrough.decode(encoded);
        assertThat(actual).isEqualTo("hello-world");
        assertThat(actual).isEqualTo(expected);
    }
}