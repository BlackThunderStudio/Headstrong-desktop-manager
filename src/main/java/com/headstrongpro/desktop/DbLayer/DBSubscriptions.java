package com.headstrongpro.desktop.DbLayer;

import com.headstrongpro.desktop.DbLayer.util.ActionType;
import com.headstrongpro.desktop.DbLayer.util.IDataAccessObject;
import com.headstrongpro.desktop.DbLayer.util.Synchronizable;
import com.headstrongpro.desktop.core.connection.DBConnect;
import com.headstrongpro.desktop.core.exception.ConnectionException;
import com.headstrongpro.desktop.core.exception.DatabaseOutOfSyncException;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.model.PaymentRate;
import com.headstrongpro.desktop.model.Subscription;
import com.headstrongpro.desktop.model.entity.Company;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBSubscriptions extends Synchronizable implements IDataAccessObject<Subscription> {

    private DBConnect dbConnect;

    public DBSubscriptions() {
        updateTimestampLocal();
    }

    @Override
    public List<Subscription> getAll() throws ModelSyncException {
        List<Subscription> subscriptions = new ArrayList<>();
        try {
            dbConnect = new DBConnect();
            String query = "SELECT * FROM subscriptions";
            ResultSet rs = dbConnect.getFromDataBase(query);
            DBCompany dbCompany = new DBCompany();
            while (rs.next()) {
                Company company = dbCompany.getById(rs.getInt("company_id"));
                PaymentRate rate = getPaymentRateByID(rs.getInt("rate_id"));
                subscriptions.add(new Subscription(
                        rs.getInt("id"),
                        rs.getInt("no_of_users"),
                        rs.getBoolean("is_active"),
                        rs.getDate("start_date"),
                        rs.getDate("end_date"),
                        rate,
                        company
                ));
            }
            timestamp = setTimestamp();
        } catch (ConnectionException | SQLException e) {
            throw new ModelSyncException("Could not load subscriptions.", e);
        }
        return subscriptions;
    }

    public PaymentRate getPaymentRateByID(int id) throws ModelSyncException {
        PaymentRate rate;
        try {
            dbConnect = new DBConnect();
            String query = "SELECT * FROM [payment_rates] WHERE id=" + id + ";";
            ResultSet rs = dbConnect.getFromDataBase(query);
            rs.next();
            rate = new PaymentRate(
                    rs.getInt("id"),
                    rs.getInt("n_of_months"),
                    rs.getString("name"));
        } catch (ConnectionException | SQLException e) {
            throw new ModelSyncException("Could not load payment rates.", e);
        }
        return rate;
    }


    @Override
    public Subscription getById(int id) throws ModelSyncException {
        Subscription subscription;
        try {
            dbConnect = new DBConnect();
            String query = "SELECT * FROM [subscriptions] WHERE id=" + id + ";";
            ResultSet rs = dbConnect.getFromDataBase(query);
            rs.next();
            DBCompany dbCompany = new DBCompany();
            Company company = dbCompany.getById(rs.getInt("company_id"));
            PaymentRate rate = getPaymentRateByID(rs.getInt("rate_id"));
            subscription = new Subscription(
                    rs.getInt("id"),
                    rs.getInt("no_of_users"),
                    rs.getBoolean("is_active"),
                    rs.getDate("start_date"),
                    rs.getDate("end_date"),
                    rate,
                    company
            );
            timestamp = setTimestamp();
        } catch (ConnectionException | SQLException e) {
            throw new ModelSyncException("Could not load resources.", e);
        }
        return subscription;
    }

    @Override
    public Subscription persist(Subscription object) throws ModelSyncException {
        try {
            dbConnect = new DBConnect();
            //language=TSQL
            String createCompanyQuery = "INSERT INTO subscriptions (company_id, is_active, start_date, end_date, no_of_users, rate_id) VALUES (?, ?, ?, ?, ?, ?);";
            PreparedStatement preparedStatement = dbConnect.getConnection().prepareStatement(createCompanyQuery, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, object.getCompany().getId());
            preparedStatement.setBoolean(2, object.isActive());
            preparedStatement.setDate(3, object.getStartDate());
            preparedStatement.setDate(4, object.getEndDate());
            preparedStatement.setInt(5, object.getNoOfUsers());
            preparedStatement.setInt(6, object.getRate().getId());
            preparedStatement.executeUpdate();
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    object.setId(generatedKeys.getInt(1));
                    logChange("subscriptions", object.getId(), ActionType.CREATE);
                } else {
                    throw new ModelSyncException("Creating subscription failed. No ID retrieved!");
                }
            }
        } catch (ConnectionException | SQLException e) {
            throw new ModelSyncException("Could not persist new subscription!", e);
        }
        return object;
    }

    @Override
    public void update(Subscription object) throws ModelSyncException, DatabaseOutOfSyncException {
        if (verifyIntegrity(object.getId())) {
            try {
                dbConnect = new DBConnect();
                //language=TSQL
                String query = "UPDATE subscriptions SET company_id=?,is_active=?,start_date=?,end_date=?,no_of_users=?,rate_id=? WHERE id=?;";
                PreparedStatement preparedStatement = dbConnect.getConnection().prepareStatement(query);
                preparedStatement.setInt(1, object.getCompany().getId());
                preparedStatement.setBoolean(2, object.isActive());
                preparedStatement.setDate(3, object.getStartDate());
                preparedStatement.setDate(4, object.getEndDate());
                preparedStatement.setInt(5, object.getNoOfUsers());
                preparedStatement.setInt(6, object.getRate().getId());
                preparedStatement.setInt(7, object.getId());
                dbConnect.uploadSafe(preparedStatement);
                logChange("subscriptions", object.getId(), ActionType.UPDATE);
            } catch (ConnectionException | SQLException e) {
                throw new ModelSyncException("WARNING! Could not update resource of ID: " + object.getId() + " !", e);
            }
        } else {
            throw new DatabaseOutOfSyncException();
        }
    }

    @Override
    public void delete(Subscription object) throws ModelSyncException, DatabaseOutOfSyncException {
        if (verifyIntegrity(object.getId())) {
            try {
                dbConnect = new DBConnect();
                //language=TSQL
                String query = "DELETE FROM subscriptions WHERE id=?;";
                PreparedStatement preparedStatement = dbConnect.getConnection().prepareStatement(query);
                preparedStatement.setInt(1, object.getId());
                preparedStatement.execute();
                logChange("subscriptions", object.getId(), ActionType.DELETE);
            } catch (ConnectionException | SQLException e) {
                throw new ModelSyncException("WARNING! Could not update subscription of ID: " + object.getId() + " !", e);
            }
        } else {
            throw new DatabaseOutOfSyncException();
        }
    }

    public List<Subscription> getByCompanyId(int companyId) throws ModelSyncException {
        List<Subscription> subscriptions = new ArrayList<>();
        try {
            dbConnect = new DBConnect();
            String query = "SELECT * FROM [subscriptions] WHERE company_id=" + companyId;
            ResultSet rs = dbConnect.getFromDataBase(query);
            DBCompany dbCompany = new DBCompany();
            while (rs.next()) {
                Company company = dbCompany.getById(rs.getInt("company_id"));
                PaymentRate rate = getPaymentRateByID(rs.getInt("rate_id"));
                subscriptions.add(new Subscription(
                        rs.getInt("id"),
                        rs.getInt("no_of_users"),
                        rs.getBoolean("is_active"),
                        rs.getDate("start_date"),
                        rs.getDate("end_date"),
                        rate,
                        company
                ));
            }
            timestamp = setTimestamp();
        } catch (ConnectionException | SQLException e) {
            throw new ModelSyncException("Could not load subscriptions.", e);
        }
        return subscriptions;
    }

    @Override
    protected boolean verifyIntegrity(int itemID) throws ModelSyncException {
        return verifyIntegrity(itemID, timestamp, "subscriptions");
    }

    public List<PaymentRate> getRates() throws ConnectionException {
        List<PaymentRate> res = new ArrayList<>();
        try {
            dbConnect = new DBConnect();
            String query = "SELECT * FROM payment_rates";
            ResultSet rs = dbConnect.getFromDataBase(query);
            while (rs.next()) {
                res.add(new PaymentRate(
                        rs.getInt("id"),
                        rs.getInt("n_of_months"),
                        rs.getString("name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }
}
