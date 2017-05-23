package com.headstrongpro.desktop.core.controller;

import com.headstrongpro.desktop.core.connection.Configurable;
import com.headstrongpro.desktop.core.exception.DatabaseOutOfSyncException;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.model.Department;
import com.headstrongpro.desktop.model.Group;
import com.headstrongpro.desktop.model.Payment;
import com.headstrongpro.desktop.model.Subscription;
import com.headstrongpro.desktop.model.entity.Client;
import com.headstrongpro.desktop.model.entity.Company;
import com.headstrongpro.desktop.model.entity.Person;
import com.headstrongpro.desktop.modelCollections.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.bouncycastle.math.raw.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Companies view
 */
public class CompaniesController implements Refreshable {
    private List<Company> companies;
    private DBCompany companyDAO;
    private int selectedCompanyId;



    //constructor from main window
    public CompaniesController() throws ModelSyncException{
        companies = new ArrayList<>();
        companyDAO = new DBCompany();
        refresh();
    }
    //constructor from other windows
    public CompaniesController(int id) throws ModelSyncException{
        companies = new ArrayList<>();
        companyDAO = new DBCompany();
        selectedCompanyId = id;
        refresh();
    }

    //lambda homosex'd up search
    public ObservableList<Company> search(String query) throws ModelSyncException{
        if(query == null) throw new NullPointerException();
        if(query.isEmpty()) return FXCollections.observableArrayList(companyDAO.getAll());
        return FXCollections.observableArrayList(companies.stream()
                .filter(e -> String.valueOf(e.getId()).toLowerCase().contains(query) ||
                e.getName().toLowerCase().contains(query) ||
                e.getCvr().toLowerCase().contains(query) ||
                e.getStreet().toLowerCase().contains(query) ||
                e.getPostal().toLowerCase().contains(query) ||
                e.getCity().toLowerCase().contains(query) ||
                e.getCountry().toLowerCase().contains(query))
                .collect(Collectors.toList()));
    }

    //check if details input are valid
    private boolean validCompany(String name, String cvr, String street, String postal, String city, String country){
        boolean isValid = true;
        String basicRegex = "[A-Za-z0-9 ]";
        String cvrRegex = "^\\d{8}&";
        if(!(name.matches(basicRegex) ||
                cvr.matches(cvrRegex) ||
                street.matches(basicRegex) ||
                postal.matches(basicRegex) ||
                city.matches(basicRegex) ||
                country.matches(basicRegex)))
            isValid = false;
        return isValid;
    }

    //add
    public void addCompany(String name, String cvr, String street, String postal, String city, String country)
            throws ModelSyncException{
        if(validCompany(name, cvr, street, postal, city, country))
            companyDAO.persist(new Company(name, cvr, street, postal, city, country));
        refresh();
    }
    //get all
    public ObservableList<Company> getCompanies() throws ModelSyncException{
        return FXCollections.observableArrayList(companyDAO.getAll());
    }
    //update
    public void updateCompany(int id, String name, String cvr, String street, String postal, String city, String country)
            throws ModelSyncException, DatabaseOutOfSyncException{
        Company selectedCompany = companyDAO.getById(id);
        if(validCompany(name, cvr, street, postal, city, country))
            companyDAO.update(selectedCompany);
        refresh();
    }
    //delete
    public void deleteCompany(int id) throws ModelSyncException, DatabaseOutOfSyncException{
        companyDAO.delete(companyDAO.getById(id));
        refresh();
    }

    //get departments by company id
    public ObservableList<Department> getDepartmentsByCompanyId(int id) throws ModelSyncException{
        return FXCollections.observableArrayList(new DBDepartments().getByCompanyID(id));
    }
    //groups
    public ObservableList<Group> getGroupsByCompanyId(int id) throws ModelSyncException{
        return FXCollections.observableArrayList(new DBGroups().getByCompanyID(id));
    }
    //subscriptions
    public ObservableList<Subscription> getSubcriptionByCompanyId(int id) throws ModelSyncException{
        return FXCollections.observableArrayList(new DBSubscriptions().getbyCompanyId(id));
    }
    //clients
    public ObservableList<Person> getClientByCompanyId(int id) throws ModelSyncException{
        return FXCollections.observableArrayList(new DBClient().getByCompanyId(id));
    }
    //payments
    public ObservableList<Payment> getPaymentsByCompanyId(int id) throws ModelSyncException{
        List<Subscription> subscriptions = new DBSubscriptions().getbyCompanyId(id);
        List<Payment> payments = new ArrayList<>();
        for (Subscription s:
             subscriptions) {
            payments.addAll(new DBPayment().getBySubscriptionId(s.getId()));
        }
        return FXCollections.observableArrayList(payments);
    }

    @Override
    public void refresh() throws ModelSyncException{
        companies.addAll(companyDAO.getAll());
    }
}
