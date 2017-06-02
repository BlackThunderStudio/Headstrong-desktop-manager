package com.headstrongpro.desktop.core;

/**
 * desktop-manager
 * <p>
 * <p>
 * Created by rajmu on 17.06.01.
 */
@FunctionalInterface
public interface SyncHandler<T> {
    T handle();
}
