package com.headstrongpro.desktop.DbLayer;

import com.headstrongpro.desktop.DbLayer.util.IStatistical;
import com.headstrongpro.desktop.core.connection.DBConnect;
import com.headstrongpro.desktop.core.exception.ConnectionException;
import com.headstrongpro.desktop.core.exception.ModelSyncException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DB Stats
 */
public class DBStats implements IStatistical {

    private DBConnect dbContext;

    public DBStats() {
        dbContext = new DBConnect();
    }

    @Override
    public int getAmountOfRecords(String tableName) throws ModelSyncException {
        String query = "DECLARE @TableName VARCHAR(64)\n" +
                "SET @TableName = ?\n" +
                "EXEC('SELECT COUNT(*) FROM ' + @TableName);";
        int rows;
        try {
            PreparedStatement preparedStatement = dbContext.getConnection().prepareStatement(query);
            preparedStatement.setString(1, tableName);
            ResultSet rs = preparedStatement.executeQuery();
            rs.next();
            rows = rs.getInt(1);
        } catch (SQLException | ConnectionException e) {
            throw new ModelSyncException(e);
        }
        return rows;
    }

    @Override
    public int getOverduePaymentsCount() {
        String query = "SELECT COUNT(*) FROM payments WHERE due_date >= CURRENT_TIMESTAMP";
        int rows = 0;
        try {
            PreparedStatement preparedStatement = dbContext.getConnection().prepareStatement(query);
            ResultSet rs = preparedStatement.executeQuery();
            rs.next();
            rows = rs.getInt(1);
        } catch (SQLException | ConnectionException e) {
            e.printStackTrace();
        }
        return rows;
    }

    @Override
    public int getActiveSubscriptionsCount() {
        String query = "SELECT COUNT(*) FROM subscriptions WHERE end_date > CURRENT_TIMESTAMP";
        int rows = 0;
        try {
            ResultSet rs = dbContext.getFromDataBase(query);
            rs.next();
            rows = rs.getInt(1);
        } catch (SQLException | ConnectionException e) {
            e.printStackTrace();
        }
        return rows;
    }
}
