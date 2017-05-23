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

    // Content view controls
    @FXML
    public TextField searchCompaniesTextfield;
    @FXML
    public TableView companiesTable;
    @FXML
    public Button newCompanyButton;
    @FXML
    public Text companiesHeader;

    // Context view controls
    @FXML
    public Button companyEditButton;
    @FXML
    public Button companyDeleteButton;
    @FXML
    public TextField companyNameTextfield;
    @FXML
    public TextField companyCvrTextfield;
    @FXML
    public TextField companyStreetTextfield;
    @FXML
    public TextField companyPostalTextfield;
    @FXML
    public TextField companyCityTextfield;
    @FXML
    public TextField companyCountryTextfield;
    @FXML
    public Button companyDepartmentsButton;
    @FXML
    public Button companyClientsButton;
    @FXML
    public Button companyGroupsButton;
    @FXML
    public Button companySubscriptionsButton;

    // "New" context view controls
    @FXML
    public TextField newCompanyNameTextfield;
    @FXML
    public TextField newCompanyCvrTextfield;
    @FXML
    public TextField newCompanyStreetTextfield;
    @FXML
    public TextField newCompanyPostalTextfield;
    @FXML
    public TextField newCompanyCityTextfield;
    @FXML
    public TextField newCompanyCountryTextfield;
    @FXML
    public Button companySaveButton;
    @FXML
    public Button companyCancelButton;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
