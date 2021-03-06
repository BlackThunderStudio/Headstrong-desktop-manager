package com.headstrongpro.desktop.DbLayer;

import com.headstrongpro.desktop.DbLayer.util.ActionType;
import com.headstrongpro.desktop.DbLayer.util.IDataAccessObject;
import com.headstrongpro.desktop.DbLayer.util.Synchronizable;
import com.headstrongpro.desktop.core.connection.DBConnect;
import com.headstrongpro.desktop.core.exception.ConnectionException;
import com.headstrongpro.desktop.core.exception.DatabaseOutOfSyncException;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.model.Department;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DB Departments
 */
public class DBDepartments extends Synchronizable implements IDataAccessObject<Department> {

    private DBConnect connect;

    public DBDepartments() {
        updateTimestampLocal();
    }

    @Override
    public List<Department> getAll() throws ModelSyncException {
        List<Department> departments = new ArrayList<>();
        try {
            connect = new DBConnect();
            //language=TSQL
            String query = "SELECT * FROM departments";
            ResultSet rs = connect.getFromDataBase(query);
            while (rs.next()) {
                departments.add(new Department(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getInt("company_id")
                ));
            }
            timestamp = setTimestamp();
        } catch (ConnectionException | SQLException e) {
            throw new ModelSyncException("Could not retrieve departments!", e);
        }
        return departments;
    }

    @Override
    public Department getById(int id) throws ModelSyncException {
        Department department;
        try {
            connect = new DBConnect();
            ResultSet rs = connect.getFromDataBase("SELECT * FROM departments WHERE id=" + id);
            rs.next();
            department = new Department(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getInt("company_id")
            );
            timestamp = setTimestamp();
        } catch (ConnectionException | SQLException e) {
            throw new ModelSyncException("Could not retrieve a department!", e);
        }
        return department;
    }

    @Override
    public Department persist(Department object) throws ModelSyncException {
        try {
            connect = new DBConnect();
            //language=TSQL
            String query = "INSERT INTO departments(name, description, company_id) VALUES (?,?,?);";
            PreparedStatement preparedStatement = connect.getConnection().prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, object.getName());
            preparedStatement.setString(2, object.getDescription());
            preparedStatement.setInt(3, object.getCompanyId());
            preparedStatement.executeUpdate();
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    object.setId(generatedKeys.getInt(1));
                    logChange("departments", object.getId(), ActionType.CREATE);
                    timestamp = setTimestamp();
                } else {
                    throw new ModelSyncException("Creating new department failed! No ID retrieved!");
                }
            }
        } catch (ConnectionException | SQLException e) {
            throw new ModelSyncException("Could not persist a department!", e);
        }
        return object;
    }

    @Override
    public void update(Department object) throws ModelSyncException, DatabaseOutOfSyncException {
        if (verifyIntegrity(object.getId())) {
            try {
                connect = new DBConnect();
                //language=TSQL
                String query = "UPDATE departments SET name=?,description=?,company_id=? WHERE id=?;";
                PreparedStatement preparedStatement = connect.getConnection().prepareStatement(query);
                preparedStatement.setString(1, object.getName());
                preparedStatement.setString(2, object.getDescription());
                preparedStatement.setInt(3, object.getCompanyId());
                preparedStatement.setInt(4, object.getId());
                connect.uploadSafe(preparedStatement);
                logChange("departments", object.getId(), ActionType.UPDATE);
            } catch (ConnectionException | SQLException e) {
                throw new ModelSyncException("Could not update a department!", e);
            }
        } else {
            throw new DatabaseOutOfSyncException();
        }
    }

    @Override
    public void delete(Department object) throws ModelSyncException, DatabaseOutOfSyncException {
        if (verifyIntegrity(object.getId())) {
            try {
                connect = new DBConnect();
                connect.upload("DELETE FROM departments WHERE id=" + object.getId());
                logChange("departments", object.getId(), ActionType.DELETE);

                ResultSet x = connect.getFromDataBase("SELECT id FROM departments_clients WHERE department_id=" + object.getId() + "; " + "DELETE FROM departments_clients WHERE department_id=" + object.getId());
                while (x.next()) {
                    logChange("departments_clients", x.getInt(1), ActionType.DELETE);
                }
            } catch (ConnectionException | SQLException e) {
                throw new ModelSyncException("Could not delete a department!", e);
            }
        } else {
            throw new DatabaseOutOfSyncException();
        }
    }

    public List<Department> getByCompanyID(int id) throws ModelSyncException {
        List<Department> departments = new ArrayList<>();
        try {
            connect = new DBConnect();
            //language=TSQL
            String query = "SELECT * FROM departments WHERE company_id=" + id + ";";
            ResultSet rs = connect.getFromDataBase(query);
            while (rs.next()) {
                departments.add(new Department(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getInt("company_id")
                ));
            }
        } catch (ConnectionException | SQLException e) {
            throw new ModelSyncException("Could not fetch departments by company ID!", e);
        }
        return departments;
    }

    void deleteByCompanyID(int id) throws ModelSyncException, DatabaseOutOfSyncException {
        if (verifyIntegrity(id)) {
            try {
                connect = new DBConnect();
                ResultSet rs = connect.getFromDataBase("SELECT id FROM departments WHERE company_id=" + id);
                List<Integer> deptIDs = new ArrayList<>();
                while (rs.next()) {
                    deptIDs.add(rs.getInt(1));
                }
                StringBuilder simplifiedDeptIDs = new StringBuilder();
                for (int i = 0; i < deptIDs.size() - 1; i++) {
                    simplifiedDeptIDs.append(deptIDs.get(i)).append(",");
                }
                if (deptIDs.size() != 0) simplifiedDeptIDs.append(deptIDs.get(deptIDs.size() - 1));

                //language=TSQL
                String query = "SELECT id FROM departments WHERE company_id=" + id + "; DELETE FROM departments WHERE company_id=" + id + ";";
                ResultSet x = connect.getFromDataBase(query);
                while (x.next()) {
                    logChange("departments", x.getInt(1), ActionType.DELETE);
                }

                if (deptIDs.size() != 0) {
                    query = "SELECT id FROM departments_clients WHERE department_id IN (" + simplifiedDeptIDs + "); DELETE FROM departments_clients WHERE department_id IN(" + simplifiedDeptIDs + ");";
                    ResultSet x2 = connect.getFromDataBase(query);
                    while (x2.next()) {
                        logChange("departments_clients", x2.getInt(1), ActionType.DELETE);
                    }
                }
            } catch (ConnectionException | SQLException e) {
                throw new ModelSyncException("Could not fetch departments by company ID!", e);
            }
        } else {
            throw new DatabaseOutOfSyncException();
        }
    }

    @Override
    protected boolean verifyIntegrity(int itemID) throws ModelSyncException {
        return verifyIntegrity(itemID, timestamp, "departments");
    }
}
