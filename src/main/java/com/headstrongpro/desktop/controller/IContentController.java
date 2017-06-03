package com.headstrongpro.desktop.controller;

import com.headstrongpro.desktop.core.exception.DatabaseOutOfSyncException;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import javafx.collections.ObservableList;

/**
 * desktop-manager
 * <p>
 * <p>
 * Created by rajmu on 17.06.01.
 */
public interface IContentController<T extends Object> {
    ObservableList<T> getAll() throws ModelSyncException;

    ObservableList<T> searchByPhrase(String input);

    void delete(T t) throws DatabaseOutOfSyncException, ModelSyncException;

    T createNew(T t) throws ModelSyncException;

    void edit(T t) throws DatabaseOutOfSyncException, ModelSyncException;

    T getByID(int id) throws ModelSyncException;
}
