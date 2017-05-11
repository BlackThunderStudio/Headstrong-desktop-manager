package com.headstrongpro.desktop.modelCollections;

import com.headstrongpro.desktop.core.connection.DBConnect;
import com.headstrongpro.desktop.core.connection.IDataAccessObject;
import com.headstrongpro.desktop.core.exception.ConnectionException;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.model.Payment;
import com.headstrongpro.desktop.model.Subscription;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBPayment implements IDataAccessObject<Payment> {

    private DBConnect dbConnect;

    @Override
    public List<Payment> getAll() throws ModelSyncException {
        List<Payment> payments = new ArrayList<>();
        try {
            dbConnect = new DBConnect();
            String query = "SELECT * FROM [payments]";
            ResultSet rs = dbConnect.getFromDataBase(query);
            DBPayment dbPayment = new DBPayment();
            DBSubscriptions dbSubs = new DBSubscriptions();
            Subscription subscription = dbSubs.getById(rs.getInt("subscription_id"));
            while (rs.next()) {
                payments.add(new Payment(
                        rs.getInt("id"),
                        rs.getDouble("value"),
                        rs.getDate("timestamp"),
                        rs.getDate("due_date"),
                        rs.getBoolean("paid"),
                        subscription
                ));
            }
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
            ResultSet rs = dbConnect.getFromDataBase("SELECT * FROM payments WHERE id=" + id);
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
        } catch (ConnectionException | SQLException e){
            throw new ModelSyncException("Could not retrieve a department!", e);
        }
        return payment;
    }

    @Override
    public Payment create(Payment object) throws ModelSyncException {
        try {
            dbConnect = new DBConnect();
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
                } else {
                    throw new ModelSyncException("Creating payment failed. No ID retrieved!");
                }
            }
        } catch (ConnectionException | SQLException e) {
            throw new ModelSyncException("Could not create new payment!", e);
        }
        return object;
    }

    @Override
    public void update(Payment object) throws ModelSyncException {
        try {
            dbConnect = new DBConnect();
            String query = "UPDATE payments SET subscription_id=?,value=?,timestamp=?,due_date=?,paid=? WHERE id=?;";
            PreparedStatement preparedStatement = dbConnect.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, object.getSubscription().getId());
            preparedStatement.setDouble(2, object.getValue());
            preparedStatement.setDate(3, object.getTimestamp());
            preparedStatement.setDate(4, object.getDueDate());
            preparedStatement.setBoolean(5, object.isPaid());
            dbConnect.uploadSafe(preparedStatement);
        } catch (ConnectionException | SQLException e) {
            throw new ModelSyncException("WARNING! Could not update payment of ID: " + object.getId() + " !", e);
        }
    }

    @Override
    public void delete(Payment object) throws ModelSyncException {
        try {
            dbConnect = new DBConnect();
            String query = "DELETE FROM payments WHERE id=?;";
            PreparedStatement preparedStatement = dbConnect.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, object.getId());
            preparedStatement.execute();
        } catch (ConnectionException | SQLException e) {
            throw new ModelSyncException("WARNING! Could not delete payment of ID: " + object.getId() + " !", e);
        }
    }

    public List<Payment> getBySubscriptionId(int subscriptionId) throws ModelSyncException {
        List<Payment> payments = new ArrayList<>();
        try {
            dbConnect = new DBConnect();
            String query = "SELECT * FROM [payments] WHERE subscription_id=" + subscriptionId;
            ResultSet rs = dbConnect.getFromDataBase(query);
            DBSubscriptions dbSubs = new DBSubscriptions();
            Subscription subscription = dbSubs.getById(rs.getInt("subscription_id"));
            while (rs.next()) {
                payments.add(new Payment(
                        rs.getInt("id"),
                        rs.getDouble("value"),
                        rs.getDate("timestamp"),
                        rs.getDate("due_date"),
                        rs.getBoolean("paid"),
                        subscription
                ));
            }
        } catch (ConnectionException | SQLException e) {
            throw new ModelSyncException("Could not load subscriptions.", e);
        }
        return payments;
    }
}
