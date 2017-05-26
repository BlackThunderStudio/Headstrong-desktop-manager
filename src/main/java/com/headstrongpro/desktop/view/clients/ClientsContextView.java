package com.headstrongpro.desktop.view.clients;

import com.headstrongpro.desktop.model.entity.Client;
import com.headstrongpro.desktop.view.ContextView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Clients Context view
 */
public class ClientsContextView extends ContextView<Client> implements Initializable {

    @FXML
    public Button clientsEditButton;
    @FXML
    public Button clientsDeleteButton;
    @FXML
    public Button clientsCompanyButton;
    @FXML
    public Button clientsGroupsButton;
    @FXML
    public Button clientsDepartmentsButton;
    @FXML
    public TextField clientsNameTextfield;
    @FXML
    public TextField clientsEmailTextfield;
    @FXML
    public TextField clientsPhoneTextfield;
    @FXML
    public RadioButton clientsGenderMaleRadio;
    @FXML
    public RadioButton clientsGenderFemaleRadio;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        clearFields();
        //clientsController = new ClientsController();
    }

    @Override
    public void setFields(){

    }

    @Override
    public void clearFields(){

    }
}
