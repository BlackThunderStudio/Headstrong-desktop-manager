package com.headstrongpro.desktop.core.exception;

/**
 * DatabaseOutOfSyncException
 */
public class DatabaseOutOfSyncException extends Exception {
    public DatabaseOutOfSyncException() {
        super("The database contains newer data, therefore this operation would disintegrate the system. Ain'data gonna happen!");
    }

    public DatabaseOutOfSyncException(String message) {
        super(message);
    }

    public DatabaseOutOfSyncException(String message, Throwable cause) {
        super(message, cause);
    }

    public DatabaseOutOfSyncException(Throwable cause) {
        super(cause);
    }

    protected DatabaseOutOfSyncException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
