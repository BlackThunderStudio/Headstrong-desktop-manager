package com.headstrongpro.desktop.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Ondřej Soukup on 23.05.2017.
 */
public class ClientsView implements Initializable {

    @FXML
    public TextField searchClientsTextfield;
    @FXML
    public TableView clientsTable;
    @FXML
    public Button newClientButton;
    @FXML
    public Text clientsHeader;
    @FXML
    public Button assignMoreButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
