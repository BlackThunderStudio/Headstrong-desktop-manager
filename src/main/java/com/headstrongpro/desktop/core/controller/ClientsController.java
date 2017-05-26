package com.headstrongpro.desktop.core.controller;

import com.headstrongpro.desktop.DbLayer.DBClient;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.model.entity.Client;
import com.headstrongpro.desktop.model.entity.Person;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

/**
 * Clients controller
 */
public class ClientsController implements Refreshable{
    List<Person> clients;
    DBClient clientsDAO;

    public ClientsController(){
        clients = new ArrayList<>();
        clientsDAO = new DBClient();
    }

    public ObservableList<Person> getClients() throws ModelSyncException {
        return FXCollections.observableArrayList(clientsDAO.getAll());
    }

    @Override
    public void refresh() throws ModelSyncException {
        clients.addAll(clientsDAO.getAll());
    }
}
