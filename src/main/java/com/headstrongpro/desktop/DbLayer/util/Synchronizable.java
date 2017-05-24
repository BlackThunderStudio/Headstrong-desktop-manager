package com.headstrongpro.desktop.DbLayer.util;

import com.headstrongpro.desktop.core.connection.DBConnect;
import com.headstrongpro.desktop.core.exception.ConnectionException;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.model.Log;
import com.headstrongpro.desktop.DbLayer.DBLogActions;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;

/**
 * Created by rajmu on 17.05.09.
 */
public abstract class Synchronizable {

    //TODO: we need to resolve the issue of tracking which employee triggered this method. Possibly storing user session data ofType a singleton?
    protected static Log logChange(int empID, String tableName, int itemID, ActionType type) throws ModelSyncException {
        return new DBLogActions().persist(new Log(empID, tableName, itemID, type.getType()));
    }

    protected static Log logChange(String tableName, int itemID, ActionType type) throws ModelSyncException {
        return new DBLogActions().persist(new Log(tableName, itemID, type.getType()));
    }

    protected static void logChange(String tableName, ActionType type, int... itemIDs) throws ModelSyncException {
        for (int i : itemIDs) {
            new DBLogActions().persist(new Log(tableName, i, type.getType()));
        }
    }

    protected static Date setTimestamp() {
        ResultSet resultSet = null;
        Date timestamp = new Date(Calendar.getInstance().getTimeInMillis());
        try {
            DBConnect connect = new DBConnect();
            resultSet = connect.getFromDataBase("SELECT CURRENT_TIMESTAMP");
            resultSet.next();
            timestamp = new Date(resultSet.getTimestamp(1).getTime());
        } catch (ConnectionException | SQLException e) {
            e.printStackTrace();
        }
        return timestamp;
    }

    protected abstract boolean verifyIntegrity(int itemID) throws ModelSyncException;

    protected boolean verifyIntegrity(int itemID, Date timestamp, String tableName) throws ModelSyncException {
        List<Log> logs = new DBLogActions().getByTable(tableName);
        if (logs.size() == 0) return true;
        else {
            if (logs.stream().anyMatch(e -> e.getItemID() == itemID)) {
                long millis = logs.stream()
                        .filter(e -> e.getItemID() == itemID)
                        .sorted(Comparator.comparingLong(e -> e.getDate().getTime()))
                        .findFirst().get().getDate().getTime(); //Retrieves the most recent change from the list
                return millis < timestamp.getTime();
            } else return true;
        }
    }
}

