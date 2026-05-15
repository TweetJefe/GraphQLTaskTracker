package com.tasktrackergraphql.exceptions;

public class NullableViolation extends RuntimeException {
    public NullableViolation(String message) {
        super(message);
    }
}

