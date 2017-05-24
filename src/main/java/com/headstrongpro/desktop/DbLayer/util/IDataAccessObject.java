package com.headstrongpro.desktop.DbLayer.util;

import com.headstrongpro.desktop.core.exception.DatabaseOutOfSyncException;
import com.headstrongpro.desktop.core.exception.ModelSyncException;

import java.util.List;

/**
 * Data Access Object Interface
 */
public interface IDataAccessObject<T> {
    List<T> getAll() throws ModelSyncException;

    T getById(int id) throws ModelSyncException;

    T persist(T t) throws ModelSyncException;

    void update(T t) throws ModelSyncException, DatabaseOutOfSyncException;

    void delete(T t) throws ModelSyncException, DatabaseOutOfSyncException;
}
