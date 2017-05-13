package com.headstrongpro.desktop.core.connection;

import com.headstrongpro.desktop.core.exception.ConnectionException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;

/**
 * Created by rajmu on 17.04.06.
 */
public class DBConnect {

    private static String url;
    private static String username;
    private static String password;

    public DBConnect() throws ConnectionException {
        parseJson();
    }

    private void parseJson() throws ConnectionException {
        JSONParser parser = new JSONParser();
        try {
            Object file = parser.parse(new InputStreamReader(new FileInputStream(getClass().getResource("/config.json").getFile())));

            JSONObject fullJson = (JSONObject) file;
            JSONObject dbJson = (JSONObject) fullJson.get("database_mssql");

            url = (String) dbJson.get("url");
            username = (String) dbJson.get("user");
            password = (String) dbJson.get("pass");

        } catch (ParseException | IOException e) {
            throw new ConnectionException(e);
        }
    }

    /***
     * Connects to thee database
     * @return returns connection object
     */
    private static Connection connect(String hostname, String user, String pass) throws ConnectionException {
        Connection con = null;
        try {
            con = DriverManager.getConnection(hostname, user, pass);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            throw new ConnectionException("Exception occurred while connecting to the database", ex);
        }
        return con;
    }

    /***
     * Returns the connection object
     * @return Connection
     */
    public Connection getConnection() throws ConnectionException {
        return connect(url, username, password);
    }

    /***
     * Test the connection
     * @return returns True when connected, otherwise false
     */
    public static boolean testConnection(String host, String user, String pass) {
        try (Connection conn = DriverManager.getConnection(host, user, pass)) {
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }

    /***
     * Executes specified SQL query and returns the data from the table
     */
    public ResultSet getFromDataBase(String query) throws ConnectionException {
        Connection con = connect(url, username, password);
        ResultSet rs = null;
        Statement statement = null;
        try {
            statement = con.createStatement();
            rs = statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ConnectionException(e);
        }
        return rs;
    }

    /***
     * Uploads data stated in the query to the database (UNSAFE)
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
            int affectedRows = stmt.executeUpdate();
            con.close();
        } catch (SQLException ex) {
            throw new ConnectionException("WARNING! exception occurred while uploading a query to the server.", ex);
        }
    }
}
