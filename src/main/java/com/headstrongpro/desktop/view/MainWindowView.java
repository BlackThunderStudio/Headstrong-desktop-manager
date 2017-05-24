package com.headstrongpro.desktop.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by rajmu on 17.04.06.
 */
public class MainWindowView implements Initializable {


    @FXML
    public SplitPane layout;

    @FXML
    public Pane navigationBar;

    @FXML
    public Pane contentBar;

    @FXML
    public Pane contextBar;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("stuff");
        try {
            navigationBar = FXMLLoader.load(getClass().getResource("/layout/navigationPane.fxml"));
            contentBar = FXMLLoader.load(getClass().getResource("/layout/companies/companiesContentPane.fxml"));
            contextBar = FXMLLoader.load(getClass().getResource("/layout/companies/companiesNewPane.fxml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        layout.getItems().set(0, navigationBar);
        layout.getItems().set(1, contentBar);
        layout.getItems().set(2, contextBar);
        layout.setDividerPosition(0, 0.20);
        layout.setDividerPosition(1, 0.78);
    }
}
