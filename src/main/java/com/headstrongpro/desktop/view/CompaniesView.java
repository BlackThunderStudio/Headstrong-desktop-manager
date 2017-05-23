package com.headstrongpro.desktop.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Ondřej Soukup on 20.05.2017.
 */
public class CompaniesView implements Initializable {

    @FXML
    public TextField searchCompaniesTextfield;
    @FXML
    public TableView companiesTable;
    @FXML
    public Button newCompanyButton;
    @FXML
    public Text companiesHeader;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
