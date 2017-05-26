package com.headstrongpro.desktop.DbLayer;

import com.headstrongpro.desktop.core.connection.DBConnect;
import com.headstrongpro.desktop.core.exception.ConnectionException;
import com.headstrongpro.desktop.core.exception.DatabaseOutOfSyncException;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.model.entity.Client;
import com.headstrongpro.desktop.model.entity.EntityFactory;
import com.headstrongpro.desktop.model.entity.Person;
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

/**
 * Created by rajmu on 17.05.15.
 */
public class DBClient extends Synchronizable implements IDataAccessObject<Person> {
    private DBConnect connect;
    private Date timestamp;

    public DBClient() {
        timestamp = new Date(Calendar.getInstance().getTimeInMillis());
    }

    @Override
    public List<Person> getAll() throws ModelSyncException {
        List<Person> clients = new ArrayList<>();
        try {
            connect = new DBConnect();
            //language=TSQL
            String query = "SELECT * FROM clients";
            ResultSet rs = connect.getFromDataBase(query);
            while (rs.next()) {
                clients.add(EntityFactory.getClient(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        String.valueOf(rs.getInt("phone_number")),
                        (rs.getBoolean("gender")? "Male" : "Female"),
                        rs.getString("login"),
                        rs.getString("pass"),
                        rs.getDate("date_registered"),
                        rs.getInt("company_id")
                ));
            }
            timestamp = setTimestamp();
        } catch (ConnectionException | SQLException e) {
            throw new ModelSyncException("Could not retrieve the clients!", e);
        }
        return clients;
    }

    @Override
    public Person getById(int id) throws ModelSyncException {
        Person client = null;
        try {
            connect = new DBConnect();
            ResultSet rs = connect.getFromDataBase("SELECT * FROM clients WHERE id=" + id + ";");
            rs.next();
            client = new Client(rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    String.valueOf(rs.getInt("phone_number")),
                    String.valueOf(rs.getBoolean("gender")),
                    rs.getString("login"),
                    rs.getString("pass"),
                    rs.getDate("date_registered"),
                    rs.getInt("company_id"));
            timestamp = setTimestamp();
        } catch (ConnectionException | SQLException e) {
            throw new ModelSyncException("Could not retrieve the clients!", e);
        }
        return client;
    }

