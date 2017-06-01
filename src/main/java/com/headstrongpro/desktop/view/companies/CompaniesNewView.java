package com.headstrongpro.desktop.view.companies;

import com.headstrongpro.desktop.controller.CompaniesController;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.core.fxControls.Footer;
import com.headstrongpro.desktop.model.entity.Company;
import com.headstrongpro.desktop.view.ContentSource;
import com.headstrongpro.desktop.view.ContextView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Add new company ContextView
 */
public class CompaniesNewView extends ContextView<Company> implements Initializable {
    // Form text fields
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

    // Bottom controls
    @FXML
    public Button saveButton;
    @FXML
    public Button cancelButton;

    // Data controller
    private CompaniesController controller;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        clearFields();
        controller = new CompaniesController();
    }

    @Override
    public void populateForm() {
        mainWindowView.changeContext(ContentSource.COMPANIES, contextItem);
    }

    @Override
    protected void clearFields() {
        companyNameTextfield.clear();
        companyCvrTextfield.clear();
        companyStreetTextfield.clear();
        companyPostalTextfield.clear();
        companyCityTextfield.clear();
        companyCountryTextfield.clear();
    }

    @FXML
    public void handleSave() {
        if (controller.validCompany(companyNameTextfield.getText(),
                companyCvrTextfield.getText(),
                companyStreetTextfield.getText(),
                companyPostalTextfield.getText(),
                companyCityTextfield.getText(),
                companyCountryTextfield.getText())) {
            try {
                mainWindowView.getContentView().footer.show("Creating new conmpany...", Footer.NotificationType.LOADING);
                controller.createCompany(companyNameTextfield.getText(),
                        companyCvrTextfield.getText(),
                        companyStreetTextfield.getText(),
                        companyPostalTextfield.getText(),
                        companyCityTextfield.getText(),
                        companyCountryTextfield.getText());
                mainWindowView.getContentView().footer.show("Company created.", Footer.NotificationType.COMPLETED);
                clearFields();
                mainWindowView.getContentView().refreshButton.fire();
            } catch (ModelSyncException e) {
                e.printStackTrace();
                mainWindowView.getContentView().footer.show(e.getMessage(), Footer.NotificationType.ERROR);
            }
        } else {
            mainWindowView.getContentView().footer.show("Values are not valid", Footer.NotificationType.WARNING);
        }
    }

    @FXML
    public void handleCancel() {
        clearFields();
        mainWindowView.changeContext(ContentSource.COMPANIES);
    }
}
