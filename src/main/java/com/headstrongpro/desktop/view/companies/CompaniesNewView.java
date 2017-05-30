package com.headstrongpro.desktop.view.companies;

/**
 * Created by 1062085 on 25-May-17.
 */

// "New" context view controls

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
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;


public class CompaniesNewView extends ContextView<Company> implements Initializable {
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

    private CompaniesController controller;

    @Override
    public void setFields() {
        newCompanyNameTextfield.setText(contextItem.getName());
        newCompanyCvrTextfield.setText(contextItem.getCvr());
        newCompanyStreetTextfield.setText(contextItem.getStreet());
        newCompanyPostalTextfield.setText(contextItem.getPostal());
        newCompanyCityTextfield.setText(contextItem.getCity());
        newCompanyCountryTextfield.setText(contextItem.getCountry());
    }

    @Override
    protected void clearFields() {
        newCompanyNameTextfield.clear();
        newCompanyCvrTextfield.clear();
        newCompanyStreetTextfield.clear();
        newCompanyPostalTextfield.clear();
        newCompanyCityTextfield.clear();
        newCompanyCountryTextfield.clear();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        clearFields();
        controller = new CompaniesController();
    }

    @FXML
    public void saveBtnOnClick(MouseEvent mouseEvent) {
        if(controller.validCompany(newCompanyNameTextfield.getText(),
        newCompanyCvrTextfield.getText(),
        newCompanyStreetTextfield.getText(),
        newCompanyPostalTextfield.getText(),
        newCompanyCityTextfield.getText(),
        newCompanyCountryTextfield.getText())){
            try {
                mainWindowView.getContentView().footer.show("Creating new conmpany...", Footer.NotificationType.LOADING);
                controller.createCompany(newCompanyNameTextfield.getText(),
                        newCompanyCvrTextfield.getText(),
                        newCompanyStreetTextfield.getText(),
                        newCompanyPostalTextfield.getText(),
                        newCompanyCityTextfield.getText(),
                        newCompanyCountryTextfield.getText());
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
    public void cancelBtnOnClick(MouseEvent mouseEvent) {
        clearFields();
        mainWindowView.changeContext(ContentSource.COMPANIES);
    }
}
