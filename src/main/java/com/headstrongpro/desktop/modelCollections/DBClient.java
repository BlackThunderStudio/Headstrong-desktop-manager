package com.headstrongpro.desktop.modelCollections;

import com.headstrongpro.desktop.core.connection.DBConnect;
import com.headstrongpro.desktop.core.connection.IDataAccessObject;
import com.headstrongpro.desktop.core.exception.ConnectionException;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.model.entity.EntityFactory;
import com.headstrongpro.desktop.model.entity.Person;

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
                        rs.getString("date_registered"),
                        rs.getInt("company_id")
                ));
            }
        } catch (ConnectionException | SQLException e) {
            throw new ModelSyncException("Could not retrieve the clients!", e);
        }
        return null;
    }

    @Override
    public Person getById(int id) throws ModelSyncException {
        //TODO: to be implemented
        return null;
    }

    @Override
    public Person create(Person object) throws ModelSyncException {
        //TODO: to be implemented
        return null;
    }

    @Override
    public void update(Person object) throws ModelSyncException {
        //TODO: to be implemented
    }

    @Override
    public void delete(Person object) throws ModelSyncException {
        //TODO: to be implemented
    }

    public List<Person> getByCompanyId(int id) {
        //TODO: to be implemented
        return null;
    }
}
