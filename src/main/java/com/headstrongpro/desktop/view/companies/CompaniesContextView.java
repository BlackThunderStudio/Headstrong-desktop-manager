package com.headstrongpro.desktop.view.companies;

import com.headstrongpro.desktop.controller.CompaniesController;
import com.headstrongpro.desktop.core.SyncHandler;
import com.headstrongpro.desktop.core.exception.DatabaseOutOfSyncException;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.core.fxControls.Footer;
import com.headstrongpro.desktop.model.entity.Company;
import com.headstrongpro.desktop.view.ContextView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Companies ContextView
 */
public class CompaniesContextView extends ContextView<Company> implements Initializable {
    // Top controls
    @FXML
    public Button toggleEditButton;
    @FXML
    public Button editButton;
    @FXML
    public Button cancelButton;
    @FXML
    public Button deleteButton;

    // Horizontal row with top controls
    @FXML
    public HBox topControls;

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
    private CompaniesController companiesController;

    // Whether making changes is allowed
    private boolean editMode = false;

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

        companiesController = new CompaniesController();

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
    public void toggleEditMode() {
        if (editMode) {
            topControls.getChildren().removeAll(editButton, cancelButton);
            topControls.getChildren().addAll(toggleEditButton, deleteButton);
            textFields.forEach(tf -> tf.setEditable(false));
        } else {
            topControls.getChildren().removeAll(toggleEditButton, deleteButton);
            topControls.getChildren().addAll(editButton, cancelButton);
            textFields.forEach(tf -> tf.setEditable(true));
        }
        editMode = !editMode;
    }

    @FXML
    public void handleEdit() {
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
                    companiesController.deleteCompany(contextItem.getId());
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

    private SyncHandler handler = () -> {
        try {
            return companiesController.getCompanyById(contextItem.getId());
        } catch (ModelSyncException e) {
            e.printStackTrace();
            mainWindowView.getContentView().footer.show(e.getMessage(), Footer.NotificationType.ERROR);
        }
        return null;
    };
    @FXML
    public void handleCancel() {
        toggleEditMode();
        populateForm();
    }

    @FXML
    public void companyClientsButtonOnClick(ActionEvent event) {
        displayNotImplementedError();
    }

    @FXML
    public void companyDepartmentsButtonOnClick(ActionEvent event) {
        displayNotImplementedError();
    }

    @FXML
    public void companyGroupsButtonOnClick(ActionEvent event) {
        displayNotImplementedError();
    }

    @FXML
    public void companySubscriptionsButtonOnClick(ActionEvent event) {
        displayNotImplementedError();
    }
}
