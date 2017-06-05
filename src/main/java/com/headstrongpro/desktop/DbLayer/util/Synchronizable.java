package com.headstrongpro.desktop.DbLayer.util;

import com.headstrongpro.desktop.DbLayer.DBLogActions;
import com.headstrongpro.desktop.controller.UserController;
import com.headstrongpro.desktop.core.connection.DBConnect;
import com.headstrongpro.desktop.core.exception.ConnectionException;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.model.Log;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;

/**
 * Synchronizable
 */
public abstract class Synchronizable {

    protected Date timestamp;

    //TODO: we need to resolve the issue of tracking which employee triggered this method. Possibly storing user session data ofType a singleton?
    protected static Log logChange(int empID, String tableName, int itemID, ActionType type) throws ModelSyncException {
        return new DBLogActions().persist(new Log(empID, tableName, itemID, type.getType()));
    }

    protected static Log logChange(String tableName, int itemID, ActionType type) throws ModelSyncException {
        return new DBLogActions().persist(
                new Log(UserController.isLoggedIn() ? UserController.getUser().getId() : -1, tableName, itemID, type.getType()));
    }

    protected static void logChange(String tableName, ActionType type, int... itemIDs) throws ModelSyncException {
        int empID = UserController.isLoggedIn() ? UserController.getUser().getId() : -1;
        for (int i : itemIDs) {
            new DBLogActions().persist(new Log(empID, tableName, i, type.getType()));
        }
    }

    protected void updateTimestampLocal() {
        timestamp = new Date(Calendar.getInstance().getTimeInMillis());
    }

    protected Date setTimestamp() {
        ResultSet resultSet;
        Date timestamp = new Date(Calendar.getInstance().getTimeInMillis());
        try {
            DBConnect connect = new DBConnect();
            resultSet = connect.getFromDataBase("SELECT CURRENT_TIMESTAMP");
            resultSet.next();
            timestamp = new Date(resultSet.getTimestamp(1).getTime());
        } catch (ConnectionException | SQLException e) {
            e.printStackTrace();
        }
        this.timestamp = timestamp;
        return timestamp;
    }

    protected abstract boolean verifyIntegrity(int itemID) throws ModelSyncException;

    protected boolean verifyIntegrity(int itemID, Date timestamp, String tableName) throws ModelSyncException {
        List<Log> logs = new DBLogActions().getByTable(tableName);
        if (logs.size() == 0) return true;
        else {
            if (logs.stream().anyMatch(e -> e.getItemId() == itemID)) {
                long millis = logs.stream()
                        .filter(e -> e.getItemId() == itemID)
                        .sorted(Comparator.comparingLong(e -> e.getDate().getTime()))
                        .findFirst().orElseGet(null).getDate().getTime(); //Retrieves the most recent change from the list
                return millis < timestamp.getTime();
            } else return true;
        }
    }
}

