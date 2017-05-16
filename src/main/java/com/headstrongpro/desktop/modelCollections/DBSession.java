package com.headstrongpro.desktop.modelCollections;

import com.headstrongpro.desktop.core.connection.DBConnect;
import com.headstrongpro.desktop.core.exception.ConnectionException;
import com.headstrongpro.desktop.core.exception.DatabaseOutOfSyncException;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.model.Session;
import com.headstrongpro.desktop.modelCollections.util.ActionType;
import com.headstrongpro.desktop.modelCollections.util.IDataAccessObject;
import com.headstrongpro.desktop.modelCollections.util.Synchronizable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

/**
 * DB Sessions
 */
public class DBSession extends Synchronizable implements IDataAccessObject<Session> {

    private DBConnect dbConnect;
    private Date timestamp;

    public DBSession(){
        timestamp = new Date(Calendar.getInstance().getTimeInMillis());
    }

    @Override
    public ObservableList<Session> getAll() throws ModelSyncException {
        ObservableList sessions = FXCollections.observableArrayList();
        String selectQuery = "SELECT * FROM [sessions]";
        try {
            dbConnect = new DBConnect();
            ResultSet resultSet = dbConnect.getFromDataBase(selectQuery);
            while (resultSet.next()) {
                sessions.add(createObject(resultSet));
            }
            timestamp = setTimestamp();
        } catch (ConnectionException | SQLException e) {
            throw new ModelSyncException("Could not load sessions.", e);
        }
        return sessions;
    }

    @Override
    public Session getById(int id) throws ModelSyncException {
        Session session = null;
        String selectQuery = "SELECT * FROM [sessions] WHERE id = " + id;
        try {
            dbConnect = new DBConnect();
            ResultSet resultSet = dbConnect.getFromDataBase(selectQuery);
            if (resultSet.next()) {
                session = createObject(resultSet);
            }
            timestamp = setTimestamp();
        } catch (ConnectionException | SQLException e) {
            throw new ModelSyncException("Could not retrieve a session!", e);
        }
        return session;
    }

    @Override
    public Session create(Session object) throws ModelSyncException {
        try {
            dbConnect = new DBConnect();
            //language=TSQL
            String createSessionQuery = "INSERT INTO sessions (name, description) VALUES (?, ?)";
            PreparedStatement preparedStatement = dbConnect.getConnection().prepareStatement(createSessionQuery, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, object.getName());
            preparedStatement.setString(2, object.getDescription());
            preparedStatement.executeUpdate();
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    object.setId(generatedKeys.getInt(1));
                    logChange("sessions", object.getId(), ActionType.CREATE);
                } else {
                    throw new ModelSyncException("Creating session failed. No ID retrieved!");
                }
            }
        } catch (ConnectionException | SQLException e) {
            throw new ModelSyncException("Could not createObject new session!", e);
        }
        return object;
    }

    @Override
    public void update(Session object) throws ModelSyncException, DatabaseOutOfSyncException {
        if (verifyIntegrity(object.getId())){
            try {
                dbConnect = new DBConnect();
                //language=TSQL
                String updateSessionQuery = "UPDATE sessions SET name = ?, description = ? WHERE id = ?;";
                PreparedStatement preparedStatement = dbConnect.getConnection().prepareStatement(updateSessionQuery);
                preparedStatement.setString(1, object.getName());
                preparedStatement.setString(2, object.getDescription());
                preparedStatement.setInt(3, object.getId());
                dbConnect.uploadSafe(preparedStatement);
                logChange("sessions", object.getId(), ActionType.UPDATE);
            } catch (ConnectionException | SQLException e) {
                throw new ModelSyncException("WARNING! Could not update session of ID: " + object.getId() + " !", e);
            }
        } else {
            throw new DatabaseOutOfSyncException();
        }
    }

    @Override
    public void delete(Session object) throws ModelSyncException, DatabaseOutOfSyncException {
        if (verifyIntegrity(object.getId())){
            try {
                dbConnect = new DBConnect();
                //language=TSQL
                String deleteSessionQuery = "DELETE FROM sessions WHERE id = ?;";
                PreparedStatement preparedStatement = dbConnect.getConnection().prepareStatement(deleteSessionQuery);
                preparedStatement.setInt(1, object.getId());
                preparedStatement.execute();
                logChange("sessions", object.getId(), ActionType.DELETE);
            } catch (ConnectionException | SQLException e) {
                throw new ModelSyncException("WARNING! Could not delete session of ID: " + object.getId() + " !", e);
            }
        } else {
            throw new DatabaseOutOfSyncException();
        }
    }

    private Session createObject(ResultSet resultSet) throws SQLException {
        Session session = new Session();
        session.setId(resultSet.getInt("id"));
        session.setName(resultSet.getString("name"));
        session.setDescription(resultSet.getString("description"));
        return session;
    }

    @Override
    protected boolean verifyIntegrity(int itemID) throws ModelSyncException {
        return verifyIntegrity(itemID, timestamp, "sessions");
    }
}
