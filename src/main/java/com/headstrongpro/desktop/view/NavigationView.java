package com.headstrongpro.desktop.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Ondřej Soukup on 19.05.2017.
 */
public class NavigationView implements Initializable {

    @FXML
    public Button dashboardButton;
    @FXML
    public Button companiesButton;
    @FXML
    public Button aboutButton;
    @FXML
    public Button clientsButton;
    @FXML
    public Button subscriptionsButton;
    @FXML
    public Button paymentsButton;
    @FXML
    public Button resourcesButton;
    @FXML
    public Button settingsButton;
    @FXML
    public Button employeesButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Scene scene = new Scene(new Group(), 300, 800);

    }
}
