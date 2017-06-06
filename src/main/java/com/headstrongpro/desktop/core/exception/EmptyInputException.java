package com.headstrongpro.desktop.core.exception;

/**
 * EmptyInputException
 */
public class EmptyInputException extends Exception {
    public EmptyInputException() {
    }

    public EmptyInputException(String message) {
        super(message);
    }

    public EmptyInputException(String message, Throwable cause) {
        super(message, cause);
    }
}
