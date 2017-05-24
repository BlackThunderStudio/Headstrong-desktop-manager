package com.headstrongpro.desktop.core.controller;


import com.headstrongpro.desktop.core.exception.ConnectionException;
import com.headstrongpro.desktop.core.exception.DatabaseOutOfSyncException;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.model.Session;
import com.headstrongpro.desktop.model.resource.*;
import com.headstrongpro.desktop.DbLayer.DBResources;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Time;
import java.util.*;
import java.util.stream.Collectors;

import static com.headstrongpro.desktop.model.resource.ResourceUploadAdapter.*;

/**
 * Created by rajmu on 17.05.19.
 */
public class ResourcesController implements Refreshable {
    private List<Resource> resources;
    private DBResources resourcesDAO;

    private Media media;
    private MediaPlayer mediaPlayer;

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
    @Concurrent
    @Override
    public void refresh() throws ModelSyncException {
        resources.addAll(resourcesDAO.getAll());
    }
    
    public List<Resource> getAll(){
        return resources;
    }

    /***
     *
     * Searches through every field of objects in the list
     *
     * @param input searched expression
     * @return A list of objects containing searched phrase
     */
    public List<Resource> searchByPhrase(String input){
        if (input == null) throw new IllegalStateException("input query cannot be of null");
        if (input.isEmpty()) return new ArrayList<Resource>();
        return resources.stream()
                .filter(e -> String.valueOf(e.getID()).contains(input) ||
                e.getName().toLowerCase().contains(input.toLowerCase()) ||
                e.getDescription().toLowerCase().contains(input.toLowerCase()))
                .collect(Collectors.toList());
    }

    /***
     *
     * Displays items of a selected type
     *
     * @param type ResourceType object
     * @return List of resources
     */
    public List<Resource> filterByType(ResourceType type){
        if (type == null) throw new IllegalStateException("Type cannot be null");
        return resources.parallelStream()
                .filter(e -> e.getType().equals(type))
                .collect(Collectors.toList());
    }

    /***
     *
     * Prepares FileChooser object. Applies extension filters suitable for media type handled by headstrong app.
     *
     * @param title FileChooser window title
     * @return FileChooser object
     */
    private FileChooser prepareFileChooser(String title){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.jpg", "*.jpeg", "*.png", "*.gif"),
                new FileChooser.ExtensionFilter("Videos", "*.mp4", "*.avi", "*.webm"),
                new FileChooser.ExtensionFilter("Text", "*.txt"),
                new FileChooser.ExtensionFilter("Audio", "*.mp3", "*.wav"),
                new FileChooser.ExtensionFilter("All files", "*.*")
        );
        return fileChooser;
    }

    /***
     *
     * Selects a file from a local storage
     *
     * @return File object
     */
    public File selectLocalFile(){
        FileChooser fileChooser = prepareFileChooser("Select local resource");
        File file = fileChooser.showOpenDialog(new Stage());
        if(file == null) throw new IllegalStateException("No file selected");
        return file;
    }

    /***
     *
     * Selects one or multiple files from a local storage
     *
     * @return List of files
     */
    public List<File> selectMultipleFiles(){
        FileChooser fileChooser = prepareFileChooser("Select resources");
        List<File> files = fileChooser.showOpenMultipleDialog(new Stage());
        if(files.size() == 0) throw new IllegalStateException("No files selected");
        return files;
    }

    /***
     *
     * Uploads chosen file to the remote file server and adds a record to the database.
     *
     * @param file File object from the local machine
     * @param remoteName Resource remoteName / filename
     * @param description Description of the resource
     * @param isForAchievement dunno
     * @param type ResourceType element
     * @param args list of arguments passed as Object. The order of objects in the list is equivalent to the order of
     *             columns in each of the tables.
     * @return Resource type object with id fetched from the database already.
     * @throws ModelSyncException throws it when shit goes south.
     */
    @Concurrent(info = "Handles both database and media server")
    public Resource uploadLocalFile(File file, String remoteName, String description, boolean isForAchievement,
                                    ResourceType type, List<Object> args)
            throws ModelSyncException, ConnectionException {

        //retard protection
        if (file == null || remoteName == null || description == null || type == null || args == null)
            throw new NullPointerException();
        if (file.getAbsolutePath().isEmpty() || file.getName().isEmpty())
            throw new IllegalStateException("File cannot be empty!");
        if (remoteName.isEmpty()) throw new IllegalStateException("Name cannot be empty");
        if (description.isEmpty()) throw new IllegalStateException("Description cannot be empty");

        ResourceUploader resourceUploader = new ResourceUploader();

        Resource resource = ResourceFactory.getResource(file.getName(), description, isForAchievement, type.get());
        resource.setFile(file);
        resource.setRemoteFileName(remoteName);

        String url= "";
        if(!type.equals(ResourceType.TEXT)){
            url = (String) resourceUploader.upload(resource, Destination.MEDIA_SERVER);
        }

        //load record into the database
        if(type.equals(ResourceType.TEXT)){
            TextResource textResource = Resource.ofType(resource);
            assert textResource != null;
            textResource.setContent((String)args.get(0));
            resource = textResource;
        } else if(type.equals(ResourceType.IMAGE)){
            ImageResource imageResource = Resource.ofType(resource);
            assert imageResource != null;
            imageResource.setURL(url);
            resource = imageResource;
        } else {
            AudioResource audioResource = Resource.ofType(resource);
            assert audioResource != null;
            audioResource.setUrl(url);
            audioResource.setDuration((Time)args.get(0));
            resource = audioResource;
        }
        return (Resource) resourceUploader.upload(resource, Destination.DATABASE);
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
    public void assignToCourse(Session session, Resource resource)
            throws DatabaseOutOfSyncException, ModelSyncException {
        if (session == null) throw new IllegalStateException("Session cannot be null");
        if (resource == null) throw new IllegalStateException("Resource cannot be null");
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
    public void assignToCourse(Session session, List<Resource> resources)
            throws DatabaseOutOfSyncException, ModelSyncException {
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
    @Concurrent
    public void editResource(Resource selectedResource)
            throws DatabaseOutOfSyncException, ModelSyncException {
        resourcesDAO.update(selectedResource);
        refresh();
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

    public void playSelected(Resource selected) throws MalformedURLException, URISyntaxException {
        AudioResource resource = Resource.ofType(selected);
        media = new Media(new URL(resource.getUrl()).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setOnReady(() -> mediaPlayer.play());
    }
}
