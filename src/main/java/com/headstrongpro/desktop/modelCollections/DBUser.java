package com.headstrongpro.desktop.modelCollections;

import com.headstrongpro.desktop.core.connection.DBConnect;
import com.headstrongpro.desktop.core.connection.IDataAccessObject;
import com.headstrongpro.desktop.core.exception.ConnectionException;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.model.entity.EntityFactory;
import com.headstrongpro.desktop.model.entity.Person;
import com.headstrongpro.desktop.model.entity.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rajmund Staniek on 09-May-17.
 */
class DBUser implements IDataAccessObject<Person> {

    private DBConnect connect;

    @Override
    public List<Person> getAll() throws ModelSyncException {
        List<Person> users = new ArrayList<>();
        try {
            connect = new DBConnect();
            String query = "SELECT * FROM employees_headstrong;";

            ResultSet rs = connect.getFromDataBase(query);
            while (rs.next()) {
                users.add(EntityFactory.getUser(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone_number"),
                        String.valueOf(rs.getBoolean("gender")),
                        String.valueOf(rs.getBigDecimal("cpr")),
                        rs.getString("street"),
                        rs.getString("postal"),
                        rs.getString("city"),
                        rs.getString("country"),
                        rs.getString("bank_account_n"),
                        String.valueOf(rs.getBigDecimal("base_salary"))
                ));
            }
        } catch (ConnectionException | SQLException e) {
            throw new ModelSyncException("Could not retrieve the users!", e);
        }
        return users;
    }

    @Override
    public Person getById(int id) throws ModelSyncException {
        Person person = null;
        try {
            connect = new DBConnect();
            ResultSet rs = connect.getFromDataBase("SELECT * FROM employees_headstrong WHERE id=" + id + ";");
            rs.next();
            person = EntityFactory.getUser(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("phone_number"),
                    String.valueOf(rs.getBoolean("gender")),
                    String.valueOf(rs.getBigDecimal("cpr")),
                    rs.getString("street"),
                    rs.getString("postal"),
                    rs.getString("city"),
                    rs.getString("country"),
                    rs.getString("bank_account_n"),
                    String.valueOf(rs.getBigDecimal("base_salary"))
            );
        } catch (ConnectionException | SQLException e) {
            throw new ModelSyncException("Could not retrieve a user!", e);
        }
        return person;
    }

    @Override
    public Person create(Person object) throws ModelSyncException {
        User user = (User) object;
        try {
            connect = new DBConnect();
            String query = "INSERT INTO employees_headstrong(name, cpr, street, postal, city, country, email, phone_number, bank_account_n, gender, base_salary) VALUES (?,?,?,?,?,?,?,?,?,?,?);";
            PreparedStatement preparedStatement = connect.getConnection().prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getCpr());
            preparedStatement.setString(3, user.getStreet());
            preparedStatement.setString(4, user.getPostal());
            preparedStatement.setString(5, user.getCity());
            preparedStatement.setString(6, user.getCountry());
            preparedStatement.setString(7, user.getEmail());
            preparedStatement.setString(8, user.getPhone());
            preparedStatement.setString(9, user.getAccountNo());
            preparedStatement.setString(10, user.getGender());
            preparedStatement.setString(11, user.getBaseSalary());
            preparedStatement.executeUpdate();
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getInt(1));
                } else {
                    throw new ModelSyncException("Creating new user failed! Could not retrieve the ID!");
                }
            }
        } catch (ConnectionException | SQLException e) {
            throw new ModelSyncException("Could not create a user!", e);
        }
        return user;
    }

    @Override
    public void update(Person object) throws ModelSyncException {
        User user = (User) object;
        try {
            connect = new DBConnect();
            //language=TSQL
            String query = "UPDATE employees_headstrong SET name=?,street=?,postal=?,city=?,country=?,email=?,phone_number=?,bank_account_n=?,gender=?,base_salary=? WHERE id=?;";
            PreparedStatement preparedStatement = connect.getConnection().prepareStatement(query);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getStreet());
            preparedStatement.setString(3, user.getPostal());
            preparedStatement.setString(4, user.getCity());
            preparedStatement.setString(5, user.getCountry());
            preparedStatement.setString(6, user.getEmail());
            preparedStatement.setString(7, user.getPhone());
            preparedStatement.setString(8, user.getAccountNo());
            preparedStatement.setString(9, user.getGender());
            preparedStatement.setString(10, user.getBaseSalary());
            preparedStatement.setInt(11, user.getId());
            connect.uploadSafe(preparedStatement);
        } catch (ConnectionException | SQLException e) {
            throw new ModelSyncException("Could not update a user!", e);
        }
    }

    @Override
    public void delete(Person object) throws ModelSyncException {
        try {
            connect = new DBConnect();
            connect.upload("DELETE FROM employees_headstrong  WHERE id=" + object.getId() + ";");
        } catch (ConnectionException e) {
            throw new ModelSyncException("Could not delete a department!", e);
        }
    }
}
