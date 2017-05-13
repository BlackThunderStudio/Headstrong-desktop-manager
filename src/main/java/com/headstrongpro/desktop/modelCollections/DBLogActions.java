package com.headstrongpro.desktop.modelCollections;

import com.headstrongpro.desktop.core.connection.DBConnect;
import com.headstrongpro.desktop.core.connection.IDataAccessObject;
import com.headstrongpro.desktop.core.exception.ConnectionException;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.model.Log;

import javax.ws.rs.DELETE;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rajmu on 17.05.09.
 */
public class DBLogActions implements IDataAccessObject<Log> {

    private DBConnect connect;

    @Override
    public List<Log> getAll() throws ModelSyncException {
        List<Log> logs = new ArrayList<>();
        try {
            connect = new DBConnect();
            //language=TSQL
            String query = "SELECT * FROM log_actions";
            ResultSet rs = connect.getFromDataBase(query);
            while (rs.next()){
                logs.add(new Log(
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getString(3),
                        rs.getInt(4),
                        rs.getDate(5),
                        rs.getString(6),
                        rs.getString(7)
                ));
            }
        } catch (ConnectionException | SQLException e) {
            throw new ModelSyncException("Could not load logs.", e);
        }
        return logs;
    }

    @Override
    public Log getById(int id) throws ModelSyncException {
        Log log = null;
        try {
            connect = new DBConnect();
            ResultSet rs = connect.getFromDataBase("SELECT * FROM log_actions WHERE id=" + id + ";");
            rs.next();
            log = new Log(
                    rs.getInt(1),
                    rs.getInt(2),
                    rs.getString(3),
                    rs.getInt(4),
                    rs.getDate(5),
                    rs.getString(6),
                    rs.getString(7)
            );
        } catch (ConnectionException | SQLException e) {
            throw new ModelSyncException("Could not load a log by ID.", e);
        }
        return log;
    }

    @Override
    public Log create(Log object) throws ModelSyncException {
        try {
            connect = new DBConnect();
            //language=TSQL
            String query = "INSERT INTO log_actions(headstrong_employee_id, table_name, item_id, action_type) VALUES (?,?,?,?);";
            PreparedStatement preparedStatement = connect.getConnection().prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, object.getHeadstrongEmpID());
            preparedStatement.setString(2, object.getTableName());
            preparedStatement.setInt(3, object.getItemID());
            preparedStatement.setString(4, object.getActionType());
            preparedStatement.executeUpdate();
            try(ResultSet generatedKeys = preparedStatement.getGeneratedKeys()){
                if(generatedKeys.next()){
                    object.setId(generatedKeys.getInt(1));
                } else {
                    throw new ModelSyncException("Creating a log record failed. No ID retrieved!");
                }
            }
        } catch (ConnectionException | SQLException e) {
            throw new ModelSyncException("Could not create a new log entry!", e);
        }
        return object;
    }

    @Deprecated
    @Override
    public void update(Log object) throws ModelSyncException {
        //user shouldn't be able to update logs
        throw new ModelSyncException("This method shall not be used!");
    }

    @Deprecated
    @Override
    public void delete(Log object) throws ModelSyncException {
        //user shouldn't be able to delete logs
        throw new ModelSyncException("This method shall not be used!");
    }

    public List<Log> getByTable(String table) throws ModelSyncException {
        List<Log> logs = new ArrayList<>();
        try {
            connect = new DBConnect();
            //language=TSQL
            String query = "SELECT * FROM log_actions WHERE table_name='" + table + "';";
            ResultSet rs = connect.getFromDataBase(query);
            while (rs.next()){
                logs.add(new Log(
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getString(3),
                        rs.getInt(4),
                        rs.getDate(5),
                        rs.getString(6),
                        rs.getString(7)
                ));
            }
        } catch (ConnectionException | SQLException e) {
            throw new ModelSyncException("Could not load logs.", e);
        }
        return logs;
    }
}
