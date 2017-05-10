package com.headstrongpro.desktop.modelCollections;

import com.headstrongpro.desktop.core.connection.DBConnect;
import com.headstrongpro.desktop.core.connection.IDataAccessObject;
import com.headstrongpro.desktop.core.connection.Synchronizable;
import com.headstrongpro.desktop.core.exception.ConnectionException;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.model.Log;
import com.headstrongpro.desktop.model.resource.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by rajmu on 17.05.08.
 */
public class DBResources extends Synchronizable implements IDataAccessObject<Resource> {

    private DBConnect dbConnect;

    private Resource prepareResource(ResultSet rs) throws SQLException, ConnectionException{
        Resource resource = ResourceFactory.getResource(rs.getInt("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getBoolean("is_for_achievement"),
                rs.getInt("type"));
        ResultSet resultSet = null;
        assert resource != null;
        switch (resource.getType()){
            case 1:
                resultSet = dbConnect.getFromDataBase("SELECT content FROM text_resources WHERE parent_id=" + resource.getID() + ";");
                resultSet.next();
                TextResource tr = Resource.as(resource, TextResource.class);
                tr.setContent(resultSet.getString(1));
                resource = tr;
                break;
            case 2:
                resultSet = dbConnect.getFromDataBase("SELECT url FROM image_resources WHERE parent_id=" + resource.getID() + ";");
                resultSet.next();
                PhotoResource pr = Resource.as(resource, PhotoResource.class);
                pr.setURL(resultSet.getString(1));
                resource = pr;
                break;
            case 3:
                resultSet = dbConnect.getFromDataBase("SELECT url,duration FROM multimedia_resources WHERE parent_id=" + resource.getID() + ";");
                resultSet.next();
                AudioResource ar = Resource.as(resource, AudioResource.class);
                ar.setUrl(resultSet.getString(1));
                ar.setDuration(resultSet.getTime(2));
                resource = ar;
                break;
            case 4:
                resultSet = dbConnect.getFromDataBase("SELECT url,duration FROM multimedia_resources WHERE parent_id=" + resource.getID() + ";");
                resultSet.next();
                VideoResource vr = Resource.as(resource, VideoResource.class);
                vr.setUrl(resultSet.getString(1));
                vr.setDuration(resultSet.getTime(2));
                resource = vr;
                break;
        }
        return resource;
    }

    @Override
    public List<Resource> getAll() throws ModelSyncException {
        List<Resource> resources = new ArrayList<>();
        try{
            dbConnect = new DBConnect();
            String query = "SELECT * FROM [resources]";
            ResultSet rs = dbConnect.getFromDataBase(query);
            while(rs.next()){
                Resource resource = prepareResource(rs);
                resources.add(resource);
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
            rs.next();
            resource = prepareResource(rs);
        } catch (ConnectionException | SQLException e) {
            throw new ModelSyncException("Could not load resources.", e);
        }
        return resource;
    }

    //TODO: need update
    @Override
    public Resource create(Resource object) throws ModelSyncException {
        try{
            dbConnect = new DBConnect();
            String createCompanyQuery = "INSERT INTO resources(name, description, is_for_achievement, type) VALUES (?, ?, ?, ?);";
            PreparedStatement preparedStatement = dbConnect.getConnection().prepareStatement(createCompanyQuery, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, object.getName());
            preparedStatement.setString(2, object.getDescription());
            preparedStatement.setBoolean(3, object.isForAchievement());
            preparedStatement.setInt(4, object.getType());
            preparedStatement.executeUpdate();
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    object.setID(generatedKeys.getInt(1));
                } else {
                    throw new ModelSyncException("Creating resource failed. No ID retrieved!");
                }
            }
            logChange(-1, "resources", object.getID(), "CREATE");
        } catch (ConnectionException | SQLException e) {
            throw new ModelSyncException("Could not create new resource!", e);
        }
        return object;
    }

    //TODO: need update
    @Override
    public void update(Resource object) throws ModelSyncException {
        try {
            dbConnect = new DBConnect();
            //language=TSQL
            String query = "UPDATE resources SET name=?,description=?,is_for_achievement=?,type=? WHERE id=?;";
            PreparedStatement preparedStatement = dbConnect.getConnection().prepareStatement(query);
            preparedStatement.setString(1, object.getName());
            preparedStatement.setString(2, object.getDescription());
            preparedStatement.setBoolean(3, object.isForAchievement());
            preparedStatement.setInt(4, object.getType());
            dbConnect.uploadSafe(preparedStatement);
        } catch (ConnectionException | SQLException e) {
            throw new ModelSyncException("WARNING! Could not update resource of ID: " + object.getID() + " !", e);
        }
    }

    //TODO: need update
    @Override
    public void delete(Resource object) throws ModelSyncException {
        try{
            dbConnect = new DBConnect();
            //language=TSQL
            String query = "DELETE FROM resources WHERE id=?;";
            PreparedStatement preparedStatement = dbConnect.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, object.getID());
            preparedStatement.execute();
            logChange(-1, "resources", object.getID(), "DELETE");
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
            while(rs.next()){
                resources.add(prepareResource(rs));
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
                resources.add(prepareResource(rs));
            }
        } catch (ConnectionException | SQLException e){
            throw new ModelSyncException("WARNING! Could not fetch resources of sessionID: " + sessionID + " !", e);
        }
        return resources;
    }

    @Override
    protected boolean verifyIntegrity(int itemID) throws ModelSyncException {
        List<Log> logs = new DBLogActions().getByTable("resources");
        if(logs.size() == 0) return true;
        else {
            if(logs.stream().anyMatch(e -> e.getItemID() == itemID)){
                long millis = logs.stream()
                        .filter(e -> e.getItemID() == itemID)
                        .sorted(Comparator.comparingLong(e -> e.getDate().getTime()))
                        .findFirst().get().getDate().getTime(); //TODO: needs to be tested
                return millis < Calendar.getInstance().getTime().getTime();
                //stuck here for now
            } else return true;
        }
    }
}
