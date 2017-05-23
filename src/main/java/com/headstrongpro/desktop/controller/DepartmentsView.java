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
public class DepartmentsView implements Initializable {

    @FXML
    public TextField searchDepartmentsTextfield;
    @FXML
    public TableView departmentsTable;
    @FXML
    public Button newDepartmentButton;
    @FXML
    public Text departmentsHeader;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
