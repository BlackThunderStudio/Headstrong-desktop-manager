package com.headstrongpro.desktop.modelCollections.util;

import com.headstrongpro.desktop.core.exception.DatabaseOutOfSyncException;
import com.headstrongpro.desktop.core.exception.ModelSyncException;

import java.util.List;

/**
 * Data Access Object Interface
 */
public interface IDataAccessObject<T> {
    List<T> getAll() throws ModelSyncException;

    T getById(int id) throws ModelSyncException;

    T persist(T object) throws ModelSyncException;

    void update(T object) throws ModelSyncException, DatabaseOutOfSyncException;

    void delete(T object) throws ModelSyncException, DatabaseOutOfSyncException;
}
