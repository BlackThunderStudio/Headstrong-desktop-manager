package com.headstrongpro.desktop.modelCollections;

import com.headstrongpro.desktop.model.entity.Person;
import com.headstrongpro.desktop.model.entity.User;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Rajmund Staniek on 09-May-17.
 */
public class DBUserTest {
    DBUser userDAO;

    @Before
    public void setUp() throws Exception {
        userDAO = new DBUser();
    }

    @Test
    public void getAll() throws Exception {
        List<Person> users = userDAO.getAll();

        assertNotNull("Users object is null", users);
        assertNotEquals("There are no users", 0, users.size());
    }

    @Test
    public void getById() throws Exception {
        User user = (User) userDAO.getById(1);
        assertEquals("Wrong name for specified user", "Rajmund Staniek", user.getName());
        assertNotEquals("User seems to somehow live in Narnia", "Narnia", user.getCountry());
    }

    @Test
    public void createDelete() throws Exception {
        User newUser = new User("john", "johnny@crash.com", "09778628", "true", "2507893435", "lil marco", "1233", "dummyville", "dummyland", "12121212", "12000");
        int oldSize = userDAO.getAll().size();
        userDAO.create(newUser);
        assertNotEquals("User has not been added", oldSize, userDAO.getAll().size());
        userDAO.delete(newUser);
        assertEquals("User has not been removed", oldSize, userDAO.getAll().size());
    }

    @Test
    public void update() throws Exception {
        User user = (User) userDAO.getById(1);
        String oldName = user.getName(), newName = "Bernard Dominik";
        user.setName(newName);
        userDAO.update(user);
        assertNotEquals("User has not been updated", oldName, userDAO.getById(1).getName());
        user.setName(oldName);
        userDAO.update(user);
        assertEquals("Users has not been reversed back", oldName, userDAO.getById(1).getName());
    }
}