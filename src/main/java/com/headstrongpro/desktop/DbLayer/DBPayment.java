package com.headstrongpro.desktop.DbLayer;

import com.headstrongpro.desktop.core.connection.DBConnect;
import com.headstrongpro.desktop.core.exception.ConnectionException;
import com.headstrongpro.desktop.core.exception.DatabaseOutOfSyncException;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.model.Payment;
import com.headstrongpro.desktop.model.Subscription;
import com.headstrongpro.desktop.DbLayer.util.ActionType;
import com.headstrongpro.desktop.DbLayer.util.IDataAccessObject;
import com.headstrongpro.desktop.DbLayer.util.Synchronizable;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DBPayment extends Synchronizable implements IDataAccessObject<Payment> {

    private DBConnect dbConnect;

    public DBPayment() {
        updateTimestampLocal();
    }

    @Override
    public List<Payment> getAll() throws ModelSyncException {
        List<Payment> payments = new ArrayList<>();
        try {
            dbConnect = new DBConnect();
            String query = "SELECT * FROM payments";
            ResultSet rs = dbConnect.getFromDataBase(query);
            //DBPayment dbPayment = new DBPayment();
            DBSubscriptions dbSubs = new DBSubscriptions();
            while (rs.next()) {
                Subscription subscription = dbSubs.getById(rs.getInt("subscription_id"));
                payments.add(new Payment(
                        rs.getInt("id"),
                        rs.getDouble("value"),
                        rs.getDate("timestamp"),
                        rs.getDate("due_date"),
                        rs.getBoolean("paid"),
                        subscription
                ));
            }
            timestamp = setTimestamp();
        } catch (ConnectionException | SQLException e) {
            throw new ModelSyncException("Could not load subscriptions.", e);
        }
        return payments;
    }

    @Override
    public Payment getById(int id) throws ModelSyncException {
        Payment payment = null;
        try {
            dbConnect = new DBConnect();
            ResultSet rs = dbConnect.getFromDataBase("SELECT * FROM payments WHERE id=" + id + ";");
            rs.next();
            DBSubscriptions dbSubs = new DBSubscriptions();
            Subscription subscription = dbSubs.getById(rs.getInt("subscription_id"));
            payment = new Payment(
                    rs.getInt("id"),
                    rs.getDouble("value"),
                    rs.getDate("timestamp"),
                    rs.getDate("due_date"),
                    rs.getBoolean("paid"),
                    subscription
            );
            timestamp = setTimestamp();
        } catch (ConnectionException | SQLException e) {
            throw new ModelSyncException("Could not retrieve a payment!", e);
        }
        return payment;
    }

    @Override
    public Payment persist(Payment object) throws ModelSyncException {
        try {
            dbConnect = new DBConnect();
            //language=TSQL
            String createPaymentQuery = "INSERT INTO payments (subscription_id, value, timestamp, due_date, paid) VALUES (?, ?, ?, ?, ?);";
            PreparedStatement preparedStatement = dbConnect.getConnection().prepareStatement(createPaymentQuery, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, object.getSubscription().getId());
            preparedStatement.setDouble(2, object.getValue());
            preparedStatement.setDate(3, object.getTimestamp());
            preparedStatement.setDate(4, object.getDueDate());
            preparedStatement.setBoolean(5, object.isPaid());
            preparedStatement.executeUpdate();
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    object.setId(generatedKeys.getInt(1));
                    logChange("payments", object.getId(), ActionType.CREATE);
                } else {
                    throw new ModelSyncException("Creating payment failed. No ID retrieved!");
                }
            }
        } catch (ConnectionException | SQLException e) {
            throw new ModelSyncException("Could not persist new payment!", e);
        }
        return object;
    }

    @Override
    public void update(Payment object) throws ModelSyncException, DatabaseOutOfSyncException {
        if (verifyIntegrity(object.getId())) {
            try {
                dbConnect = new DBConnect();
                //language=TSQL
                String query = "UPDATE payments SET subscription_id=?,value=?,timestamp=?,due_date=?,paid=? WHERE id=?;";
                PreparedStatement preparedStatement = dbConnect.getConnection().prepareStatement(query);
                preparedStatement.setInt(1, object.getSubscription().getId());
                preparedStatement.setDouble(2, object.getValue());
                preparedStatement.setDate(3, object.getTimestamp());
                preparedStatement.setDate(4, object.getDueDate());
                preparedStatement.setBoolean(5, object.isPaid());
                preparedStatement.setInt(6, object.getId());
                dbConnect.uploadSafe(preparedStatement);
                logChange("payments", object.getId(), ActionType.UPDATE);
            } catch (ConnectionException | SQLException e) {
                throw new ModelSyncException("WARNING! Could not update payment of ID: " + object.getId() + " !", e);
            }
        } else {
            throw new DatabaseOutOfSyncException();
        }
    }

    @Override
    public void delete(Payment object) throws ModelSyncException, DatabaseOutOfSyncException {
        if (verifyIntegrity(object.getId())) {
            try {
                dbConnect = new DBConnect();
                //language=TSQL
                String query = "DELETE FROM payments WHERE id=?;";
                PreparedStatement preparedStatement = dbConnect.getConnection().prepareStatement(query);
                preparedStatement.setInt(1, object.getId());
                preparedStatement.execute();
                logChange("payments", object.getId(), ActionType.DELETE);
            } catch (ConnectionException | SQLException e) {
                throw new ModelSyncException("WARNING! Could not delete payment of ID: " + object.getId() + " !", e);
            }
        } else {
            throw new DatabaseOutOfSyncException();
        }
    }

    public List<Payment> getBySubscriptionId(int subscriptionId) throws ModelSyncException {
        List<Payment> payments = new ArrayList<>();
        try {
            dbConnect = new DBConnect();
            String query = "SELECT * FROM payments WHERE subscription_id=" + subscriptionId;
            ResultSet rs = dbConnect.getFromDataBase(query);
            DBSubscriptions dbSubs = new DBSubscriptions();
            while (rs.next()) {
                Subscription subscription = dbSubs.getById(rs.getInt("subscription_id"));
                payments.add(new Payment(
                        rs.getInt("id"),
                        rs.getDouble("value"),
                        rs.getDate("timestamp"),
                        rs.getDate("due_date"),
                        rs.getBoolean("paid"),
                        subscription
                ));
            }
            timestamp = setTimestamp();
        } catch (ConnectionException | SQLException e) {
            throw new ModelSyncException("Could not load subscriptions.", e);
        }
        return payments;
    }

    @Override
    protected boolean verifyIntegrity(int itemID) throws ModelSyncException {
        return verifyIntegrity(itemID, timestamp, "payments");
    }
}
