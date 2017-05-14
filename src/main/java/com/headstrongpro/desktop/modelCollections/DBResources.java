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
import java.util.stream.Collectors;

import static com.headstrongpro.desktop.core.connection.ActionType.*;
import static com.headstrongpro.desktop.model.resource.ResourceType.*;

/**
 * Created by rajmu on 17.05.08.
 */
public class DBResources extends Synchronizable implements IDataAccessObject<Resource> {

    private DBConnect dbConnect;

    private List<Resource> prepareResources(List<Resource> resources) throws ConnectionException, SQLException {
        //TODO: created a bit more optimised version of the query
        // resource segment: TEXT
        String query = "SELECT content,parent_id FROM text_resources WHERE parent_id IN (";
        String parentIDs = "";
        List<TextResource> textResources = resources.stream().filter(e -> e.getType().equals(TEXT)).map(TextResource.class::cast).collect(Collectors.toList());
        if(textResources.size() != 0){
            for (int i = 0; i < textResources.size() - 1; i++){
                parentIDs += textResources.get(i).getID() + ",";
            }
            parentIDs += textResources.get(textResources.size() - 1).getID();
            ResultSet resultSet = dbConnect.getFromDataBase(query + parentIDs + ");");
            while (resultSet.next()){
                textResources.stream().filter(e -> {
                    try {
                        return e.getID() == resultSet.getInt(2);
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                    return false;
                }).findFirst().get().setContent(resultSet.getString(1));
            }
        }
        //EOS

        //resource segment: Image
        List<ImageResource> imageResources = resources.stream().filter(e -> e.getType().equals(IMAGE)).map(ImageResource.class::cast).collect(Collectors.toList());
        query = "SELECT url, parent_id FROM image_resources WHERE parent_id IN (";
        parentIDs = "";
        if(imageResources.size() != 0){
            for (int i = 0; i < imageResources.size() - 1; i++){
                parentIDs += imageResources.get(i).getID() + ",";
            }
            parentIDs += imageResources.get(imageResources.size() - 1).getID();
            ResultSet resultSet = dbConnect.getFromDataBase(query + parentIDs + ");");
            while (resultSet.next()){
                imageResources.stream().filter(e -> {
                    try {
                        return e.getID() == resultSet.getInt(2);
                    } catch (SQLException e2){
                        e2.printStackTrace();
                    }
                    return false;
                }).findFirst().get().setURL(resultSet.getString(1));
            }
        }
        //EOS

        //resource segment: audio
        List<AudioResource> audioResources = resources.stream().filter(e -> e.getType().equals(AUDIO)).map(AudioResource.class::cast).collect(Collectors.toList());
        parentIDs = "";
        query = "SELECT url, duration, parent_id FROM multimedia_resources WHERE parent_id IN (";
        if(audioResources.size() != 0){
            for (int i = 0; i < audioResources.size() - 1; i++){
                parentIDs += audioResources.get(i).getID() + ",";
            }
            parentIDs += audioResources.get(audioResources.size() - 1).getID();
            ResultSet resultSet = dbConnect.getFromDataBase(query + parentIDs + ");");
            while (resultSet.next()){
                AudioResource ar =  audioResources.stream().filter(e -> {
                    try {
                        return e.getID() == resultSet.getInt(3);
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                    return false;
                }).findFirst().get();
                ar.setUrl(resultSet.getString(1));
                ar.setDuration(resultSet.getTime(2));
            }
        }
        //EOS

        //resource segment: video
        List<VideoResource> videoResources = resources.stream().filter(e -> e.getType().equals(VIDEO)).map(VideoResource.class::cast).collect(Collectors.toList());
        parentIDs = "";
        if(videoResources.size() != 0){
            for (int i = 0; i < videoResources.size() - 1; i++){
                parentIDs += videoResources.get(i).getID() + ",";
            }
            parentIDs += videoResources.get(videoResources.size() - 1).getID();
            ResultSet resultSet = dbConnect.getFromDataBase(query + parentIDs + ");");
            while (resultSet.next()){
                VideoResource vr =  videoResources.stream().filter(e -> {
                    try {
                        return e.getID() == resultSet.getInt(3);
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                    return false;
                }).findFirst().get();
                vr.setUrl(resultSet.getString(1));
                vr.setDuration(resultSet.getTime(2));
            }
        }
        //EOS

        resources = textResources.stream().map(Resource.class::cast).collect(Collectors.toList());
        resources.addAll(imageResources.stream().map(Resource.class::cast).collect(Collectors.toList()));
        resources.addAll(audioResources.stream().map(Resource.class::cast).collect(Collectors.toList()));
        resources.addAll(videoResources.stream().map(Resource.class::cast).collect(Collectors.toList()));

        return resources;
    }

    @Override
    public List<Resource> getAll() throws ModelSyncException {
        List<Resource> resources = new ArrayList<>();
        try{
            dbConnect = new DBConnect();
            String query = "SELECT * FROM [resources]";
            ResultSet rs = dbConnect.getFromDataBase(query);
            while(rs.next()){
                Resource resource = ResourceFactory.getResource(rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getBoolean("is_for_achievement"),
                        rs.getInt("type"));
                resources.add(resource);
            }
           resources = prepareResources(resources);
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
            List<Resource> r = new ArrayList<>();
            resource = ResourceFactory.getResource(rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getBoolean("is_for_achievement"),
                    rs.getInt("type"));
            r.add(resource);
            resource = prepareResources(r).get(0);
        } catch (ConnectionException | SQLException e) {
            throw new ModelSyncException("Could not load resources.", e);
        }
        return resource;
    }

    @Override
    public Resource create(Resource object) throws ModelSyncException {
        try{
            dbConnect = new DBConnect();
            //language=TSQL
            String query = "INSERT INTO resources(name, description, is_for_achievement, type) VALUES (?, ?, ?, ?);";
            PreparedStatement preparedStatement = dbConnect.getConnection().prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, object.getName());
            preparedStatement.setString(2, object.getDescription());
            preparedStatement.setBoolean(3, object.isForAchievement());
            preparedStatement.setInt(4, object.getType().get());
            preparedStatement.executeUpdate();
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    object.setID(generatedKeys.getInt(1));

                    //creating records in children tables!!!
                    switch (object.getType().get()){
                        case 1:
                            TextResource textResource = Resource.ofType(object);
                            query = "INSERT INTO text_resources(content, parent_id) VALUES (?,?)";
                            PreparedStatement preparedStatement1 = dbConnect.getConnection().prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
                            preparedStatement1.setString(1, textResource.getContent());
                            preparedStatement1.setInt(2, textResource.getID());
                            preparedStatement1.executeUpdate();
                            try(ResultSet generatedKeys1 = preparedStatement1.getGeneratedKeys()) {
                                if (generatedKeys1.next()){
                                    logChange(-1, "text_resources", generatedKeys1.getInt(1), CREATE);
                                } else throw new ModelSyncException("Creating record in child table failed! No child ID received!");
                            }
                            break;
                        case 2:
                            ImageResource imageResource = Resource.ofType(object);
                            query = "INSERT INTO image_resources(url, parent_id) VALUES (?,?)";
                            PreparedStatement preparedStatement2 = dbConnect.getConnection().prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
                            preparedStatement2.setString(1, imageResource.getURL());
                            preparedStatement2.setInt(2, imageResource.getID());
                            preparedStatement2.executeUpdate();
                            try(ResultSet generatedKeys2 = preparedStatement2.getGeneratedKeys()){
                                if(generatedKeys2.next()){
                                    logChange(-1, "image_resources", generatedKeys2.getInt(1), CREATE);
                                } else throw new ModelSyncException("Creating record in child table failed! No child ID received!");
                            }
                            break;
                        case 3:
                            AudioResource audioResource = Resource.ofType(object);
                            query = "INSERT INTO multimedia_resources(url, duration, parent_id) VALUES (?,?,?)";
                            PreparedStatement preparedStatement3 = dbConnect.getConnection().prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
                            preparedStatement3.setString(1, audioResource.getUrl());
                            preparedStatement3.setTime(2, audioResource.getDuration());
                            preparedStatement3.setInt(3, audioResource.getID());
                            preparedStatement3.executeUpdate();
                            try(ResultSet generatedKeys3 = preparedStatement3.getGeneratedKeys()){
                                if(generatedKeys3.next()){
                                    logChange(-1, "multimedia_resources", generatedKeys3.getInt(1), CREATE);
                                } else throw new ModelSyncException("Creating record in child table failed! No child ID received!");
                            }
                            break;
                        case 4:
                            VideoResource videoResource = Resource.ofType(object);
                            query = "INSERT INTO multimedia_resources(url, duration, parent_id) VALUES (?,?,?)";
                            PreparedStatement preparedStatement4 = dbConnect.getConnection().prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
                            preparedStatement4.setString(1, videoResource.getUrl());
                            preparedStatement4.setTime(2, videoResource.getDuration());
                            preparedStatement4.setInt(3, videoResource.getID());
                            preparedStatement4.executeUpdate();
                            try(ResultSet generatedKeys4 = preparedStatement4.getGeneratedKeys()){
                                if(generatedKeys4.next()){
                                    logChange(-1, "multimedia_resources", generatedKeys4.getInt(1), CREATE);
                                } else throw new ModelSyncException("Creating record in child table failed! No child ID received!");
                            }
                            break;
                    } //end of switch
                } else {
                    throw new ModelSyncException("Creating resource failed. No ID retrieved!");
                }
            }
            logChange(-1, "resources", object.getID(), CREATE);
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
            String query = "UPDATE resources SET name=?,description=?,is_for_achievement=?,type=? WHERE id=?;";
            PreparedStatement preparedStatement = dbConnect.getConnection().prepareStatement(query);
            preparedStatement.setString(1, object.getName());
            preparedStatement.setString(2, object.getDescription());
            preparedStatement.setBoolean(3, object.isForAchievement());
            preparedStatement.setInt(4, object.getType().get());
            preparedStatement.setInt(5, object.getID());
            dbConnect.uploadSafe(preparedStatement);

            //update the rest of it
            switch (object.getType().get()){
                case 1:
                    TextResource textResource = Resource.ofType(object);
                    query = "UPDATE text_resources SET content=? WHERE parent_id=?";
                    PreparedStatement preparedStatement1 = dbConnect.getConnection().prepareStatement(query);
                    preparedStatement1.setString(1, textResource.getContent());
                    preparedStatement1.setInt(2, textResource.getID());
                    ResultSet x = dbConnect.getFromDataBase("SELECT id FROM text_resources WHERE parent_id=" + textResource.getID());
                    x.next();
                    dbConnect.uploadSafe(preparedStatement1);
                    logChange(-1, "text_resources", x.getInt(1), UPDATE);
                    break;
                case 2:
                    ImageResource imageResource = Resource.ofType(object);
                    query = "UPDATE image_resources SET url=? WHERE parent_id=?";
                    PreparedStatement preparedStatement2 = dbConnect.getConnection().prepareStatement(query);
                    preparedStatement2.setString(1, imageResource.getURL());
                    preparedStatement2.setInt(2, imageResource.getID());
                    dbConnect.uploadSafe(preparedStatement2);
                    ResultSet x2 = dbConnect.getFromDataBase("SELECT id FROM image_resources WHERE parent_id=" + imageResource.getID());
                    x2.next();
                    logChange(-1, "image_resources", x2.getInt(1), UPDATE);
                    break;
                case 3:
                    AudioResource audioResource = Resource.ofType(object);
                    query = "UPDATE multimedia_resources SET url=?,duration=? WHERE parent_id=?";
                    PreparedStatement preparedStatement3 = dbConnect.getConnection().prepareStatement(query);
                    preparedStatement3.setString(1, audioResource.getUrl());
                    preparedStatement3.setTime(2, audioResource.getDuration());
                    preparedStatement3.setInt(3, audioResource.getID());
                    dbConnect.uploadSafe(preparedStatement3);
                    ResultSet x3 = dbConnect.getFromDataBase("SELECT id FROM multimedia_resources WHERE parent_id=" + audioResource.getID());
                    x3.next();
                    logChange(-1, "multimedia_resources", x3.getInt(1), UPDATE);
                    break;
                case 4:
                    VideoResource videoResource = Resource.ofType(object);
                    query = "UPDATE multimedia_resources SET url=?,duration=? WHERE parent_id=?";
                    PreparedStatement preparedStatement4 = dbConnect.getConnection().prepareStatement(query);
                    preparedStatement4.setString(1, videoResource.getUrl());
                    preparedStatement4.setTime(2, videoResource.getDuration());
                    preparedStatement4.setInt(3, videoResource.getID());
                    dbConnect.uploadSafe(preparedStatement4);
                    ResultSet x4 = dbConnect.getFromDataBase("SELECT id FROM multimedia_resources WHERE parent_id=" + videoResource.getID());
                    x4.next();
                    logChange(-1, "multimedia_resources", x4.getInt(1), UPDATE);
                    break;
            } //end of switch
            logChange(-1, "resources", object.getID(), UPDATE);
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
            dbConnect.uploadSafe(preparedStatement);
            logChange(-1, "resources", object.getID(), DELETE);

            switch (object.getType().get()){
                case 1:
                    ResultSet x = dbConnect.getFromDataBase("SELECT id FROM text_resources WHERE parent_id=" + object.getID());
                    query = "DELETE FROM text_resources WHERE parent_id=" + object.getID();
                    dbConnect.upload(query);
                    x.next();
                    logChange(-1, "text_resources", x.getInt(1), DELETE);
                    break;
                case 2:
                    ResultSet x2 = dbConnect.getFromDataBase("SELECT id FROM image_resources WHERE parent_id=" + object.getID());
                    query = "DELETE FROM image_resources WHERE parent_id=" + object.getID();
                    dbConnect.upload(query);
                    x2.next();
                    logChange(-1, "image_resources", x2.getInt(1), DELETE);
                    break;
                case 3 | 4:
                    ResultSet x3 = dbConnect.getFromDataBase("SELECT id FROM multimedia_resources WHERE parent_id=" + object.getID());
                    query = "DELETE FROM multimedia_resources WHERE parent_id=" + object.getID();
                    dbConnect.upload(query);
                    x3.next();
                    logChange(-1, "multimedia_resources", x3.getInt(1), DELETE);
                    break;
            }
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
                Resource resource = ResourceFactory.getResource(rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getBoolean("is_for_achievement"),
                        rs.getInt("type"));
                resources.add(resource);
            }
            resources = prepareResources(resources);
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
            for (int i = 0; i < resIDs.size() - 1; i ++){
                simplifiedResIDs += resIDs.get(i) + ",";
            }
            simplifiedResIDs += resIDs.get(resIDs.size() - 1);
            qry = "SELECT * FROM resources WHERE id IN(" + simplifiedResIDs + ");";
            rs = dbConnect.getFromDataBase(qry);
            while (rs.next()){
                Resource resource = ResourceFactory.getResource(rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getBoolean("is_for_achievement"),
                        rs.getInt("type"));
                resources.add(resource);
            }
            resources = prepareResources(resources);
        } catch (ConnectionException | SQLException e){
            throw new ModelSyncException("WARNING! Could not fetch resources of sessionID: " + sessionID + " !", e);
        }
        return resources;
    }

    //TODO: this shit needs to be REWORKED. I have no idea how is it supposed to check for date. Perhaps we anyway need a timestamp in each table, cause I don't see any other solution to it
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
