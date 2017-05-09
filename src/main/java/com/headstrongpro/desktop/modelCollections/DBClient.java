package com.headstrongpro.desktop.modelCollections;

import com.headstrongpro.desktop.core.connection.DBConnect;
import com.headstrongpro.desktop.core.connection.IDataAccessObject;
import com.headstrongpro.desktop.core.exception.ConnectionException;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.model.entity.Client;
import com.headstrongpro.desktop.model.entity.EntityFactory;
import com.headstrongpro.desktop.model.entity.Person;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Clients model collection
 */
class DBClient implements IDataAccessObject<Person> {

    private DBConnect connect;

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
                        String.valueOf(rs.getBoolean("gender")),
                        rs.getString("login"),
                        rs.getString("pass"),
                        rs.getDate("date_registered"),
                        rs.getInt("company_id")
                ));
            }
        } catch (ConnectionException | SQLException e) {
            throw new ModelSyncException("Could not retrieve the clients!", e);
        }
        return clients;
    }

    @Override
    public Person getById(int id) throws ModelSyncException {
        Person client = null;
        try{
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
        } catch (ConnectionException | SQLException e) {
          throw new ModelSyncException("Could not retrieve the clients!", e);
        }
        return client;
    }

    @Override
    public Person create(Person object) throws ModelSyncException {
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
                } else {
                    throw new ModelSyncException("Creating new client failed! Could not retrieve the ID!");
                }
            }
        } catch (ConnectionException | SQLException e) {
            throw new ModelSyncException("Could not create a client!", e);
        }
        return newClient;
    }

    @Override
    public void update(Person object) throws ModelSyncException {
        Client client = (Client) object;
        try{
            connect = new DBConnect();
            String query = "UPDATE clients SET name=?, email=?, phone_number=?, gender=?, login=?, pass=?, date_registered=?, company_id=?;";
            PreparedStatement preparedStatement = connect.getConnection().prepareStatement(query);
            preparedStatement.setString(1, client.getName());
            preparedStatement.setString(2, client.getEmail());
            preparedStatement.setString(3, client.getPhone());
            preparedStatement.setString(4, client.getGender());
            preparedStatement.setString(5, client.getLogin());
            preparedStatement.setString(6, client.getPassword());
            preparedStatement.setDate(7, client.getRegistrationDate());
            preparedStatement.setInt(8, client.getcompanyId());
            connect.uploadSafe(preparedStatement);
        } catch (ConnectionException | SQLException e) {
            throw new ModelSyncException("Could not update a client!", e);
        }
    }

    @Override
    public void delete(Person object) throws ModelSyncException {
        try{
            connect = new DBConnect();
            connect.upload("DELETE FROM clients WHERE id=" + object.getId() + ";");
        } catch (ConnectionException e) {
            throw new ModelSyncException("Could not delete a client!", e);
        }
    }

    public List<Person> getByCompanyId(int id) {
        //TODO: to be implemented
        return null;
    }
}
