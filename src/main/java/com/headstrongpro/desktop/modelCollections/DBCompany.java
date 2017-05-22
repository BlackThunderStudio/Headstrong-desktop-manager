package com.headstrongpro.desktop.modelCollections;

import com.headstrongpro.desktop.core.connection.DBConnect;
import com.headstrongpro.desktop.core.exception.ConnectionException;
import com.headstrongpro.desktop.core.exception.DatabaseOutOfSyncException;
import com.headstrongpro.desktop.core.exception.EmptyInputException;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.model.entity.Company;
import com.headstrongpro.desktop.modelCollections.util.ActionType;
import com.headstrongpro.desktop.modelCollections.util.IDataAccessObject;
import com.headstrongpro.desktop.modelCollections.util.Synchronizable;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * company model collection
 */
public class DBCompany extends Synchronizable implements IDataAccessObject<Company> {

    private DBConnect dbConnect;
    private Date timestamp;

    public DBCompany() {
        timestamp = new Date(Calendar.getInstance().getTimeInMillis());
    }

    @Override
    public List<Company> getAll() throws ModelSyncException {
        List<Company> companies = new ArrayList<>();
        try {
            dbConnect = new DBConnect();
            String query = "SELECT [id], [name], [cvr], [street], [postal], [city], [country] FROM [companies]";
            ResultSet companyRS = dbConnect.getFromDataBase(query);
            while (companyRS.next())
                companies.add(new Company(companyRS.getInt("id"),
                        companyRS.getString("name"),
                        companyRS.getString("cvr"),
                        companyRS.getString("street"),
                        companyRS.getString("postal"),
                        companyRS.getString("city"),
                        companyRS.getString("country")));
            timestamp = setTimestamp();
        } catch (ConnectionException | SQLException e) {
            throw new ModelSyncException("Could not load companies.", e);
        }
        return companies;
    }

    @Override
    public Company getById(int id) throws ModelSyncException {
        Company company = null;
        try {
            dbConnect = new DBConnect();
            String query = "SELECT * FROM companies WHERE id=" + id + ";";
            ResultSet rs = dbConnect.getFromDataBase(query);
            rs.next();
            company = new Company(rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("cvr"),
                    rs.getString("street"),
                    rs.getString("postal"),
                    rs.getString("city"),
                    rs.getString("country"));
            timestamp = setTimestamp();
        } catch (ConnectionException | SQLException e) {
            throw new ModelSyncException("Could not retrieve object by ID", e);
        }
        return company;
    }

    @Override
    public Company persist(Company newCompany) throws ModelSyncException {
        try {
            if (!(newCompany.getName().isEmpty() && newCompany.getCvr().isEmpty() && newCompany.getStreet().isEmpty() && newCompany.getPostal().isEmpty() && newCompany.getCity().isEmpty() && newCompany.getCountry().isEmpty())) {
                dbConnect = new DBConnect();
                //language=TSQL
                String createCompanyQuery = "INSERT INTO companies(name, cvr, street, postal, city, country) VALUES (?, ?, ?, ?, ?, ?);";
                PreparedStatement preparedStatement = dbConnect.getConnection().prepareStatement(createCompanyQuery, PreparedStatement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, newCompany.getName());
                preparedStatement.setString(2, newCompany.getCvr());
                preparedStatement.setString(3, newCompany.getStreet());
                preparedStatement.setString(4, newCompany.getPostal());
                preparedStatement.setString(5, newCompany.getCity());
                preparedStatement.setString(6, newCompany.getCountry());
                preparedStatement.executeUpdate();
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        newCompany.setId(generatedKeys.getInt(1));
                        logChange("companies", newCompany.getId(), ActionType.CREATE);
                    } else {
                        throw new ModelSyncException("Creating company failed. No ID retrieved!");
                    }
                }
            } else {
                throw new EmptyInputException("All parameters must be filled in!");
            }
        } catch (EmptyInputException e) {
            throw new ModelSyncException("All parameters must be filled in!");
        } catch (ConnectionException | SQLException e) {
            throw new ModelSyncException("Could not persist new company!", e);
        }
        return newCompany;
    }

    @Override
    public void update(Company company) throws ModelSyncException, DatabaseOutOfSyncException {
        if (verifyIntegrity(company.getId())) {
            try {
                dbConnect = new DBConnect();
                //language=TSQL
                String updateCompanyQuery = "UPDATE companies SET name=?, cvr=?, street=?, postal=?, city=?, country=? WHERE id=?;";
                PreparedStatement preparedStatement = dbConnect.getConnection().prepareStatement(updateCompanyQuery);
                preparedStatement.setString(1, company.getName());
                preparedStatement.setString(2, company.getCvr());
                preparedStatement.setString(3, company.getStreet());
                preparedStatement.setString(4, company.getPostal());
                preparedStatement.setString(5, company.getCity());
                preparedStatement.setString(6, company.getCountry());
                preparedStatement.setInt(7, company.getId());
                dbConnect.uploadSafe(preparedStatement);
                logChange("companies", company.getId(), ActionType.UPDATE);
            } catch (SQLException | ConnectionException e) {
                throw new ModelSyncException("WARNING! Could not update the company of id [" + company.getId() + "]!", e);
            }
        } else {
            throw new DatabaseOutOfSyncException();
        }
    }

    @Override
    public void delete(Company company) throws ModelSyncException, DatabaseOutOfSyncException {
        if (verifyIntegrity(company.getId())) {
            try {
                dbConnect = new DBConnect();
                //language=TSQL
                String deleteCompanyQuery = "DELETE FROM companies WHERE id=?;";
                PreparedStatement preparedStatement = dbConnect.getConnection().prepareStatement(deleteCompanyQuery);
                preparedStatement.setInt(1, company.getId());
                preparedStatement.execute();
                logChange("companies", company.getId(), ActionType.DELETE);

                new DBDepartments().deleteByCompanyID(company.getId());
            } catch (ConnectionException | SQLException e) {
                throw new ModelSyncException("Couldn't delete the company of id=" + company.getId(), e);
            }
        } else {
            throw new DatabaseOutOfSyncException();
        }
    }

    public String getCompanyNameByID(int id) throws ModelSyncException {
        String result;
        try {
            dbConnect = new DBConnect();
            ResultSet rs = dbConnect.getFromDataBase("SELECT name FROM companies WHERE id=" + id + ";");
            rs.next();
            result = rs.getString(1);
        } catch (ConnectionException | SQLException e) {
            throw new ModelSyncException("Couldn't fetch the company name of id=" + id, e);
        }
        return result;
    }

    @Override
    protected boolean verifyIntegrity(int itemID) throws ModelSyncException {
        return verifyIntegrity(itemID, timestamp, "companies");
    }
}
