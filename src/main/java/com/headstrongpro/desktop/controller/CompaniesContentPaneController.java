package com.headstrongpro.desktop.controller;

import com.sun.org.apache.bcel.internal.generic.IndexedInstruction;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.controlsfx.control.CheckComboBox;

import javax.swing.text.TableView;
import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Ondřej Soukup on 20.05.2017.
 */
public class CompaniesContentPaneController implements Initializable {

    @FXML
    public TextField searchCompaniesTextfield;
    @FXML
    public CheckComboBox filterCompaniesCombo;
    @FXML
    public Button searchCompaniesButton;
    @FXML
    public TableView companiesTable;
    @FXML
    public Button newCompanyButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
