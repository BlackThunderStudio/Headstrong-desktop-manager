package com.headstrongpro.desktop.view.clients;

import com.headstrongpro.desktop.model.entity.Client;
import com.headstrongpro.desktop.view.ContextView;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Clients Context view
 */
public class ClientsContextView extends ContextView<Client> implements Initializable {

    //ClientsController

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
