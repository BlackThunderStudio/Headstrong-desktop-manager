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


    /**
     * Default constructor
     * @throws ModelSyncException
     */
    public CompaniesController() throws ModelSyncException{
        companies = new ArrayList<>();
        companyDAO = new DBCompany();
        refresh();
    }

    /**
     * Constructor selecting the selected company,
     * may be useful if window is initialised
     * from another window
     * @param id Selected company id
     * @throws ModelSyncException
     */
    public CompaniesController(int id) throws ModelSyncException{
        companies = new ArrayList<>();
        companyDAO = new DBCompany();
        selectedCompanyId = id;
        refresh();
    }

    /**
     * Search keyword in all companies details
     * searches thru the companies by a keyword
     * @param keyword Searched expression
     * @return Observable Array list of all companies containing the keyword
     * @throws ModelSyncException
     */
    public ObservableList<Company> search(String keyword) throws ModelSyncException{
        if(keyword == null) throw new NullPointerException();
        if(keyword.isEmpty()) return FXCollections.observableArrayList(companyDAO.getAll());
        return FXCollections.observableArrayList(companies.stream()
                .filter(e -> String.valueOf(e.getId()).toLowerCase().contains(keyword) ||
                e.getName().toLowerCase().contains(keyword) ||
                e.getCvr().toLowerCase().contains(keyword) ||
                e.getStreet().toLowerCase().contains(keyword) ||
                e.getPostal().toLowerCase().contains(keyword) ||
                e.getCity().toLowerCase().contains(keyword) ||
                e.getCountry().toLowerCase().contains(keyword))
                .collect(Collectors.toList()));
    }

    /**
     * Validator for creating/updating company
     * @param name Company name
     * @param cvr Company cvr
     * @param street Company street
     * @param postal Company postal code
     * @param city Company city
     * @param country Company country
     * @return true if company details are valid, false otherwise
     */
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

    /**
     * Create a company if details are valid
     * @param name Company name
     * @param cvr Company cvr
     * @param street Company street
     * @param postal Company postal code
     * @param city Company city
     * @param country Company country
     * @throws ModelSyncException
     */
    public void createCompany(String name, String cvr, String street, String postal, String city, String country)
            throws ModelSyncException{
        if(validCompany(name, cvr, street, postal, city, country))
            companyDAO.persist(new Company(name, cvr, street, postal, city, country));
        refresh();
    }

    /**
     * Read all the companies
     * @return Observable array list of existing companies
     * @throws ModelSyncException
     */
    public ObservableList<Company> getCompanies() throws ModelSyncException{
        return FXCollections.observableArrayList(companyDAO.getAll());
    }

    /**
     * Update a company if new details are valid
     * @param id Company ID
     * @param name Company name
     * @param cvr Company cvr
     * @param street Company street
     * @param postal Company postal code
     * @param city Company city
     * @param country Company country
     * @throws ModelSyncException
     * @throws DatabaseOutOfSyncException
     */
    public void updateCompany(int id, String name, String cvr, String street, String postal, String city, String country)
            throws ModelSyncException, DatabaseOutOfSyncException{
        Company selectedCompany = companyDAO.getById(id);
        if(validCompany(name, cvr, street, postal, city, country))
            companyDAO.update(selectedCompany);
        refresh();
    }

    /**
     * Delete a company by ID
     * @param id Company ID
     * @throws ModelSyncException
     * @throws DatabaseOutOfSyncException
     */
    public void deleteCompany(int id) throws ModelSyncException, DatabaseOutOfSyncException{
        companyDAO.delete(companyDAO.getById(id));
        refresh();
    }

    /**
     * Get departments by company ID
     * @param id Company ID
     * @return Observable array list of departments
     * @throws ModelSyncException
     */
    public ObservableList<Department> getDepartmentsByCompanyId(int id) throws ModelSyncException{
        return FXCollections.observableArrayList(new DBDepartments().getByCompanyID(id));
    }

    /**
     * Get groups by company ID
     * @param id Company ID
     * @return Observable array list of groups
     * @throws ModelSyncException
     */
    public ObservableList<Group> getGroupsByCompanyId(int id) throws ModelSyncException{
        return FXCollections.observableArrayList(new DBGroups().getByCompanyID(id));
    }

    /**
     * Get subscriptions by company ID
     * @param id Company ID
     * @return Observable array list of subscriptions
     * @throws ModelSyncException
     */
    public ObservableList<Subscription> getSubcriptionByCompanyId(int id) throws ModelSyncException{
        return FXCollections.observableArrayList(new DBSubscriptions().getbyCompanyId(id));
    }

    /**
     * Get clients by company ID
     * @param id Company ID
     * @return Observable array list of clients
     * @throws ModelSyncException
     */
    public ObservableList<Person> getClientByCompanyId(int id) throws ModelSyncException{
        return FXCollections.observableArrayList(new DBClient().getByCompanyId(id));
    }

    /**
     * Get payments by company ID
     * @param id Company ID
     * @return Observable array list of payments
     * @throws ModelSyncException
     */
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
