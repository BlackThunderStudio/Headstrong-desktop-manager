package com.headstrongpro.desktop.view.companies;

import com.headstrongpro.desktop.controller.CompaniesController;
import com.headstrongpro.desktop.core.SyncHandler;
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

import java.net.URL;
import java.util.Arrays;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Companies ContextView
 */
public class CompaniesContextView extends ContextView<Company> implements Initializable {

    // Form text fields
    @FXML
    public TextField nameField, cvrField, streetField, postalField, cityField, countryField;

    // Links to related items
    @FXML
    public Button companyDepartmentsButton, companyClientsButton, companyGroupsButton, companySubscriptionsButton;

    private CompaniesController controller; // Data controller

    private SyncHandler handler = () -> {
        try {
            return controller.getCompanyById(contextItem.getId());
        } catch (ModelSyncException e) {
            e.printStackTrace();
            mainWindowView.getContentView().footer.show(e.getMessage(), Footer.NotificationType.ERROR);
        }
        return null;
    };

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

        setDefaults();
    }

    @Override
    public void populateForm() {
        nameField.setText(contextItem.getName());
        cvrField.setText(contextItem.getCvr());
        streetField.setText(contextItem.getStreet());
        postalField.setText(contextItem.getPostal());
        cityField.setText(contextItem.getCity());
        countryField.setText(contextItem.getCountry());
    }

    @FXML
    public void handleEdit() {
        if (controller.validCompany(nameField.getText(),
                cvrField.getText(),
                streetField.getText(),
                postalField.getText(),
                cityField.getText(),
                countryField.getText())) {
            try {
                mainWindowView.getContentView().footer.show("Updating company...", Footer.NotificationType.LOADING);
                controller.updateCompany(contextItem.getId(),
                        nameField.getText(),
                        cvrField.getText(),
                        streetField.getText(),
                        postalField.getText(),
                        cityField.getText(),
                        countryField.getText());
                mainWindowView.getContentView().footer.show("Company updated.", Footer.NotificationType.COMPLETED);
                mainWindowView.getContentView().refreshButton.fire();
            } catch (ModelSyncException e) {
                e.fillInStackTrace();
                mainWindowView.getContentView().footer.show("Error! Could not update company!", Footer.NotificationType.ERROR, Footer.FADE_LONG);
            } catch (DatabaseOutOfSyncException e) {
                handleOutOfSync(handler);
            }
        } else
            mainWindowView.getContentView().footer.show("Values not valid!", Footer.NotificationType.ERROR);
    }

    @FXML
    public void handleDelete() {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setHeaderText(String.format("Are you sure you want to delete %s from the list?", contextItem.getName()));
        a.setContentText("You cannot take that action back");
        Optional<ButtonType> response = a.showAndWait();
        response.ifPresent(btn -> {
            if (ButtonType.OK.equals(btn)) {
                try {
                    mainWindowView.getContentView().footer.show("Deleting " + contextItem.getName() + "...", Footer.NotificationType.LOADING);
                    controller.deleteCompany(contextItem.getId());
                    mainWindowView.getContentView().footer.show("Company deleted.", Footer.NotificationType.COMPLETED);
                    mainWindowView.getContentView().refreshButton.fire();
                } catch (ModelSyncException e) {
                    e.printStackTrace();
                    mainWindowView.getContentView().footer.show(e.getMessage(), Footer.NotificationType.ERROR, Footer.FADE_LONG);
                } catch (DatabaseOutOfSyncException e) {
                    handleOutOfSync(handler);
                }
            }
        });
    }

    @FXML
    public void companyClientsButtonOnClick() {
        displayNotImplementedError();
    }

    @FXML
    public void companyDepartmentsButtonOnClick() {
        displayNotImplementedError();
    }

    @FXML
    public void companyGroupsButtonOnClick() {
        displayNotImplementedError();
    }

    @FXML
    public void companySubscriptionsButtonOnClick() {
        displayNotImplementedError();
    }
}
