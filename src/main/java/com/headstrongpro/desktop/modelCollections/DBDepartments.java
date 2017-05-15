package com.headstrongpro.desktop.modelCollections;

import com.headstrongpro.desktop.core.connection.DBConnect;
import com.headstrongpro.desktop.core.exception.ConnectionException;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.model.Department;
import com.headstrongpro.desktop.modelCollections.util.IDataAccessObject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rajmu on 17.05.08.
 */
public class DBDepartments implements IDataAccessObject<Department> {

    private DBConnect connect;

    @Override
    public List<Department> getAll() throws ModelSyncException {
        List<Department> departments = new ArrayList<>();
        try{
            connect = new DBConnect();
            //language=TSQL
            String query = "SELECT * FROM departments";
            ResultSet rs = connect.getFromDataBase(query);
            while (rs.next()){
                departments.add(new Department(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getInt("company_id")
                ));
            }
        } catch (ConnectionException | SQLException e){
            throw new ModelSyncException("Could not retrieve departments!", e);
        }
        return departments;
    }

    @Override
    public Department getById(int id) throws ModelSyncException {
        Department department = null;
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
        } catch (ConnectionException | SQLException e){
            throw new ModelSyncException("Could not retrieve a department!", e);
        }
        return department;
    }

    @Override
    public Department create(Department object) throws ModelSyncException {
        try{
            connect = new DBConnect();
            String query = "INSERT INTO departments(name, description, company_id) VALUES (?,?,?);";
            PreparedStatement preparedStatement = connect.getConnection().prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, object.getName());
            preparedStatement.setString(2, object.getDescription());
            preparedStatement.setInt(3, object.getCompanyID());
            preparedStatement.executeUpdate();
            try(ResultSet generatedKeys = preparedStatement.getGeneratedKeys()){
                if(generatedKeys.next()){
                    object.setId(generatedKeys.getInt(1));
                } else {
                    throw new ModelSyncException("Creating new department failed! No ID retrieved!");
                }
            }
        } catch (ConnectionException | SQLException e){
            throw new ModelSyncException("Could not create a department!", e);
        }
        return object;
    }

    @Override
    public void update(Department object) throws ModelSyncException {
        try {
            connect = new DBConnect();
            //language=TSQL
            String query = "UPDATE departments SET name=?,description=?,company_id=? WHERE id=?;";
            PreparedStatement preparedStatement = connect.getConnection().prepareStatement(query);
            preparedStatement.setString(1, object.getName());
            preparedStatement.setString(2, object.getDescription());
            preparedStatement.setInt(3, object.getCompanyID());
            preparedStatement.setInt(4, object.getId());
            connect.uploadSafe(preparedStatement);
        } catch (ConnectionException | SQLException e){
            throw new ModelSyncException("Could not update a department!", e);
        }
    }

    @Override
    public void delete(Department object) throws ModelSyncException {
        try {
            connect = new DBConnect();
            connect.upload("DELETE FROM departments WHERE id=" + object.getId());
            connect.upload("DELETE FROM departments_clients WHERE department_id=" + object.getId());
        } catch (ConnectionException e){
            throw new ModelSyncException("Could not delete a department!", e);
        }
    }

    public List<Department> getByCompanyID(int id) throws ModelSyncException{
        List<Department> departments = new ArrayList<>();
        try {
            connect = new DBConnect();
            //language=TSQL
            String query = "SELECT * FROM departments WHERE company_id=" + id + ";";
            ResultSet rs = connect.getFromDataBase(query);
            while (rs.next()){
                departments.add(new Department(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getInt("company_id")
                ));
            }
        } catch (ConnectionException | SQLException e){
            throw new ModelSyncException("Could not fetch departments by company ID!", e);
        }
        return departments;
    }

    public void deleteByCompanyID(int id) throws ModelSyncException{
        try {
            connect = new DBConnect();
            ResultSet rs = connect.getFromDataBase("SELECT id FROM departments WHERE company_id=" + id);
            List<Integer> deptIDs = new ArrayList<>();
            while (rs.next()){
                deptIDs.add(rs.getInt(1));
            }
            String simplifiedDeptIDs = "";
            for (int i = 0 ; i < deptIDs.size() - 2; i++){
                simplifiedDeptIDs += deptIDs.get(i) + ",";
            }
            simplifiedDeptIDs += deptIDs.get(deptIDs.size() - 1);

            //language=TSQL
            String query = "DELETE FROM departments WHERE company_id=" + id + ";";
            connect.upload(query);

            query = "DELETE FROM departments_clients WHERE department_id IN(" + simplifiedDeptIDs + ");";
            connect.upload(query);
        } catch (ConnectionException | SQLException e){
            throw new ModelSyncException("Could not fetch departments by company ID!", e);
        }
    }
}
