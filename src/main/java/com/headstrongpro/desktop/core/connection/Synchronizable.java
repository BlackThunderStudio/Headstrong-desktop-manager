package com.headstrongpro.desktop.core.connection;

import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.model.Log;
import com.headstrongpro.desktop.modelCollections.DBLogActions;

/**
 * Created by rajmu on 17.05.09.
 */
public abstract class Synchronizable {

    //TODO: we need to resolve the issue of tracking which employee triggered this method. Possibly storing user session data as a singleton?
    protected static Log logChange(int empID, String tableName, int itemID, String actionType) throws ModelSyncException {
        return new DBLogActions().create(new Log(empID, tableName, itemID, actionType));
    }

    protected abstract boolean verifyIntegrity(int itemID) throws ModelSyncException;
}
