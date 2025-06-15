package com.github.grusu94.spring.cloud.loadbalancer.extensions.propagator.jms;

/**
 * Accepts property name within [(a-z)(A-Z)-_]* permuting '-' with '$'
 */
public class SimpleMessagePropertyEncoder implements MessagePropertyEncoder {

    @Override
    public String encode(String value) {
        return process(value, '-', '$');
    }

    @Override
    public String decode(String value) {
        return process(value, '$', '-');
    }

    public String process(String value, char oldChar, char newChar) {
        char[] chars = value.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == oldChar) {
                chars[i] = newChar;
            } else if (!isJavaFieldChar(chars[i])) {
                throw new IllegalArgumentException("invalid property name '" + value + "'");
            }
        }
        return new String(chars);
    }

    private boolean isJavaFieldChar(char aChar) {
        return (aChar >= 'a' && aChar <= 'z') ||
                (aChar >= 'A' && aChar <= 'Z') ||
                (aChar >= '0' && aChar <= '9') ||
                aChar == '_';
    }
}
