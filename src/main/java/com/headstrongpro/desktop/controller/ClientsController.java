package com.headstrongpro.desktop.controller;

import com.headstrongpro.desktop.DbLayer.DBClient;
import com.headstrongpro.desktop.DbLayer.DBCompany;
import com.headstrongpro.desktop.core.exception.DatabaseOutOfSyncException;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.model.entity.Client;
import com.headstrongpro.desktop.model.entity.Company;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Clients Controller
 */
public class ClientsController implements Refreshable, IContentController<Client> {
    private List<Client> clients;
    private DBClient clientsDAO;
    private DBCompany companyDAO;

    public ClientsController() {
        clients = new ArrayList<>();
        clientsDAO = new DBClient();
        companyDAO = new DBCompany();
    }

    @Override
    public ObservableList<Client> getAll() throws ModelSyncException {
        refresh();
        return FXCollections.observableArrayList(clientsDAO.getAll());
    }

    @Override
    public ObservableList<Client> searchByPhrase(String input) {
        if (input == null) throw new NullPointerException();
        return FXCollections.observableArrayList(clients.stream().filter(e -> String.valueOf(e.getId()).contains(input) ||
                e.getName().toLowerCase().contains(input.toLowerCase()) ||
                e.getEmail().toLowerCase().contains(input.toLowerCase()) ||
                e.getPhone().toLowerCase().contains(input.toLowerCase()) ||
                e.getGender().toLowerCase().contains(input.toLowerCase())).collect(Collectors.toList()));
    }

    @Override
    public void delete(Client client) throws DatabaseOutOfSyncException, ModelSyncException {
        clientsDAO.delete(client);
        refresh();
    }

    @Override
    public Client getById(int id) throws ModelSyncException {
        if (id > 0) {
            return clientsDAO.getById(id);
        } else throw new IllegalStateException("Id must be greater than 0!");
    }

    public void updateClient(int id, String name, String email, String phone, String gender) throws ModelSyncException, DatabaseOutOfSyncException {
        Client client = clientsDAO.getById(id);
        client.setName(name);
        client.setEmail(email);
        client.setPhone(phone);
        client.setGender(gender);
        clientsDAO.update(client);
    }

    @Override
    public void refresh() throws ModelSyncException {
        clients.addAll(clientsDAO.getAll());
    }

    public List<Company> LoadCompaniesData() throws ModelSyncException {
        return companyDAO.getAll();
    }

    public Client createNew(Client c) throws ModelSyncException {
        return clientsDAO.persist(c);
    }

    @Override
    public void edit(Client client) throws DatabaseOutOfSyncException, ModelSyncException {

    }
}
