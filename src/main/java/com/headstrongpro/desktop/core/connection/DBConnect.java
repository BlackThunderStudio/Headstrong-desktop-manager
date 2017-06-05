package com.headstrongpro.desktop.core.connection;

import com.headstrongpro.desktop.core.exception.ConnectionException;
import org.json.simple.JSONObject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DB Connection Utility
 */
public class DBConnect extends Configurable {

    private static String url, username, password;

    public DBConnect() throws ConnectionException {
        List<Object> credentials = getConfig();
        url = (String) credentials.get(0);
        username = (String) credentials.get(1);
        password = (String) credentials.get(2);
    }

    /**
     * Connects to the database
     *
     * @return returns connection object
     */
    private static Connection connect(String hostname, String user, String pass) throws ConnectionException {
        Connection con;
        try {
            con = DriverManager.getConnection(hostname, user, pass);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            throw new ConnectionException("Exception occurred while connecting to the database", ex);
        }
        return con;
    }

    /**
     * Returns the connection object
     *
     * @return Connection
     */
    public Connection getConnection() throws ConnectionException {
        return connect(url, username, password);
    }

    /**
     * Executes specified SQL query and returns the data from the table
     *
     * @return ResultSet
     */
    public ResultSet getFromDataBase(String query) throws ConnectionException {
        Connection con = connect(url, username, password);
        ResultSet rs;
        Statement statement;
        try {
            statement = con.createStatement();
            rs = statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ConnectionException(e);
        }
        return rs;
    }

    /**
     * Uploads data stated in the query to the database (UNSAFE)
     *
     * @param query an SQL query string
     */
    public void upload(String query) throws ConnectionException {
        Connection con = connect(url, username, password);
        try {
            Statement statement = con.createStatement();
            statement.execute(query);
            con.close();
        } catch (SQLException e) {
            throw new ConnectionException(e);
        }
    }

    /***
     * Uploads data stated in the query to the database (SAFE)
     * @param stmt preparedStatement object with protection against SQLi
     */
    public void uploadSafe(PreparedStatement stmt) throws ConnectionException {
        Connection con = connect(url, username, password);
        try {
            stmt.executeUpdate();
            con.close();
        } catch (SQLException ex) {
            throw new ConnectionException("WARNING! exception occurred while uploading a query to the server.", ex);
        }
    }

    @Override
    protected List<Object> getConfig() {
        JSONObject credentials = parseJsonConfig("/config.json", "database_mssql");
        List<Object> result = new ArrayList<>();
        result.add(credentials.get("url"));
        result.add(credentials.get("user"));
        result.add(credentials.get("pass"));
        return result;
    }
}
