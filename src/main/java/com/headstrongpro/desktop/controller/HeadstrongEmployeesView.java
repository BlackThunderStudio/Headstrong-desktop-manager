package com.headstrongpro.desktop.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Ondřej Soukup on 23.05.2017.
 */
public class HeadstrongEmployeesView implements Initializable {

    @FXML
    public Text headstrongEmployeesHeader;
    @FXML
    public TextField searchHeadstrongEmployeesTextfield;
    @FXML
    public TableView headstrongEmployeesTable;
    @FXML
    public Button newHeadstrongEmployeeButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
