package com.headstrongpro.desktop.view.clients;

import com.headstrongpro.desktop.controller.ClientsController;
import com.headstrongpro.desktop.core.SyncHandler;
import com.headstrongpro.desktop.core.exception.DatabaseOutOfSyncException;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.core.fxControls.Footer;
import com.headstrongpro.desktop.model.entity.Client;
import com.headstrongpro.desktop.view.ContextView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

/**
 * Clients Context view
 */
public class ClientsContextView extends ContextView<Client> implements Initializable {

    private final ToggleGroup genderRadios = new ToggleGroup();

    // Form text fields
    @FXML
    public TextField nameField, emailField, phoneField;
    @FXML
    public RadioButton maleGenderRadio, femaleGenderRadio;

    // Links to related items
    @FXML
    public Button clientsCompanyButton, clientsGroupsButton, clientsDepartmentsButton;

    private SyncHandler handler = () -> {
        try {
            return controller.getById(contextItem.getId());
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
                emailField,
                phoneField
        ));

        radioButtons.addAll(Arrays.asList(
                maleGenderRadio,
                femaleGenderRadio
        ));

        controller = new ClientsController();

        radioButtons.forEach(rb -> rb.setToggleGroup(genderRadios));

        setDefaults();
    }

    @Override
    protected void populateForm() {
        nameField.setText(contextItem.getName());
        emailField.setText(contextItem.getEmail());
        phoneField.setText(contextItem.getPhone());
        radioButtons.forEach(rb -> rb.setDisable(false));
        if (contextItem.getGender().equals("Male")) {
            maleGenderRadio.fire();
        } else {
            femaleGenderRadio.fire();
        }
        radioButtons.forEach(rb -> rb.setDisable(true));
    }

    @FXML
    public void handleEdit() {
        ClientsController controller = (ClientsController) this.controller;
        if (validateInput(nameField, emailField, phoneField)) {
            try {
                mainWindowView.getContentView().footer.show("Updating client...", Footer.NotificationType.LOADING);
                controller.updateClient(contextItem.getId(),
                        nameField.getText(),
                        emailField.getText(),
                        phoneField.getText(),
                        contextItem.getGender());
                mainWindowView.getContentView().footer.show("Client updated.", Footer.NotificationType.COMPLETED);
                mainWindowView.getContentView().refreshButton.fire();
            } catch (ModelSyncException e) {
                e.fillInStackTrace();
                mainWindowView.getContentView().footer.show("Error! Could not update company!", Footer.NotificationType.ERROR, Footer.FADE_LONG);
            } catch (DatabaseOutOfSyncException e) {
                e.fillInStackTrace();
                //noinspection unchecked
                handleOutOfSync(handler);
            }
        } else {
            mainWindowView.getContentView().footer.show("Invalid input", Footer.NotificationType.WARNING, Footer.FADE_QUICK);
        }
    }

    @FXML
    public void handleDelete() {
        handleDelete(handler, contextItem.getName());
    }

    @FXML
    public void clientsCompanyButtonOnClick() {
        displayNotImplementedError();
    }

    @FXML
    public void clientsGroupsButtonOnClick() {
        displayNotImplementedError();
    }

    @FXML
    public void clientsDepartmentsButtonOnClick() {
        displayNotImplementedError();
    }
}
