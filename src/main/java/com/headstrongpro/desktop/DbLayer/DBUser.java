package com.headstrongpro.desktop.DbLayer;

import com.headstrongpro.desktop.DbLayer.util.ActionType;
import com.headstrongpro.desktop.DbLayer.util.IDataAccessObject;
import com.headstrongpro.desktop.DbLayer.util.Synchronizable;
import com.headstrongpro.desktop.core.connection.DBConnect;
import com.headstrongpro.desktop.core.exception.ConnectionException;
import com.headstrongpro.desktop.core.exception.DatabaseOutOfSyncException;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.model.entity.Person;
import com.headstrongpro.desktop.model.entity.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DB User
 */
public class DBUser extends Synchronizable implements IDataAccessObject<Person> {

    private DBConnect dbConnect;

    public DBUser() {
        updateTimestampLocal();
    }

    @Override
    public ObservableList<Person> getAll() throws ModelSyncException {
        ObservableList<Person> users = FXCollections.observableArrayList();
        String selectQuery = "SELECT * FROM [employees_headstrong];";
        try {
            dbConnect = new DBConnect();
            ResultSet resultSet = dbConnect.getFromDataBase(selectQuery);
            while (resultSet.next()) {
                users.add(create(resultSet));
            }
            timestamp = setTimestamp();
        } catch (ConnectionException | SQLException e) {
            throw new ModelSyncException("Could not retrieve users!", e);
        }
        return users;
    }

    @Override
    public Person getById(int id) throws ModelSyncException {
        Person person = null;
        String selectQuery = "SELECT * FROM [employees_headstrong] WHERE id = " + id + ";";
        try {
            dbConnect = new DBConnect();
            ResultSet resultSet = dbConnect.getFromDataBase(selectQuery);
            if (resultSet.next()) {
                person = create(resultSet);
            }
            timestamp = setTimestamp();
        } catch (ConnectionException | SQLException e) {
            throw new ModelSyncException("Could not retrieve the user!", e);
        }
        return person;
    }

    public Person getByCredentials(String username, String password) throws ModelSyncException {
        Person person;
        String selectQuery = "SELECT * FROM [employees_headstrong]" +
                " WHERE login = '" + username + "'";
        String selectQueryWithPassword = selectQuery + " AND pass = '" + password + "';";
        selectQuery += ";";
        try {
            dbConnect = new DBConnect();
            ResultSet resultSetWithPassword = dbConnect.getFromDataBase(selectQueryWithPassword);
            if (resultSetWithPassword.next()) {
                person = create(resultSetWithPassword);
            } else {
                ResultSet resultSet = dbConnect.getFromDataBase(selectQuery);
                throw new IllegalArgumentException(
                        resultSet.next() ? "The password entered is incorrect." : "The user does not exist."
                );
            }
            timestamp = setTimestamp();
        } catch (ConnectionException | SQLException e) {
            throw new ModelSyncException("Could not retrieve the user!", e);
        }
        return person;
    }

    @Override
    public Person persist(Person object) throws ModelSyncException {
        User user = (User) object;
        try {
            dbConnect = new DBConnect();
            //language=TSQL
            String query = "INSERT INTO employees_headstrong(name, cpr, street, postal, city, country, email, phone_number, bank_account_n, gender, base_salary) VALUES (?,?,?,?,?,?,?,?,?,?,?);";
            PreparedStatement preparedStatement = dbConnect.getConnection().prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
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
                    logChange("employees_headstrong", user.getId(), ActionType.CREATE);
                } else {
                    throw new ModelSyncException("Creating new user failed! Could not retrieve the ID!");
                }
            }
        } catch (ConnectionException | SQLException e) {
            throw new ModelSyncException("Could not persist a user!", e);
        }
        return user;
    }

    @Override
    public void update(Person object) throws ModelSyncException, DatabaseOutOfSyncException {
        User user = (User) object;
        if (verifyIntegrity(object.getId())) {
            try {
                dbConnect = new DBConnect();
                //language=TSQL
                String query = "UPDATE employees_headstrong SET name=?,street=?,postal=?,city=?,country=?,email=?,phone_number=?,bank_account_n=?,gender=?,base_salary=? WHERE id=?;";
                PreparedStatement preparedStatement = dbConnect.getConnection().prepareStatement(query);
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
                dbConnect.uploadSafe(preparedStatement);
                logChange("employees_headstrong", object.getId(), ActionType.UPDATE);
            } catch (ConnectionException | SQLException e) {
                throw new ModelSyncException("Could not update a user!", e);
            }
        } else {
            throw new DatabaseOutOfSyncException();
        }
    }

    @Override
    public void delete(Person object) throws ModelSyncException, DatabaseOutOfSyncException {
        if (verifyIntegrity(object.getId())) {
            try {
                dbConnect = new DBConnect();
                dbConnect.upload("DELETE FROM employees_headstrong  WHERE id=" + object.getId() + ";");
                logChange("employees_headstrong", object.getId(), ActionType.DELETE);
            } catch (ConnectionException e) {
                throw new ModelSyncException("Could not delete a user!", e);
            }
        } else {
            throw new DatabaseOutOfSyncException();
        }
    }

    @Override
    protected boolean verifyIntegrity(int itemID) throws ModelSyncException {
        return verifyIntegrity(itemID, timestamp, "employees_headstrong");
    }

    private User create(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt("id"));
        user.setName(resultSet.getString("name"));
        user.setEmail(resultSet.getString("email"));
        user.setPhone(resultSet.getString("phone_number"));
        user.setGender(String.valueOf(resultSet.getBoolean("gender")));
        user.setCpr(String.valueOf(resultSet.getBigDecimal("cpr")));
        user.setStreet(resultSet.getString("street"));
        user.setPostal(resultSet.getString("postal"));
        user.setCity(resultSet.getString("city"));
        user.setCountry(resultSet.getString("country"));
        user.setAccountNo(resultSet.getString("bank_account_n"));
        user.setBaseSalary(String.valueOf(resultSet.getBigDecimal("base_salary")));
        return user;
    }
}
