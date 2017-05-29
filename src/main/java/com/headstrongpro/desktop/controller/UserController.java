package com.headstrongpro.desktop.controller;

import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.model.entity.Person;
import com.headstrongpro.desktop.DbLayer.DBUser;

/**
 * User Controller
 */
public class UserController {

    private static DBUser dbUser = new DBUser();

    private static Person loggedUser;

    public static boolean validateUser(String username, String password) {
        try {
            loggedUser = dbUser.getByCredentials(username, password);
        } catch (ModelSyncException ex) {
            System.out.println(ex.getMessage());
        }

        return (loggedUser != null);
    }

    static Person getUser() {
        return loggedUser;
    }

    public static boolean isLoggedIn() {
        return (loggedUser != null);
    }
}
