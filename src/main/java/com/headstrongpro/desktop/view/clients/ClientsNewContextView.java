package com.headstrongpro.desktop.view.clients;

import com.headstrongpro.desktop.controller.ClientsController;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.core.fxControls.Footer;
import com.headstrongpro.desktop.model.entity.Client;
import com.headstrongpro.desktop.model.entity.Company;
import com.headstrongpro.desktop.view.ContentSource;
import com.headstrongpro.desktop.view.ContextView;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.Date;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Add new client ContextView
 */
public class ClientsNewContextView extends ContextView<Client> implements Initializable {

    // Form text fields
    @FXML
    public TextField nameField, emailField, phoneField;
    @FXML
    public RadioButton maleGenderRadio, femaleGenderRadio;
    @FXML
    public ComboBox<String> companyComboBox;

    private ClientsController controller;
    private List<Company> companies;

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

        Task<List<Company>> initCompanies = new Task<List<Company>>() {
            @Override
            protected List<Company> call() throws Exception {
                return controller.LoadCompaniesData();
            }
        };

        initCompanies.valueProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue != null) {
                companies = newValue;
                companyComboBox.getItems().addAll(companies.stream().map(Company::getName).collect(Collectors.toList()));
                companyComboBox.getSelectionModel().selectFirst();
            }
        }));

        maleGenderRadio.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue) {
                femaleGenderRadio.setSelected(false);
            }
        }));

        femaleGenderRadio.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue) {
                maleGenderRadio.setSelected(false);
            }
        }));

        new Thread(initCompanies).start();
    }

    @Override
    public void populateForm() {
        mainWindowView.changeContext(ContentSource.CLIENTS, contextItem);
    }

    @FXML
    public void handleSave() {
        if (validateInput(emailField, nameField, phoneField)) {
            Client client = new Client(nameField.getText(),
                    emailField.getText(),
                    phoneField.getText(),
                    maleGenderRadio.isArmed() ? "Male" : "Female",
                    "",
                    "",
                    new Date(Calendar.getInstance().getTimeInMillis()),
                    companies.stream().filter(e -> e.getName().equals(companyComboBox.getValue())).map(Company::getId).findFirst().get());
            try {
                mainWindowView.getContentView().footer.show("Creating new client...", Footer.NotificationType.LOADING);
                controller.createNew(client);
                mainWindowView.getContentView().footer.show("Client created.", Footer.NotificationType.COMPLETED);
                clearFields();
                mainWindowView.getContentView().handleRefresh();
            } catch (ModelSyncException e) {
                e.printStackTrace();
                mainWindowView.getContentView().footer.show(e.getMessage(), Footer.NotificationType.ERROR);
            }
        } else {
            mainWindowView.getContentView().footer.show("Invalid input!", Footer.NotificationType.WARNING);
        }
    }

    @FXML
    public void handleCancel() {
        clearFields();
        mainWindowView.changeContext(ContentSource.CLIENTS);
    }
}
