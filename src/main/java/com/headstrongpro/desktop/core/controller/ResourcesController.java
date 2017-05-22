package com.headstrongpro.desktop.core.controller;


import com.headstrongpro.desktop.core.exception.DatabaseOutOfSyncException;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.model.Session;
import com.headstrongpro.desktop.model.resource.*;
import com.headstrongpro.desktop.modelCollections.DBResources;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.sql.Time;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by rajmu on 17.05.19.
 */
public class ResourcesController implements Refreshable {
    private List<Resource> resources;
    private DBResources resourcesDAO;

    public ResourcesController() throws ModelSyncException {
        resources = new ArrayList<>();
        resourcesDAO = new DBResources();

        refresh();
    }

    /***
     *
     * Kind of self explanatory
     *
     * @throws ModelSyncException
     */
    @Override
    public void refresh() throws ModelSyncException {
        resources.addAll(resourcesDAO.getAll());
    }

    /***
     *
     * Searches through every field of objects in the list
     *
     * @param input searched expression
     * @return A list of objects containing searched phrase
     */
    public List<Resource> searchByPhrase(String input){
        return resources.stream().filter(e -> {
            if(String.valueOf(e.getID()).equalsIgnoreCase(input) ||
                    e.getName().equalsIgnoreCase(input) ||
                    e.getDescription().equalsIgnoreCase(input)){
                return true;
            } else return false;
        }).collect(Collectors.toList());
    }

    /***
     *
     * Displays items of a selected type
     *
     * @param type ResourceType object
     * @return List of resources
     */
    public List<Resource> filterByType(ResourceType type){
        return resources.stream().filter(e -> e.getType().equals(type)).collect(Collectors.toList());
    }

    /***
     *
     * Selects a file from a local storage
     *
     * @return File object
     */
    public File selectLocalFile(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select local resource");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.jpg", "*.jpeg", "*.png", "*.gif"),
                new FileChooser.ExtensionFilter("Videos", "*.mp4", "*.avi", "*.webm"),
                new FileChooser.ExtensionFilter("Text", "*.txt"),
                new FileChooser.ExtensionFilter("Audio", "*.mp3", "*.wav"),
                new FileChooser.ExtensionFilter("All files", "*.*")
        );
        return fileChooser.showOpenDialog(new Stage());
    }

    /***
     *
     * Uploads chosen file to the remote file server and adds a record to the database.
     *
     * @param file File object from the local machine
     * @param name Resource name / filename
     * @param description Description of the resource
     * @param isForAchievement dunno
     * @param type ResourceType element
     * @param args list of arguments passed as Object. The order of objects in the list is equivalent to the order of columns in each of the tables.
     * @return Resource type object with id fetched from the database already.
     * @throws ModelSyncException throws it when shit goes south.
     */
    public Resource uploadLocalFile(File file, String name, String description, boolean isForAchievement, ResourceType type, List<Object> args) throws ModelSyncException {
        //TODO: figure out server upload logic
        //TODO: Do some magic over here
        String url= "";
        if(!type.equals(ResourceType.TEXT)){
            //upload to the server
            url = file.getAbsolutePath(); //DELETE THIS LINE WHEN FTP CONNECTIVITY IS IMPLEMENTED
        }

        //load record into the database
        Resource resource = ResourceFactory.getResource(file.getName(), description, isForAchievement, type.get());
        if(type.equals(ResourceType.TEXT)){
            TextResource textResource = Resource.ofType(resource);
            textResource.setContent((String)args.get(0));
            resource = textResource;
        } else if(type.equals(ResourceType.IMAGE)){
            ImageResource imageResource = Resource.ofType(resource);
            imageResource.setURL(url);
            resource = imageResource;
        } else {
            AudioResource audioResource = Resource.ofType(resource);
            audioResource.setUrl(url);
            audioResource.setDuration((Time)args.get(0));
            resource = audioResource;
        }
        return resourcesDAO.persist(resource);
    }

    /***
     *
     * Assigns resource to a session
     *
     * @param session Session object
     * @param resource Resource object
     * @throws DatabaseOutOfSyncException
     * @throws ModelSyncException
     */
    public void assignToCourse(Session session, Resource resource) throws DatabaseOutOfSyncException, ModelSyncException {
        resourcesDAO.assignToSession(resource, session);
    }

    /***
     *
     * Assigns a set of resources to a session
     *
     * @param session Session object
     * @param resources List of Resources
     * @throws DatabaseOutOfSyncException
     * @throws ModelSyncException
     */
    public void assignToCourse(Session session, List<Resource> resources) throws DatabaseOutOfSyncException, ModelSyncException {
        for (Resource r : resources){
            resourcesDAO.assignToSession(r, session);
        }
    }

    /***
     *
     * Updates record of an existing resource in the database
     *
     * @param selectedResource
     * @throws DatabaseOutOfSyncException
     * @throws ModelSyncException
     */
    public void editResource(Resource selectedResource) throws DatabaseOutOfSyncException, ModelSyncException {
        resourcesDAO.update(selectedResource);
    }

    /***
     *
     * Returns image object so it can be previewed
     *
     * @param imageResource ImageResource object. Child of a Resource interface
     * @return returns javafx.scene.image.Image Object
     */
    public Image getImageFromResource(ImageResource imageResource){
        return new Image(imageResource.getURL());
    }

    //TODO: stopped at media playback. WIP. To be continued

}
