package com.headstrongpro.desktop.controller;

import com.headstrongpro.desktop.model.entity.User;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by 1062085 on 31-May-17.
 */
public class UserControllerTest {
    UserController userController;

    @Before
    public void setUp(){
        userController = new UserController();
    }

    @Test
    public void validateUserTest() throws Exception {
        assertTrue(userController.validateUser("test", "test"));
    }

    @Test
    public void getUserTest() throws Exception {
        userController.validateUser("test", "test");
        assertNotNull(userController.getUser());
    }

    @Test
    public void isLoggedInTest() throws Exception {
        userController.validateUser("test", "test");
        assertNotNull(userController.isLoggedIn());
    }

}