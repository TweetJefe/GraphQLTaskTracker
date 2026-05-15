package com.tasktrackergraphql.exceptions;

public class UniquenessViolation extends RuntimeException {
    public UniquenessViolation(String message) {
        super(message);
    }
}
