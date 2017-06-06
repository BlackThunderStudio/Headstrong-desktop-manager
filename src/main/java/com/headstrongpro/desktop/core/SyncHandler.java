package com.headstrongpro.desktop.core;

/**
 * SyncHandler Functional Interface
 */
@FunctionalInterface
public interface SyncHandler<T> {
    T handle();
}
