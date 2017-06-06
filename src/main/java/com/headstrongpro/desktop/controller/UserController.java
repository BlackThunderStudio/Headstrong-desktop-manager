package com.headstrongpro.desktop.controller;

import com.headstrongpro.desktop.DbLayer.DBUser;
import com.headstrongpro.desktop.core.TcDigest;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.model.entity.Person;

/**
 * User Controller
 */
public class UserController {

    private static DBUser dbUser = new DBUser();

    private static Person loggedUser;

    private static TcDigest tcDigest = new TcDigest();

    public static boolean validateUser(String username, String password) {
        String safeUsername = tcDigest.encode(username);
        String safePassword = tcDigest.encode(password);
        try {
            loggedUser = dbUser.getByCredentials(safeUsername, safePassword);
        } catch (ModelSyncException ex) {
            System.out.println(ex.getMessage());
        }

        return (loggedUser != null);
    }

    public static Person getUser() {
        return loggedUser;
    }

    public static boolean isLoggedIn() {
        return (loggedUser != null);
    }
}