    @Override
    public Person persist(Person object) throws ModelSyncException {
        Client newClient = (Client) object;
        try {
            connect = new DBConnect();
            String query = "INSERT INTO clients(name, email, phone_number, gender, login, pass, date_registered, company_id) VALUES (?,?,?,?,?,?,?,?);";
            PreparedStatement preparedStatement = connect.getConnection().prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, newClient.getName());
            preparedStatement.setString(2, newClient.getEmail());
            preparedStatement.setString(3, newClient.getPhone());
            preparedStatement.setString(4, newClient.getGender());
            preparedStatement.setString(5, newClient.getLogin());
            preparedStatement.setString(6, newClient.getPassword());
            preparedStatement.setDate(7, newClient.getRegistrationDate());
            preparedStatement.setInt(8, newClient.getcompanyId());
            preparedStatement.executeUpdate();
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    newClient.setId(generatedKeys.getInt(1));
                    logChange("clients", newClient.getId(), ActionType.CREATE);
                } else {
                    throw new ModelSyncException("Creating new client failed! Could not retrieve the ID!");
                }
            }
        } catch (ConnectionException | SQLException e) {
            throw new ModelSyncException("Could not persist a client!", e);
        }
        return newClient;
    }

    @Override
    public void update(Person object) throws ModelSyncException, DatabaseOutOfSyncException {
        Client client = (Client) object;
        if (verifyIntegrity(client.getId())) {
            try {
                connect = new DBConnect();
                String query = "UPDATE clients SET name=?, email=?, phone_number=?, gender=?, login=?, pass=?, date_registered=?, company_id=? WHERE id=?;";
                PreparedStatement preparedStatement = connect.getConnection().prepareStatement(query);
                preparedStatement.setString(1, client.getName());
                preparedStatement.setString(2, client.getEmail());
                preparedStatement.setString(3, client.getPhone());
                preparedStatement.setString(4, client.getGender());
                preparedStatement.setString(5, client.getLogin());
                preparedStatement.setString(6, client.getPassword());
                preparedStatement.setDate(7, client.getRegistrationDate());
                preparedStatement.setInt(8, client.getcompanyId());
                preparedStatement.setInt(9, client.getId());
                connect.uploadSafe(preparedStatement);
                logChange("clients", client.getId(), ActionType.UPDATE);
            } catch (ConnectionException | SQLException e) {
                throw new ModelSyncException("Could not update a client!", e);
            }
        } else {
            throw new DatabaseOutOfSyncException();
        }
    }

    @Override
    public void delete(Person object) throws ModelSyncException, DatabaseOutOfSyncException {
        if (verifyIntegrity(object.getId())) {
            try {
                connect = new DBConnect();
                int clientID = object.getId();
                connect.upload("DELETE FROM clients WHERE id=" + clientID + ";");
                logChange("clients", clientID, ActionType.DELETE);

                ResultSet x = connect.getFromDataBase("SELECT id FROM achievements_clients WHERE client_id=" + clientID + "; " + "DELETE FROM achievements_clients WHERE client_id=" + clientID + ";");
                while (x.next()) {
                    logChange("achievements_clients", x.getInt(1), ActionType.DELETE);
                }

                x = connect.getFromDataBase("SELECT id FROM departments_clients WHERE client_id=" + clientID + "; " + "DELETE FROM departments_clients WHERE client_id=" + clientID + ";");
                while (x.next()) {
                    logChange("departments_clients", x.getInt(1), ActionType.DELETE);
                }

                x = connect.getFromDataBase("SELECT id FROM groups_clients WHERE client_id=" + clientID + "; " + "DELETE FROM groups_clients WHERE client_id=" + clientID + ";");
                while (x.next()) {
                    logChange("groups_clients", x.getInt(1), ActionType.DELETE);
                }

                x = connect.getFromDataBase("SELECT id FROM payments_clients WHERE clients_id=" + clientID + "; " + "DELETE FROM payments_clients WHERE clients_id=" + clientID + ";");
                while (x.next()) {
                    logChange("payments_clients", x.getInt(1), ActionType.DELETE);
                }
                x = connect.getFromDataBase("SELECT id FROM roles_clients WHERE client_id=" + clientID + "; " + "DELETE FROM roles_clients WHERE client_id=" + clientID + ";");
                while (x.next()) {
                    logChange("roles_clients", x.getInt(1), ActionType.DELETE);
                }
            } catch (ConnectionException | SQLException e) {
                throw new ModelSyncException("Could not delete a client!", e);
            }
        } else {
            throw new DatabaseOutOfSyncException();
        }
    }

    public List<Person> getByCompanyId(int id) throws ModelSyncException {
        List<Person> clients = new ArrayList<>();
        try {
            connect = new DBConnect();
            //language=TSQL
            String query = "SELECT * FROM clients WHERE company_id=" + id + ";";
            ResultSet rs = connect.getFromDataBase(query);
            while (rs.next()) {
                clients.add(EntityFactory.getClient(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        String.valueOf(rs.getInt("phone_number")),
                        String.valueOf(rs.getBoolean("gender")),
                        rs.getString("login"),
                        rs.getString("pass"),
                        rs.getDate("date_registered"),
                        rs.getInt("company_id")
                ));
            }
            timestamp = setTimestamp();
        } catch (ConnectionException | SQLException e) {
            throw new ModelSyncException("Could not fetch clients by a company ID!", e);
        }
        return clients;
    }

    @Override
    protected boolean verifyIntegrity(int itemID) throws ModelSyncException {
        return verifyIntegrity(itemID, timestamp, "clients");
    }
}
