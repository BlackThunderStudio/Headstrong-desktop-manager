package com.headstrongpro.desktop.view;

import javafx.event.ActionEvent;
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

    private MainWindowView mainWindowView;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Scene scene = new Scene(new Group(), 300, 800);
    }

    public void setMainWindowView(MainWindowView mainWindowView) {
        this.mainWindowView = mainWindowView;
    }

    @FXML
    public void dashboardOnClick(ActionEvent actionEvent) {
    }

    @FXML
    public void companiesOnClick(ActionEvent actionEvent) {
        mainWindowView.changeContent(ContentSource.COMPANIES);
    }

    @FXML
    public void clientsOnClick(ActionEvent actionEvent) {
    }

    @FXML
    public void subsOnClick(ActionEvent actionEvent) {
    }

    @FXML
    public void resourcesOnClick(ActionEvent actionEvent) {
    }

    @FXML
    public void employeesOnClick(ActionEvent actionEvent) {
    }

    @FXML
    public void paymentsOnClick(ActionEvent actionEvent) {
    }

    @FXML
    public void AboutOnClick(ActionEvent actionEvent) {
        mainWindowView.changeContent(ContentSource.ABOUT);
    }

    @FXML
    public void settingsOnClick(ActionEvent actionEvent) {
    }
}
