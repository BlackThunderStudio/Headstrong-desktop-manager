package com.headstrongpro.desktop.controller;

import com.headstrongpro.desktop.core.exception.ModelSyncException;

/**
 * Refreshable Functional Interface
 */
@FunctionalInterface
public interface Refreshable {
    void refresh() throws ModelSyncException;
}
