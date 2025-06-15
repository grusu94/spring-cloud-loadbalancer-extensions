package com.github.grusu94.spring.cloud.loadbalancer.extensions.propagator.jms;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SimpleMessagePropertyEncoderTest {
    SimpleMessagePropertyEncoder simple = new SimpleMessagePropertyEncoder();

    @Test()
    public void should_fail_encoding_unsupported_value() {
        assertThrows(IllegalArgumentException.class,
                () -> simple.encode("*"));
    }

    @Test()
    public void should_fail_decoding_unsupported_value1() {
        assertThrows(IllegalArgumentException.class,
                () -> simple.decode("" + (char) ('a' - 1)));
    }

    @Test()
    public void should_fail_decoding_unsupported_value2() {
        assertThrows(IllegalArgumentException.class,
                () -> simple.decode("" + (char) ('A' - 1)));
    }

    @Test()
    public void should_fail_decoding_unsupported_value3() {
        assertThrows(IllegalArgumentException.class,
                () -> simple.decode("" + (char) ('0' - 1)));
    }

    @Test()
    public void should_fail_decoding_unsupported_value4() {
        assertThrows(IllegalArgumentException.class,
                () -> simple.decode("" + (char) ('z' + 1)));
    }

    @Test()
    public void should_fail_decoding_unsupported_value5() {
        assertThrows(IllegalArgumentException.class,
                () -> simple.decode("" + (char) ('Z' + 1)));
    }

    @Test()
    public void should_fail_decoding_unsupported_value6() {
        assertThrows(IllegalArgumentException.class,
                () -> simple.decode("" + (char) ('9' + 1)));
    }

    @Test
    public void encode_decode_test() {
        String expected = "azAZ09Hello_-world3";
        String encoded = simple.encode(expected);
        String actual = simple.decode(encoded);
        assertThat(encoded).isEqualTo("azAZ09Hello_$world3");
        assertThat(actual).isEqualTo(expected);
        assertThat(isValidJavaIdentifier(encoded)).isTrue();
    }

    private boolean isValidJavaIdentifier(String s) {
        if (s == null || s.isEmpty()) {
            return false;
        }
        char[] c = s.toCharArray();
        if (!Character.isJavaIdentifierStart(c[0])) {
            return false;
        }
        for (int i = 1; i < c.length; i++) {
            if (!Character.isJavaIdentifierPart(c[i])) {
                return false;
            }
        }
        return true;
    }
}