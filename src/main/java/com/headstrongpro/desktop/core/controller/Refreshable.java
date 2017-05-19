package com.headstrongpro.desktop.core.controller;

import com.headstrongpro.desktop.core.exception.ModelSyncException;

/**
 * Created by rajmu on 17.05.19.
 */
@FunctionalInterface
public interface Refreshable {
    void refresh() throws ModelSyncException;
}
