package com.headstrongpro.desktop.controller;

import com.headstrongpro.desktop.DbLayer.DBLogActions;
import com.headstrongpro.desktop.core.exception.DatabaseOutOfSyncException;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.model.Log;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

/**
 * Logs Controller
 */
public class LogsController implements Refreshable, IContentController<Log> {

    private List<Log> logs;
    private DBLogActions dbLogActions;

    public LogsController() {
        dbLogActions = new DBLogActions();
        logs = new ArrayList<>();
    }

    @Concurrent
    @Override
    public void refresh() throws ModelSyncException {
        logs = dbLogActions.getAll();
    }

    public ObservableList<Log> getAll() throws ModelSyncException {
        refresh();
        return FXCollections.observableArrayList(logs);
    }

    @Override
    public ObservableList<Log> searchByPhrase(String input) {
        return null;
    }

    @Override
    public void delete(Log log) throws DatabaseOutOfSyncException, ModelSyncException {

    }

    @Override
    public Log createNew(Log log) throws ModelSyncException {
        return null;
    }

    @Override
    public void edit(Log log) throws DatabaseOutOfSyncException, ModelSyncException {

    }

    @Override
    public Log getById(int id) throws ModelSyncException {
        return null;
    }
}
