package com.headstrongpro.desktop.core.exception;

/**
 * Created by rajmu on 17.05.25.
 */
public class SyncException extends Exception {
    public SyncException() {
        super();
    }

    public SyncException(String message) {
        super(message);
    }

    public SyncException(String message, Throwable cause) {
        super(message, cause);
    }

    public SyncException(Throwable cause) {
        super(cause);
    }

    protected SyncException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
