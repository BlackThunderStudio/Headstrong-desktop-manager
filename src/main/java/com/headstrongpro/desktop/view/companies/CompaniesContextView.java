package com.headstrongpro.desktop.view.companies;

import com.headstrongpro.desktop.model.entity.Company;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * Created by 1062085 on 25-May-17.
 */
public class CompaniesContextView { // Context view controls
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

    public void setFields(Company company){
        companyNameTextfield.setText(company.getName());
        companyCvrTextfield.setText(company.getCvr());
        companyStreetTextfield.setText(company.getStreet());
        companyPostalTextfield.setText(company.getPostal());
        companyCityTextfield.setText(company.getCity());
        companyCountryTextfield.setText(company.getCountry());
    }
}
