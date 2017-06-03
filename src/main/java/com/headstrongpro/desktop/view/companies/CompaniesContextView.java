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
import javafx.scene.control.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Companies ContextView
 */
public class CompaniesContextView extends ContextView<Company> implements Initializable {

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

    // Links to related items
    @FXML
    public Button companyDepartmentsButton;
    @FXML
    public Button companyClientsButton;
    @FXML
    public Button companyGroupsButton;
    @FXML
    public Button companySubscriptionsButton;

    // Data controller
    private CompaniesController controller;

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

        textFields = new ArrayList<>(Arrays.asList(
                companyNameTextfield,
                companyCvrTextfield,
                companyStreetTextfield,
                companyPostalTextfield,
                companyCityTextfield,
                companyCountryTextfield
        ));

        controller = new CompaniesController();

        // By default, hide buttons of editing mode
        topControls.getChildren().removeAll(editButton, cancelButton);
    }

    @Override
    public void populateForm() {
        companyNameTextfield.setText(contextItem.getName());
        companyCvrTextfield.setText(contextItem.getCvr());
        companyStreetTextfield.setText(contextItem.getStreet());
        companyPostalTextfield.setText(contextItem.getPostal());
        companyCityTextfield.setText(contextItem.getCity());
        companyCountryTextfield.setText(contextItem.getCountry());
        System.out.println(contextItem.getName());
    }

    @FXML
    public void handleEdit() {
        if (controller.validCompany(companyNameTextfield.getText(),
                companyCvrTextfield.getText(),
                companyStreetTextfield.getText(),
                companyPostalTextfield.getText(),
                companyCityTextfield.getText(),
                companyCountryTextfield.getText())) {
            try {
                mainWindowView.getContentView().footer.show("Updating company...", Footer.NotificationType.LOADING);
                controller.updateCompany(contextItem.getId(),
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
                handleOutOfSync(handler);
            }
        } else
            mainWindowView.getContentView().footer.show("Values not valid!", Footer.NotificationType.ERROR);
    }

    @Override
    protected void clearFields() {
        textFields.forEach(TextInputControl::clear);
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
    public void handleCancel() {
        toggleEditMode();
        if (contextItem != null) populateForm();
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
