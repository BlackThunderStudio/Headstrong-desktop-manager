package com.headstrongpro.desktop.controller;

import com.headstrongpro.desktop.DbLayer.DBLogActions;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.model.Log;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

/**
 * Logs Controller
 */
public class LogsController implements Refreshable {

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
}
