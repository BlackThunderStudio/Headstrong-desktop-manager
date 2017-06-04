package com.headstrongpro.desktop.controller;

import com.headstrongpro.desktop.DbLayer.*;
import com.headstrongpro.desktop.core.exception.DatabaseOutOfSyncException;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.model.Department;
import com.headstrongpro.desktop.model.Group;
import com.headstrongpro.desktop.model.Payment;
import com.headstrongpro.desktop.model.Subscription;
import com.headstrongpro.desktop.model.entity.Company;
import com.headstrongpro.desktop.model.entity.Person;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Companies Controller
 */
public class CompaniesController implements Refreshable, IContentController<Company> {

    private List<Company> companies;
    private DBCompany companyDAO;

    /**
     * Default constructor
     */
    public CompaniesController() {
        companies = new ArrayList<>();
        companyDAO = new DBCompany();
    }

    /**
     * Validator for creating/updating company
     *
     * @param name    Company name
     * @param cvr     Company cvr
     * @param street  Company street
     * @param postal  Company postal code
     * @param city    Company city
     * @param country Company country
     * @return true if company details are valid, false otherwise
     */
    public boolean validCompany(String name, String cvr, String street, String postal, String city, String country) {
        boolean isValid = true;
        String basicRegex = "[A-Za-z0-9 .-]*";
        String intRegex = "^[0-9]*$";
        String cvrRegex = "^[0-9]{8}$";
        if (!(name.matches(basicRegex) ||
                street.matches(basicRegex) ||
                city.matches(basicRegex) ||
                country.matches(basicRegex)))
            isValid = false;
        if (!cvr.matches(cvrRegex))
            isValid = false;
        if (!postal.matches(intRegex))
            isValid = false;
        return isValid;
    }

    /**
     * Create a company if details are valid
     *
     * @param name    Company name
     * @param cvr     Company cvr
     * @param street  Company street
     * @param postal  Company postal code
     * @param city    Company city
     * @param country Company country
     * @throws ModelSyncException
     */
    public void createCompany(String name, String cvr, String street, String postal, String city, String country)
            throws ModelSyncException {
        if (validCompany(name, cvr, street, postal, city, country))
            companyDAO.persist(new Company(name, cvr, street, postal, city, country));
        refresh();
    }

    /**
     * Update a company if new details are valid
     *
     * @param id      Company ID
     * @param name    Company name
     * @param cvr     Company cvr
     * @param street  Company street
     * @param postal  Company postal code
     * @param city    Company city
     * @param country Company country
     * @throws ModelSyncException
     * @throws DatabaseOutOfSyncException
     */
    public void updateCompany(int id, String name, String cvr, String street, String postal, String city, String country)
            throws ModelSyncException, DatabaseOutOfSyncException {
        Company selectedCompany = companyDAO.getById(id);
        if (validCompany(name, cvr, street, postal, city, country)) {
            selectedCompany.setName(name);
            selectedCompany.setCvr(cvr);
            selectedCompany.setStreet(street);
            selectedCompany.setPostal(postal);
            selectedCompany.setCity(city);
            selectedCompany.setCountry(country);
            companyDAO.update(selectedCompany);
        }
        refresh();
    }

    /**
     * Get departments by company ID
     *
     * @param id Company ID
     * @return Observable array list of departments
     * @throws ModelSyncException
     */
    public ObservableList<Department> getDepartmentsByCompanyId(int id) throws ModelSyncException {
        return FXCollections.observableArrayList(new DBDepartments().getByCompanyID(id));
    }

    /**
     * Get groups by company ID
     *
     * @param id Company ID
     * @return Observable array list of groups
     * @throws ModelSyncException
     */
    public ObservableList<Group> getGroupsByCompanyId(int id) throws ModelSyncException {
        return FXCollections.observableArrayList(new DBGroups().getByCompanyID(id));
    }

    /**
     * Get subscriptions by company ID
     *
     * @param id Company ID
     * @return Observable array list of subscriptions
     * @throws ModelSyncException
     */
    public ObservableList<Subscription> getSubscriptionByCompanyId(int id) throws ModelSyncException {
        return FXCollections.observableArrayList(new DBSubscriptions().getbyCompanyId(id));
    }

    /**
     * Get clients by company ID
     *
     * @param id Company ID
     * @return Observable array list of clients
     * @throws ModelSyncException
     */
    public ObservableList<Person> getClientByCompanyId(int id) throws ModelSyncException {
        return FXCollections.observableArrayList(new DBClient().getByCompanyId(id));
    }

    /**
     * Get payments by company ID
     *
     * @param id Company ID
     * @return Observable array list of payments
     * @throws ModelSyncException
     */
    public ObservableList<Payment> getPaymentsByCompanyId(int id) throws ModelSyncException {
        List<Subscription> subscriptions = new DBSubscriptions().getbyCompanyId(id);
        List<Payment> payments = new ArrayList<>();
        for (Subscription s :
                subscriptions) {
            payments.addAll(new DBPayment().getBySubscriptionId(s.getId()));
        }
        return FXCollections.observableArrayList(payments);
    }

    @Override
    public void refresh() throws ModelSyncException {
        companies.clear();
        companies.addAll(companyDAO.getAll());
    }

    @Override
    public ObservableList<Company> getAll() throws ModelSyncException {
        refresh();
        return FXCollections.observableArrayList(companies);
    }

    @Override
    public ObservableList<Company> searchByPhrase(String input) {
        if (input == null) throw new NullPointerException();
        return FXCollections.observableArrayList(companies.stream()
                .filter(e -> e.getName().toLowerCase().contains(input.toLowerCase()) ||
                        e.getCvr().toLowerCase().contains(input.toLowerCase()) ||
                        e.getStreet().toLowerCase().contains(input.toLowerCase()) ||
                        e.getPostal().toLowerCase().contains(input.toLowerCase()) ||
                        e.getCity().toLowerCase().contains(input.toLowerCase()) ||
                        e.getCountry().toLowerCase().contains(input.toLowerCase()))
                .collect(Collectors.toList()));
    }

    @Override
    public void delete(Company company) throws DatabaseOutOfSyncException, ModelSyncException {
        companyDAO.delete(company);
        refresh();
    }

    @Override
    public Company createNew(Company company) throws ModelSyncException {
        return companyDAO.persist(company);
    }

    @Override
    public void edit(Company company) throws DatabaseOutOfSyncException, ModelSyncException {
        companyDAO.update(company);
        refresh();
    }

    @Override
    public Company getById(int id) throws ModelSyncException {
        return companyDAO.getById(id);
    }
}
