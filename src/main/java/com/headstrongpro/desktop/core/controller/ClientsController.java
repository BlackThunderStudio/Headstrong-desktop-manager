package com.headstrongpro.desktop.core.controller;

import com.headstrongpro.desktop.DbLayer.DBClient;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.model.entity.Client;
import com.headstrongpro.desktop.model.entity.Person;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        //System.out.println("cl" + clientsDAO.getAll().size());
        return FXCollections.observableArrayList(clientsDAO.getAll());
    }

    //TODO fixxx
    public ObservableList<Person> search(String keyword) throws ModelSyncException{
        if(keyword == null) throw new NullPointerException();
        return FXCollections.observableArrayList(clients.stream().filter(e -> String.valueOf(e.getId()).contains(keyword) ||
                e.getName().toLowerCase().contains(keyword.toLowerCase()) ||
                e.getEmail().toLowerCase().contains(keyword.toLowerCase()) ||
                e.getPhone().toLowerCase().contains(keyword.toLowerCase()) ||
                e.getGender().toLowerCase().contains(keyword.toLowerCase())).collect(Collectors.toList()));
    }

    @Override
    public void refresh() throws ModelSyncException {
        clients.addAll(clientsDAO.getAll());
    }
}
