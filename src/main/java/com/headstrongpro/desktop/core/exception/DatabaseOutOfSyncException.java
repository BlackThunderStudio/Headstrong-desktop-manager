package com.headstrongpro.desktop.core.exception;

/**
 * Created by rajmu on 17.05.15.
 */
public class DatabaseOutOfSyncException extends Exception {
    public DatabaseOutOfSyncException() {
        super("The database contains newer data, therefore this operation would disintegrate the system. Ain't gonna happen!");
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
