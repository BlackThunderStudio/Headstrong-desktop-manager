package com.headstrongpro.desktop.connection;


import com.headstrongpro.desktop.exception.ModelSyncException;

import java.util.List;

/**
 * Data Access Object Interface
 */
public interface IDataAccessObject<T> {
    List<T> getAll() throws ModelSyncException;

    T getById(int id) throws ModelSyncException;

    T create(T object) throws ModelSyncException;

    void update(T object) throws ModelSyncException;

    void delete(T object) throws ModelSyncException;
}

