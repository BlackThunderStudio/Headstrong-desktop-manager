package com.headstrongpro.desktop.controller;

import com.headstrongpro.desktop.core.exception.DatabaseOutOfSyncException;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import javafx.collections.ObservableList;

/**
 * Content Controller Interface
 */
public interface IContentController<T> {

    ObservableList<T> getAll() throws ModelSyncException;

    ObservableList<T> searchByPhrase(String input);

    void delete(T t) throws DatabaseOutOfSyncException, ModelSyncException;

    T createNew(T t) throws ModelSyncException;

    void edit(T t) throws DatabaseOutOfSyncException, ModelSyncException;

    T getById(int id) throws ModelSyncException;
}
