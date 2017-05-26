package com.headstrongpro.desktop.view.companies;

import com.headstrongpro.desktop.core.controller.CompaniesController;
import com.headstrongpro.desktop.core.exception.DatabaseOutOfSyncException;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.core.fxControls.Footer;
import com.headstrongpro.desktop.model.entity.Company;
import com.headstrongpro.desktop.view.ContextView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.Optional;
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

    private CompaniesController companiesController;

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
        clearFields();
        companiesController = new CompaniesController();
    }

    @FXML
    public void companiesContextEditButtonPress() {
        if (companiesController.validCompany(companyNameTextfield.getText(),
                companyCvrTextfield.getText(),
                companyStreetTextfield.getText(),
                companyPostalTextfield.getText(),
                companyCityTextfield.getText(),
                companyCountryTextfield.getText())) {
            try {
                mainWindowView.getContentView().footer.show("Updating company...", Footer.NotificationType.LOADING);
                companiesController.updateCompany(contextItem.getId(),
                        companyNameTextfield.getText(),
                        companyCvrTextfield.getText(),
                        companyStreetTextfield.getText(),
                        companyPostalTextfield.getText(),
                        companyCityTextfield.getText(),
                        companyCountryTextfield.getText());
                mainWindowView.getContentView().footer.show("Company updated.", Footer.NotificationType.COMPLETED);
                mainWindowView.getContentView().refreshButton.fire();
            } catch (ModelSyncException e) {
                e.fillInStackTrace();
                mainWindowView.getContentView().footer.show("Error! Could not update company!", Footer.NotificationType.ERROR, Footer.FADE_LONG);
            } catch (DatabaseOutOfSyncException e) {
                handleDataInconsistency();
            }
        } else
            mainWindowView.getContentView().footer.show("Values not valid!", Footer.NotificationType.ERROR, 2000);
    }

    @Override
    protected void clearFields() {
        companyCityTextfield.clear();
        companyCountryTextfield.clear();
        companyCvrTextfield.clear();
        companyNameTextfield.clear();
        companyPostalTextfield.clear();
        companyStreetTextfield.clear();
    }

    private void handleDataInconsistency(){
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        mainWindowView.getContentView().footer.show("Warning! Data inconsistency!", Footer.NotificationType.WARNING);
        a.setHeaderText("Warning! Database contains newer data.");
        a.setContentText("Do you want to reload the data? Clicking 'Cancel' will clear all the input");
        Optional<ButtonType> response = a.showAndWait();
        response.ifPresent(btn -> {
            if (ButtonType.OK.equals(btn)) {
                try {
                    changeContextItem(companiesController.getCompanyById(contextItem.getId()));
                } catch (ModelSyncException e1) {
                    e1.printStackTrace();
                    //TODO: handle error: couldn't reload the company
                }
            } else {
                clearFields();
            }
        });
    }


    @FXML
    public void deleteButtonOnClick(MouseEvent mouseEvent) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setHeaderText("Are you sure you want to delete " + contextItem.getName() + " from the list?");
        a.setContentText("You cannot take that action back");
        Optional<ButtonType> response = a.showAndWait();
        response.ifPresent(btn -> {
            if(ButtonType.OK.equals(btn)) {
                try {
                    mainWindowView.getContentView().footer.show("Deleting " + contextItem.getName() + "...", Footer.NotificationType.LOADING);
                    companiesController.deleteCompany(contextItem.getId());
                    mainWindowView.getContentView().footer.show("Company deleted.", Footer.NotificationType.COMPLETED);
                    mainWindowView.getContentView().refreshButton.fire();
                } catch (ModelSyncException e) {
                    e.printStackTrace();
                    mainWindowView.getContentView().footer.show(e.getMessage(), Footer.NotificationType.ERROR, Footer.FADE_LONG);
                } catch (DatabaseOutOfSyncException e) {
                    e.printStackTrace();
                    handleDataInconsistency();
                }
            }
        });
    }
}