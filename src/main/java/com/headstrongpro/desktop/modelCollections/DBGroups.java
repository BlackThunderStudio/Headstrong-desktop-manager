package com.headstrongpro.desktop.modelCollections;

import com.headstrongpro.desktop.core.connection.DBConnect;
import com.headstrongpro.desktop.core.exception.ConnectionException;
import com.headstrongpro.desktop.core.exception.DatabaseOutOfSyncException;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.model.Group;
import com.headstrongpro.desktop.modelCollections.util.ActionType;
import com.headstrongpro.desktop.modelCollections.util.IDataAccessObject;
import com.headstrongpro.desktop.modelCollections.util.Synchronizable;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * groups model collection
 */
public class DBGroups extends Synchronizable implements IDataAccessObject<Group> {
    private DBConnect connect;
    private Date timestamp;
    
    public DBGroups() {
        timestamp = new java.sql.Date(Calendar.getInstance().getTimeInMillis());
    }

    @Override
    public List<Group> getAll() throws ModelSyncException {
        List<Group> groups = new ArrayList<>();
        try {
            connect = new DBConnect();
            //language=TSQL
            String query = "SELECT * FROM groups";
            ResultSet rs = connect.getFromDataBase(query);
            while (rs.next()) {
                groups.add(new Group(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("company_id"),
                        rs.getInt("parent_id")
                ));
            }
            timestamp = setTimestamp();
        } catch (ConnectionException | SQLException e) {
            throw new ModelSyncException("Could not retrieve groups!", e);
        }
        return groups;
    }

    @Override
    public Group getById(int id) throws ModelSyncException {
        Group group = null;
        try {
            connect = new DBConnect();
            ResultSet rs = connect.getFromDataBase("SELECT * FROM groups WHERE id=" + id);
            rs.next();
            group = new Group(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("company_id"),
                    rs.getInt("parent_id"));
            timestamp = setTimestamp();
        } catch (ConnectionException | SQLException e) {
            throw new ModelSyncException("Could not retrieve a group!", e);
        }
        return group;
    }

    @Override
    public Group persist(Group object) throws ModelSyncException {
        try {
            connect = new DBConnect();
            //language=TSQL
            String query = "INSERT INTO groups(name, company_id, parent_id) VALUES (?,?,?);";
            PreparedStatement preparedStatement = connect.getConnection().prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, object.getName());
            preparedStatement.setInt(2, object.getCompanyId());
            preparedStatement.setInt(3, object.getParentId());
            preparedStatement.executeUpdate();
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    object.setId(generatedKeys.getInt(1));
                    logChange("groups", object.getId(), ActionType.CREATE);
                    timestamp = setTimestamp();
                } else {
                    throw new ModelSyncException("Creating new group failed! No ID retrieved!");
                }
            }
        } catch (ConnectionException | SQLException e) {
            throw new ModelSyncException("Could not persist a group!", e);
        }
        return object;
    }

    @Override
    public void update(Group object) throws ModelSyncException, DatabaseOutOfSyncException {
        if (verifyIntegrity(object.getId())) {
            try {
                connect = new DBConnect();
                //language=TSQL
                String query = "UPDATE groups SET name=?,description=?,company_id=? WHERE id=?;";
                PreparedStatement preparedStatement = connect.getConnection().prepareStatement(query);
                preparedStatement.setString(1, object.getName());
                preparedStatement.setInt(2, object.getCompanyId());
                preparedStatement.setInt(3, object.getParentId());
                preparedStatement.setInt(4, object.getId());
                connect.uploadSafe(preparedStatement);
                logChange("groups", object.getId(), ActionType.UPDATE);
            } catch (ConnectionException | SQLException e) {
                throw new ModelSyncException("Could not update a group!", e);
            }
        } else {
            throw new DatabaseOutOfSyncException();
        }
    }

    @Override
    public void delete(Group object) throws ModelSyncException, DatabaseOutOfSyncException {
        if (verifyIntegrity(object.getId())) {
            try {
                connect = new DBConnect();
                connect.upload("DELETE FROM groups WHERE id=" + object.getId());
                logChange("groups", object.getId(), ActionType.DELETE);

                ResultSet x = connect.getFromDataBase("SELECT id FROM groups_clients WHERE group_id=" + object.getId() + "; " + "DELETE FROM groups_clients WHERE group_id=" + object.getId());
                while (x.next()) {
                    logChange("groups_clients", x.getInt(1), ActionType.DELETE);
                }
            } catch (ConnectionException | SQLException e) {
                throw new ModelSyncException("Could not delete a group!", e);
            }
        } else {
            throw new DatabaseOutOfSyncException();
        }
    }

    public List<Group> getByCompanyID(int id) throws ModelSyncException {
        List<Group> groups = new ArrayList<>();
        try {
            connect = new DBConnect();
            //language=TSQL
            String query = "SELECT * FROM groups WHERE company_id=" + id + ";";
            ResultSet rs = connect.getFromDataBase(query);
            while (rs.next()) {
                groups.add(new Group(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("company_id"),
                        rs.getInt("parent_id")
                ));
            }
        } catch (ConnectionException | SQLException e) {
            throw new ModelSyncException("Could not fetch groups by company ID!", e);
        }
        return groups;
    }

    public void deleteByCompanyID(int id) throws ModelSyncException, DatabaseOutOfSyncException {
        if (verifyIntegrity(id)) {
            try {
                connect = new DBConnect();
                ResultSet rs = connect.getFromDataBase("SELECT id FROM groups WHERE company_id=" + id);
                List<Integer> deptIDs = new ArrayList<>();
                while (rs.next()) {
                    deptIDs.add(rs.getInt(1));
                }
                String simplifiedDeptIDs = "";
                for (int i = 0; i < deptIDs.size() - 1; i++) {
                    simplifiedDeptIDs += deptIDs.get(i) + ",";
                }
                if (deptIDs.size() != 0) simplifiedDeptIDs += deptIDs.get(deptIDs.size() - 1);

                //language=TSQL
                String query = "SELECT id FROM groups WHERE company_id=" + id + "; DELETE FROM groups WHERE company_id=" + id + ";";
                ResultSet x = connect.getFromDataBase(query);
                while (x.next()) {
                    logChange("groups", x.getInt(1), ActionType.DELETE);
                }

                if (deptIDs.size() != 0) {
                    query = "SELECT id FROM groups_clients WHERE group_id IN (" + simplifiedDeptIDs + "); DELETE FROM groups_clients WHERE group_id IN(" + simplifiedDeptIDs + ");";
                    ResultSet x2 = connect.getFromDataBase(query);
                    while (x2.next()) {
                        logChange("groups_clients", x2.getInt(1), ActionType.DELETE);
                    }
                }
            } catch (ConnectionException | SQLException e) {
                throw new ModelSyncException("Could not fetch groups by company ID!", e);
            }
        } else {
            throw new DatabaseOutOfSyncException();
        }
    }


    @Override
    protected boolean verifyIntegrity(int itemID) throws ModelSyncException {
        return verifyIntegrity(itemID, timestamp, "groups");
    }
}
