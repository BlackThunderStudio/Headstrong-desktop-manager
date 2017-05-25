package com.headstrongpro.desktop.view.companies;

import com.headstrongpro.desktop.core.controller.CompaniesController;
import com.headstrongpro.desktop.core.exception.DatabaseOutOfSyncException;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.model.entity.Company;
import com.headstrongpro.desktop.view.ContextView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Companies ContextView
 */
public class CompaniesContextView extends ContextView<Company> implements Initializable {
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

    CompaniesController companiesController;

    @Override
    public void setFields() {
        companyNameTextfield.setText(contextItem.getName());
        companyCvrTextfield.setText(contextItem.getCvr());
        companyStreetTextfield.setText(contextItem.getStreet());
        companyPostalTextfield.setText(contextItem.getPostal());
        companyCityTextfield.setText(contextItem.getCity());
        companyCountryTextfield.setText(contextItem.getCountry());
        System.out.println(contextItem.getName());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        companyNameTextfield.setText("");
        companyCvrTextfield.setText("");
        companyStreetTextfield.setText("");
        companyPostalTextfield.setText("");
        companyCityTextfield.setText("");
        companyCountryTextfield.setText("");
        try {
            companiesController = new CompaniesController();
        } catch (ModelSyncException e){
            e.fillInStackTrace();
        }
    }

    public void companiesContextEditButtonPress(){
        if(companiesController.validCompany(companyNameTextfield.getText(), companyCvrTextfield.getText(), companyStreetTextfield.getText(), companyPostalTextfield.getText(), companyCityTextfield.getText(), companyCountryTextfield.getText()))
            try {
                companiesController.updateCompany(contextItem.getId(), companyNameTextfield.getText(), companyCvrTextfield.getText(), companyStreetTextfield.getText(), companyPostalTextfield.getText(), companyCityTextfield.getText(), companyCountryTextfield.getText());
            } catch(ModelSyncException | DatabaseOutOfSyncException e){
                e.fillInStackTrace();
            }
    }
}
