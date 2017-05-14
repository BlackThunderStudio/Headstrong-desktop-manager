package com.headstrongpro.desktop.core.controller;

import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.model.Session;
import com.headstrongpro.desktop.modelCollections.DBResources;
import com.headstrongpro.desktop.modelCollections.DBSession;

import java.util.List;

/**
 * Created by rajmu on 17.05.14.
 */
public class SessionController {

    private DBSession dbSession;
    private DBResources dbResources;

    public List<Session> getAllSessions() throws ModelSyncException {
        List<Session> sessions = dbSession.getAll();
        sessions.forEach(e -> {
            try {
                e.setResources(dbResources.getBySessionID(e.getId()));
            } catch (ModelSyncException e1) {
                e1.printStackTrace();
            }
        });
        return sessions;
    }
}
