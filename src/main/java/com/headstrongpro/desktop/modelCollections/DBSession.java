package com.headstrongpro.desktop.modelCollections;

import com.headstrongpro.desktop.core.connection.DBConnect;
import com.headstrongpro.desktop.core.connection.IDataAccessObject;
import com.headstrongpro.desktop.core.exception.ConnectionException;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.model.Session;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DB Sessions
 */
public class DBSession implements IDataAccessObject<Session> {

    private DBConnect dbConnect;

    @Override
    public List<Session> getAll() throws ModelSyncException {
        List<Session> sessions = new ArrayList<>();
        try {
            dbConnect = new DBConnect();
            String query = "SELECT * FROM [sessions]";
            ResultSet rs = dbConnect.getFromDataBase(query);
            while (rs.next()) {
                sessions.add(new Session(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description")
                ));
            }
        } catch (ConnectionException | SQLException e) {
            throw new ModelSyncException("Could not load sessions.", e);
        }
        return sessions;
    }

    @Override
    public Session getById(int id) throws ModelSyncException {
        Session session;
        try {
            dbConnect = new DBConnect();
            ResultSet rs = dbConnect.getFromDataBase("SELECT * FROM sessions WHERE id=" + id);
            rs.next();
            session = new Session(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("description")
            );
        } catch (ConnectionException | SQLException e) {
            throw new ModelSyncException("Could not retrieve a session!", e);
        }
        return session;
    }

    @Override
    public Session create(Session object) throws ModelSyncException {
        try {
            dbConnect = new DBConnect();
            String createSessionQuery = "INSERT INTO sessions (name, description) VALUES (?, ?)";
            PreparedStatement preparedStatement = dbConnect.getConnection().prepareStatement(createSessionQuery, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, object.getName());
            preparedStatement.setString(2, object.getDescription());
            preparedStatement.executeUpdate();
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    object.setId(generatedKeys.getInt(1));
                } else {
                    throw new ModelSyncException("Creating session failed. No ID retrieved!");
                }
            }
        } catch (ConnectionException | SQLException e) {
            throw new ModelSyncException("Could not create new session!", e);
        }
        return object;
    }

    @Override
    public void update(Session object) throws ModelSyncException {
        try {
            dbConnect = new DBConnect();
            String updateSessionQuery = "UPDATE sessions SET name = ?, description = ? WHERE id = ?;";
            PreparedStatement preparedStatement = dbConnect.getConnection().prepareStatement(updateSessionQuery);
            preparedStatement.setString(1, object.getName());
            preparedStatement.setString(2, object.getDescription());
            preparedStatement.setInt(3, object.getId());
            dbConnect.uploadSafe(preparedStatement);
        } catch (ConnectionException | SQLException e) {
            throw new ModelSyncException("WARNING! Could not update session of ID: " + object.getId() + " !", e);
        }
    }

    @Override
    public void delete(Session object) throws ModelSyncException {
        try {
            dbConnect = new DBConnect();
            String deleteSessionQuery = "DELETE FROM sessions WHERE id = ?;";
            PreparedStatement preparedStatement = dbConnect.getConnection().prepareStatement(deleteSessionQuery);
            preparedStatement.setInt(1, object.getId());
            preparedStatement.execute();
        } catch (ConnectionException | SQLException e) {
            throw new ModelSyncException("WARNING! Could not delete session of ID: " + object.getId() + " !", e);
        }
    }
}
