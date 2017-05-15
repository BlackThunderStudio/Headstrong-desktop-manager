package com.headstrongpro.desktop.modelCollections.util;

import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.model.Log;
import com.headstrongpro.desktop.modelCollections.DBLogActions;

/**
 * Created by rajmu on 17.05.09.
 */
public abstract class Synchronizable {

    //TODO: we need to resolve the issue of tracking which employee triggered this method. Possibly storing user session data ofType a singleton?
    protected static Log logChange(int empID, String tableName, int itemID, ActionType type) throws ModelSyncException {
        return new DBLogActions().create(new Log(empID, tableName, itemID, type.getType()));
    }

    protected static Log logChange(String tableName, int itemID, ActionType type) throws ModelSyncException {
        return new DBLogActions().create(new Log(tableName, itemID, type.getType()));
    }

    protected static void logChange(String tableName, ActionType type, int... itemIDs) throws ModelSyncException {
        for (int i : itemIDs){
            new DBLogActions().create(new Log(tableName, i, type.getType()));
        }
    }

    protected abstract boolean verifyIntegrity(int itemID) throws ModelSyncException;
}

