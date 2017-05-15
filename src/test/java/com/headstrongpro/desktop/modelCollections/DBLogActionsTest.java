package com.headstrongpro.desktop.modelCollections;

import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.model.Log;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by rajmu on 17.05.09.
 */
public class DBLogActionsTest {

    private DBLogActions logActions;

    @Before
    public void setUp() throws Exception {
        logActions = new DBLogActions();
    }

    @Test
    public void getAll() throws Exception {
        List<Log> logs = logActions.getAll();

        assertNotNull(logs);
        assertNotEquals(0, logs.size());
    }

    @Test
    public void getById() throws Exception {
        Log log = logActions.getById(2);

        assertNotNull(log);
        assertEquals("TEST", log.getActionType());
    }

    @Test(expected = ModelSyncException.class)
    public void update() throws Exception {
        logActions.update(null);
    }

    @Test(expected = ModelSyncException.class)
    public void delete() throws Exception {
        logActions.delete(null);
    }

    @Test
    public void getByTable() throws Exception {
        List<Log> logs = logActions.getByTable("test");

        assertNotNull(logs);
        assertNotEquals(0, logs.size());
    }

    @Test
    public void getByTableNonexistent() throws Exception {
        List<Log> logs = logActions.getByTable("nonexistent");

        assertNotNull(logs);
        assertEquals(0, logs.size());
    }
}