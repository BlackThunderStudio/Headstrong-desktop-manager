package com.headstrongpro.desktop.view.companies;

import com.headstrongpro.desktop.controller.CompaniesController;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.core.fxControls.Footer;
import com.headstrongpro.desktop.model.entity.Company;
import com.headstrongpro.desktop.view.ContentSource;
import com.headstrongpro.desktop.view.ContextView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

/**
 * Add new company ContextView
 */
public class CompaniesNewView extends ContextView<Company> implements Initializable {

    // Form text fields
    @FXML
    public TextField nameField, cvrField, streetField, postalField, cityField, countryField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        textFields.addAll(Arrays.asList(
                nameField,
                cvrField,
                streetField,
                postalField,
                cityField,
                countryField
        ));

        controller = new CompaniesController();
    }

    @Override
    public void populateForm() {
        mainWindowView.changeContext(ContentSource.COMPANIES, contextItem);
    }

    @FXML
    public void handleSave() {
        CompaniesController controller = (CompaniesController) this.controller;
        if (controller.validCompany(nameField.getText(),
                cvrField.getText(),
                streetField.getText(),
                postalField.getText(),
                cityField.getText(),
                countryField.getText())) {
            try {
                mainWindowView.getContentView().footer.show("Creating new company...", Footer.NotificationType.LOADING);
                controller.createCompany(nameField.getText(),
                        cvrField.getText(),
                        streetField.getText(),
                        postalField.getText(),
                        cityField.getText(),
                        countryField.getText());
                mainWindowView.getContentView().footer.show("Company created.", Footer.NotificationType.COMPLETED);
                clearFields();
                mainWindowView.getContentView().handleRefresh();
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
