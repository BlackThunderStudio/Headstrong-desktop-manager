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
import org.bouncycastle.math.raw.Mod;

import java.util.ArrayList;
import java.util.List;

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

    //search
    public List<Company> search(String query) throws ModelSyncException{
        companies = companyDAO.getAll();
        List<Company> foundCompanies = new ArrayList<>();
        for (Company c:
             companies)
            if((String.valueOf(c.getId()).contains(query)) || c.getName().contains(query) || c.getCvr().contains(query) || c.getStreet().contains(query) || c.getPostal().contains(query) || c.getCity().contains(query) || c.getCountry().contains(query))
                foundCompanies.add(c);

        return foundCompanies;
    }
    //add TODO
    public void addCompany(String name, String cvr, String street, String postal, String city, String country){

    }
    //update TODO not sure if object or string arguments are to be passed
    public void updateCompany(Company company) throws ModelSyncException, DatabaseOutOfSyncException{
        companyDAO.update(company);
    }

    //delete
    public void deleteCompany(int id) throws ModelSyncException, DatabaseOutOfSyncException{
        companyDAO.delete(companyDAO.getById(id));
        refresh();
    }

    //get departments by company id
    public List<Department> getDepartmentsByCompanyId(int id) throws ModelSyncException{
        return (new DBDepartments().getByCompanyID(id));
    }
    //groups
    public List<Group> getGroupsByCompanyId(int id) throws ModelSyncException{
        return (new DBGroups().getByCompanyID(id));
    }
    //subscriptions
    public List<Subscription> getSubcriptionByCompanyId(int id) throws ModelSyncException{
        return (new DBSubscriptions().getbyCompanyId(id));
    }
    //clients
    public List<Person> getClientByCompanyId(int id) throws ModelSyncException{
        return (new DBClient().getByCompanyId(id));
    }
    //payments
    public List<Payment> getPaymentsByCompanyId(int id) throws ModelSyncException{
        List<Subscription> subscriptions = new DBSubscriptions().getbyCompanyId(id);
        List<Payment> payments = new ArrayList<>();
        for (Subscription s:
             subscriptions) {
            payments.addAll(new DBPayment().getBySubscriptionId(s.getId()));
        }
        return (payments);
    }

    @Override
    public void refresh() throws ModelSyncException{
        companies.addAll(companyDAO.getAll());
    }


}
