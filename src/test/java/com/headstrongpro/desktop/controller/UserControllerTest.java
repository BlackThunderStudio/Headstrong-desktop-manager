package com.headstrongpro.desktop.controller;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by 1062085 on 31-May-17.
 */
public class UserControllerTest {

    @Test
    public void validateUserTest() throws Exception {
        assertTrue(UserController.validateUser("test", "test"));
    }

    @Test
    public void getUserTest() throws Exception {
        UserController.validateUser("test", "test");
        assertNotNull(UserController.getUser());
    }

    @Test
    public void isLoggedInTest() throws Exception {
        UserController.validateUser("test", "test");
        assertNotNull(UserController.isLoggedIn());
    }

}