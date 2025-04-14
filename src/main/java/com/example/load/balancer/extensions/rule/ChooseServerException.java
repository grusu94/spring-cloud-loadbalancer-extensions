package com.example.load.balancer.extensions.rule;

/**
 * Thrown when the rule can not choose any server.
 */
public class ChooseServerException extends RuntimeException {
    public ChooseServerException(String message) {
        super(message);
    }
}
