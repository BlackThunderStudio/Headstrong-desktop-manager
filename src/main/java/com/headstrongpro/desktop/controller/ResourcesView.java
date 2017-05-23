package com.headstrongpro.desktop.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Ondřej Soukup on 23.05.2017.
 */
public class ResourcesView implements Initializable {

    @FXML
    public TextField searchResourcesTextfield;
    @FXML
    public ComboBox resourcesComboBox;
    @FXML
    public TableView resourcesTable;
    @FXML
    public Button newResourceButton;
    @FXML
    public Text resourcesHeader;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
