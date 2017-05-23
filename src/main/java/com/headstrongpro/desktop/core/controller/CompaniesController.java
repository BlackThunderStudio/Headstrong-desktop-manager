package com.headstrongpro.desktop.core.controller;

import com.headstrongpro.desktop.core.connection.Configurable;
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
 * Companies controller
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
    //add
    //edit
    //delete

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
