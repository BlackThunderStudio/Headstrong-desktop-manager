package com.headstrongpro.desktop.view;

import com.headstrongpro.desktop.controller.UserController;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Login View
 */
public class LoginView extends RootLayoutView implements Initializable {
    private final PseudoClass errorClass = PseudoClass.getPseudoClass("error");
    @FXML
    public TextField userNameField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public ProgressIndicator progressIndicator;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        progressIndicator.setVisible(false);
    }

    public void handleLogin() {
        progressIndicator.setVisible(true);
        userNameField.pseudoClassStateChanged(errorClass, false);
        passwordField.pseudoClassStateChanged(errorClass, false);

        try {
            if (UserController.validateUser(userNameField.getText(), passwordField.getText())) {
                mainApp.initLayout();
            }
        } catch (IllegalArgumentException e) {
            passwordField.pseudoClassStateChanged(errorClass, true);
            if (!e.toString().contains("password")) {
                userNameField.pseudoClassStateChanged(errorClass, true);
            }
        } finally {
            progressIndicator.setVisible(false);
        }
    }

}
