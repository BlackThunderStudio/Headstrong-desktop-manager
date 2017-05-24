package com.headstrongpro.desktop.core.controller;

import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.model.entity.Person;
import com.headstrongpro.desktop.DbLayer.DBUser;

/**
 * User Controller
 */
public class UserController {

    private DBUser dbUser = new DBUser();

    private static Person loggedUser;

    private boolean validateUser(String username, String password) throws ModelSyncException {
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

    static boolean isLoggedIn() {
        return (loggedUser != null);
    }
}
