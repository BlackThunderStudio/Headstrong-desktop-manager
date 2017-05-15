package com.headstrongpro.desktop.modelCollections.util;

import com.headstrongpro.desktop.core.exception.ModelSyncException;

import java.util.List;

/**
 * Created by rajmu on 17.04.06.
 */
public interface IDataAccessObject<T> {
    List<T> getAll() throws ModelSyncException;

    T getById(int id) throws ModelSyncException;

    T create(T object) throws ModelSyncException;

    void update(T object) throws ModelSyncException;

    void delete(T object) throws ModelSyncException;
}
