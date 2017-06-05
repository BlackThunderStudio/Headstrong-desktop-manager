package com.headstrongpro.desktop.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Navigation View
 */
public class NavigationView implements Initializable {

    // Nav links
    @FXML
    public Button dashboardButton, companiesButton, aboutButton, clientsButton;
    @FXML
    public Button subscriptionsButton, paymentsButton, resourcesButton, settingsButton;

    // MainWindow parent controller
    private MainWindowView mainWindowView;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        new Scene(new Group(), 300, 800);
    }

    public void setMainWindowView(MainWindowView mainWindowView) {
        this.mainWindowView = mainWindowView;
    }

    @FXML
    public void dashboardOnClick() {
        mainWindowView.changeContent(ContentSource.DASHBOARD);
    }

    @FXML
    public void companiesOnClick() {
        mainWindowView.changeContent(ContentSource.COMPANIES);
    }

    @FXML
    public void clientsOnClick() {
        mainWindowView.changeContent(ContentSource.CLIENTS);
    }

    @FXML
    public void subscriptionsOnClick() {
        mainWindowView.changeContent(ContentSource.SUBSCRIPTIONS);
    }

    @FXML
    public void resourcesOnClick() {
        mainWindowView.changeContent(ContentSource.RESOURCES);
    }

    @FXML
    public void coursesOnClick() {
        mainWindowView.changeContent(ContentSource.COURSES);
    }

    @FXML
    public void paymentsOnClick() {
        mainWindowView.changeContent(ContentSource.PAYMENTS);
    }

    @FXML
    public void aboutOnClick() {
        mainWindowView.changeContent(ContentSource.ABOUT);
    }

    @FXML
    public void settingsOnClick() {
        mainWindowView.changeContent(ContentSource.SETTINGS);
    }
}
