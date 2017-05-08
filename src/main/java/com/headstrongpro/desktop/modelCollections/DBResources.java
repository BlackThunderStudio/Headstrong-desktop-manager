package com.headstrongpro.desktop.modelCollections;

import com.headstrongpro.desktop.core.connection.DBConnect;
import com.headstrongpro.desktop.core.connection.IDataAccessObject;
import com.headstrongpro.desktop.core.exception.ConnectionException;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.model.resource.Resource;
import com.headstrongpro.desktop.model.resource.ResourceFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rajmu on 17.05.08.
 */
class DBResources implements IDataAccessObject<Resource> {

    private DBConnect dbConnect;

    @Override
    public List<Resource> getAll() throws ModelSyncException {
        List<Resource> resources = new ArrayList<>();
        try{
            dbConnect = new DBConnect();
            String query = "SELECT * FROM [resources]";
            ResultSet rs = dbConnect.getFromDataBase(query);
            ResourceFactory resourceFactory = new ResourceFactory();
            while(rs.next()){
                resources.add(resourceFactory.getResource(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("url"),
                        rs.getBoolean("is_for_achievement"),
                        rs.getInt("type")
                ));
            }
        } catch (ConnectionException | SQLException e) {
            throw new ModelSyncException("Could not load resources.", e);
        }
        return resources;
    }

    @Override
    public Resource getById(int id) throws ModelSyncException {
        Resource resource = null;
        try{
            dbConnect = new DBConnect();
            String query = "SELECT * FROM [resources] WHERE id=" + id + ";";
            ResultSet rs = dbConnect.getFromDataBase(query);
            ResourceFactory resourceFactory = new ResourceFactory();
            rs.next();
            resource = resourceFactory.getResource(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getString("url"),
                    rs.getBoolean("is_for_achievement"),
                    rs.getInt("type"));
        } catch (ConnectionException | SQLException e) {
            throw new ModelSyncException("Could not load resources.", e);
        }
        return resource;
    }

    @Override
    public Resource create(Resource object) throws ModelSyncException {
        try{
            dbConnect = new DBConnect();
            String createCompanyQuery = "INSERT INTO resources(name, description, url, is_for_achievement, type) VALUES (?, ?, ?, ?, ?);";
            PreparedStatement preparedStatement = dbConnect.getConnection().prepareStatement(createCompanyQuery, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, object.getName());
            preparedStatement.setString(2, object.getDescription());
            preparedStatement.setString(3, object.getURL());
            preparedStatement.setBoolean(4, object.isForAchievement());
            preparedStatement.setInt(5, object.getType());
            preparedStatement.executeUpdate();
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    object.setID(generatedKeys.getInt(1));
                } else {
                    throw new ModelSyncException("Creating resource failed. No ID retrieved!");
                }
            }
        } catch (ConnectionException | SQLException e) {
            throw new ModelSyncException("Could not create new resource!", e);
        }
        return object;
    }

    @Override
    public void update(Resource object) throws ModelSyncException {
        try {
            dbConnect = new DBConnect();
            //language=TSQL
            String query = "UPDATE resources SET name=?,description=?,url=?,is_for_achievement=?,type=? WHERE id=?;";
            PreparedStatement preparedStatement = dbConnect.getConnection().prepareStatement(query);
            preparedStatement.setString(1, object.getName());
            preparedStatement.setString(2, object.getDescription());
            preparedStatement.setString(3, object.getURL());
            preparedStatement.setBoolean(4, object.isForAchievement());
            preparedStatement.setInt(5, object.getType());
            dbConnect.uploadSafe(preparedStatement);
        } catch (ConnectionException | SQLException e) {
            throw new ModelSyncException("WARNING! Could not update resource of ID: " + object.getID() + " !", e);
        }
    }

    @Override
    public void delete(Resource object) throws ModelSyncException {
        try{
            dbConnect = new DBConnect();
            //language=TSQL
            String query = "DELETE FROM resources WHERE id=?;";
            PreparedStatement preparedStatement = dbConnect.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, object.getID());
            preparedStatement.execute();
        } catch (ConnectionException | SQLException e) {
            throw new ModelSyncException("WARNING! Could not update resource of ID: " + object.getID() + " !", e);
        }
    }

    public List<Resource> getbyType(int type) throws ModelSyncException {
        List<Resource> resources = new ArrayList<>();
        try{
            dbConnect = new DBConnect();
            //language=TSQL
            String query = "SELECT * FROM [resources] WHERE type=" + type;
            ResultSet rs = dbConnect.getFromDataBase(query);
            ResourceFactory resourceFactory = new ResourceFactory();
            while(rs.next()){
                resources.add(resourceFactory.getResource(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("url"),
                        rs.getBoolean("is_for_achievement"),
                        rs.getInt("type")
                ));
            }
        } catch (ConnectionException | SQLException e) {
            throw new ModelSyncException("Could not load resources.", e);
        }
        return resources;
    }

    public List<Resource> getBySessionID(int sessionID) throws ModelSyncException {
        ArrayList<Integer> resIDs = new ArrayList<>();
        List<Resource> resources = new ArrayList<>();
        try{
            dbConnect = new DBConnect();
            ResourceFactory resourceFactory = new ResourceFactory();
            //language=TSQL
            String qry = "SELECT * FROM sessions_resources WHERE session_id=" + sessionID + ";";
            ResultSet rs = dbConnect.getFromDataBase(qry);
            while (rs.next()){
                resIDs.add(rs.getInt("resource_id"));
            }

            String simplifiedResIDs = "";
            for (int i = 0; i < resIDs.size() - 2; i ++){
                simplifiedResIDs += resIDs.get(i) + ",";
            }
            simplifiedResIDs += resIDs.get(resIDs.size() - 1);
            qry = "SELECT * FROM resources WHERE id IN(" + simplifiedResIDs + ");";
            rs = dbConnect.getFromDataBase(qry);
            while (rs.next()){
                resources.add(resourceFactory.getResource(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("url"),
                        rs.getBoolean("is_for_achievement"),
                        rs.getInt("type")
                ));
            }
        } catch (ConnectionException | SQLException e){
            throw new ModelSyncException("WARNING! Could not fetch resources of sessionID: " + sessionID + " !", e);
        }
        return resources;
    }
}
