package com.headstrongpro.desktop.view.clients;

import com.headstrongpro.desktop.controller.ClientsController;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.core.fxControls.Footer;
import com.headstrongpro.desktop.model.entity.Client;
import com.headstrongpro.desktop.model.entity.Company;
import com.headstrongpro.desktop.view.ContentSource;
import com.headstrongpro.desktop.view.ContextView;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * desktop-manager
 * <p>
 * <p>
 * Created by rajmu on 17.06.03.
 */
public class ClientsNewContextView extends ContextView<Client> implements Initializable {
    @FXML
    public TextField clientsNameTextfield;
    @FXML
    public TextField clientsEmailTextfield;
    @FXML
    public TextField clientsPhoneTextfield;
    @FXML
    public Button saveBtn;
    @FXML
    public Button cancelBtn;
    @FXML
    public RadioButton clientsGenderMaleRadio;
    @FXML
    public RadioButton clientsGenderFemaleRadio;
    @FXML
    public ComboBox<String> companyCombo;

    private ClientsController controller;
    private List<Company> companies;

    @Override
    public void populateForm() {

    }

    @Override
    protected void clearFields() {
        clientsEmailTextfield.clear();
        clientsNameTextfield.clear();
        clientsPhoneTextfield.clear();
        clientsGenderFemaleRadio.disarm();
        clientsGenderMaleRadio.disarm();
        companyCombo.getSelectionModel().selectFirst();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        controller = new ClientsController();
        Task<List<Company>> initCompanies = new Task<List<Company>>() {
            @Override
            protected List<Company> call() throws Exception {
                return controller.LoadCompaniesData();
            }
        };

        initCompanies.valueProperty().addListener(((observable, oldValue, newValue) -> {
            if(newValue != null){
                companies = newValue;
                companyCombo.getItems().addAll(companies.stream().map(Company::getName).collect(Collectors.toList()));
                companyCombo.getSelectionModel().selectFirst();
            }
        }));

        clientsGenderMaleRadio.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            if(newValue){
                clientsGenderFemaleRadio.setSelected(false);
            }
        }));

        clientsGenderFemaleRadio.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            if(newValue){
                clientsGenderMaleRadio.setSelected(false);
            }
        }));

        new Thread(initCompanies).start();
    }

    @FXML
    public void saveBtnOnClick(ActionEvent event) {
        if(validateInput(clientsEmailTextfield, clientsNameTextfield, clientsPhoneTextfield)){
            Client client = new Client(clientsNameTextfield.getText(),
                    clientsEmailTextfield.getText(),
                    clientsPhoneTextfield.getText(),
                    clientsGenderMaleRadio.isArmed()?"Male":"Female", //TODO: there's some error while converting theese values to database BIT
                    "",
                    "",
                    new Date(Calendar.getInstance().getTimeInMillis()),
                    companies.stream().filter(e -> e.getName().equals(companyCombo.getValue())).map(Company::getId).findFirst().get());
            try {
                controller.createNew(client);
                mainWindowView.getContentView().footer.show("Client created.", Footer.NotificationType.COMPLETED);
                mainWindowView.changeContent(ContentSource.CLIENTS);
            } catch (ModelSyncException e) {
                e.printStackTrace();
                mainWindowView.getContentView().footer.show(e.getMessage(), Footer.NotificationType.ERROR);
            }
        } else {
            mainWindowView.getContentView().footer.show("Invalid input!", Footer.NotificationType.WARNING);
        }
    }

    @FXML
    public void cancelBtnOnClick(ActionEvent event) {
        clearFields();
    }
}
