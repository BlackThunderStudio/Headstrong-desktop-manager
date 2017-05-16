package com.headstrongpro.desktop.modelCollections;

import com.headstrongpro.desktop.model.Session;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by 1062085 on 16-May-17.
 */
public class DBSessionTest {
    DBSession sessionDAO;

    @Before
    public void setUp() throws Exception {
        sessionDAO = new DBSession();
    }

    @Test
    public void getAll() throws Exception {
        assertNotNull("Sessions object is null", sessionDAO.getAll());
        assertNotEquals("Sessions object is empty", 0, sessionDAO.getAll().size());
    }

    @Test
    public void getById() throws Exception {
        assertEquals("Wrong session name", "The Work Week", sessionDAO.getById(4).getName());
    }

    @Test
    public void create() throws Exception {
        Session newSess = new Session(402, "test session", "how to dummy for sessions");
        int oldSize = sessionDAO.getAll().size();
        sessionDAO.create(newSess);
        assertNotEquals("Session has not been added", oldSize, sessionDAO.getAll().size());
        sessionDAO.delete(newSess);
        assertEquals("Session has not been removed", oldSize, sessionDAO.getAll().size());
    }

    @Test
    public void update() throws Exception {
        Session sess = sessionDAO.getById(4);
        String oldName = sess.getName(), newName = "Test purpose training";
        sess.setName(newName);
        sessionDAO.update(sess);
        assertNotEquals("Name has not been changed", oldName, sessionDAO.getById(4).getName());
        sess.setName(oldName);
        sessionDAO.update(sess);
        assertEquals("Name has not been reverted back", oldName, sessionDAO.getById(4).getName());
    }

}