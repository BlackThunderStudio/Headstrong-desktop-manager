package com.headstrongpro.desktop.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Main Window View
 */
public class MainWindowView implements Initializable {

    private static MainWindowView ourInstance = new MainWindowView();
    @FXML
    public SplitPane layout;
    @FXML
    public Pane navigationBar;
    @FXML
    public Pane contentBar;
    @FXML
    public Pane contextBar;

    private FXMLLoader contentBarLoader;
    private FXMLLoader contextBarLoader;

    public static MainWindowView getInstance() {
        return ourInstance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            navigationBar = FXMLLoader.load(getClass().getResource("/layout/navigationPane.fxml"));
            changeContent(ContentSource.COMPANIES);
        } catch (Exception e) {
            e.printStackTrace();
        }
        layout.getItems().set(0, navigationBar);
        layout.getItems().set(1, contentBar);
        layout.getItems().set(2, contextBar);
        layout.setDividerPosition(0, 0.20);
        layout.setDividerPosition(1, 0.75);
    }

    public void changeContent(ContentSource contentSource) {
        contentBarLoader = new FXMLLoader();
        contentBarLoader.setLocation(getClass().getResource(contentSource.getContentView()));

        contextBarLoader = new FXMLLoader();
        contextBarLoader.setLocation(getClass().getResource(contentSource.getContextView()));

        try {
            contentBar = contentBarLoader.load();
            contextBar = contextBarLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ContentView contentView = contentBarLoader.getController();
        contentView.setContextView(contextBarLoader.getController());
    }

}
