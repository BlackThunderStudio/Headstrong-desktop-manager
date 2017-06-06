package com.headstrongpro.desktop.controller;


import com.headstrongpro.desktop.DbLayer.DBResources;
import com.headstrongpro.desktop.core.exception.ConnectionException;
import com.headstrongpro.desktop.core.exception.DatabaseOutOfSyncException;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.model.Course;
import com.headstrongpro.desktop.model.resource.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.headstrongpro.desktop.model.resource.ResourceUploadAdapter.Destination;

/**
 * ResourcesController
 */
public class ResourcesController implements Refreshable, IContentController<Resource> {
    private List<Resource> resources;
    private DBResources resourcesDAO;

    public ResourcesController() {
        resources = new ArrayList<>();
        resourcesDAO = new DBResources();
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
        resources.clear();
        resources.addAll(resourcesDAO.getAll());
    }

    @Override
    public ObservableList<Resource> getAll() throws ModelSyncException {
        refresh();
        return FXCollections.observableArrayList(resources);
    }

    /***
     *
     * Searches through every field of objects in the list
     *
     * @param input searched expression
     * @return A list of objects containing searched phrase
     */
    @Override
    public ObservableList<Resource> searchByPhrase(String input) {
        if (input == null) throw new IllegalStateException("input query cannot be of null");
        return FXCollections.observableArrayList(resources.stream()
                .filter(e -> String.valueOf(e.getId()).contains(input) ||
                        e.getName().toLowerCase().contains(input.toLowerCase()) ||
                        e.getDescription().toLowerCase().contains(input.toLowerCase()))
                .collect(Collectors.toList()));
    }

    /**
     * Filters the search results by resource type
     *
     * @param input searched expression
     * @param type  resource type
     * @return A list of objects of specified type containing the input
     */
    public List<Resource> filterSearch(String input, String type) {
        switch (type) {
            case "Image":
                return searchByPhrase(input).stream().filter(e -> e.getType().equals(ResourceType.IMAGE)).collect(Collectors.toList());
            case "Audio":
                return searchByPhrase(input).stream().filter(e -> e.getType().equals(ResourceType.AUDIO)).collect(Collectors.toList());
            case "Text":
                return searchByPhrase(input).stream().filter(e -> e.getType().equals(ResourceType.TEXT)).collect(Collectors.toList());
            default:
                return searchByPhrase(input);
        }
    }

    /***
     *
     * Displays items of a selected type
     *
     * @param type ResourceType object
     * @return List of resources
     */
    public List<Resource> filterByType(ResourceType type) {
        if (type == null) throw new IllegalStateException("Type cannot be null");
        /*return resources.parallelStream()
                .filter(e -> e.getType().equals(type))
                .collect(Collectors.toList());*/
        try {
            return resourcesDAO.getByType(type.get());
        } catch (ModelSyncException e) {
            e.printStackTrace();
        }
        return null;
    }

    /***
     *
     * Prepares FileChooser object. Applies extension filters suitable for media type handled by headstrong app.
     *
     * @param title FileChooser window title
     * @return FileChooser object
     */
    private FileChooser prepareFileChooser(String title) {
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
     * Retrieves the ResourceType object from a file
     *
     * @param file Input file
     * @return ResourceType object
     */
    public ResourceType getResourceType(File file) {
        String ext = FilenameUtils.getExtension(file.getAbsolutePath());
        if ("jpg;jpeg;png;gif".contains(ext.toLowerCase())) return ResourceType.IMAGE;
        else if ("mp3;wav".contains(ext.toLowerCase())) return ResourceType.AUDIO;
        else if ("mp4;avi;webm".contains(ext.toLowerCase())) return ResourceType.VIDEO;
        else return ResourceType.TEXT;
    }

    /***
     *
     * Selects a file from a local storage
     *
     * @return File object
     */
    public File selectLocalFile() {
        FileChooser fileChooser = prepareFileChooser("Select local resource");
        return fileChooser.showOpenDialog(new Stage());
    }

    /***
     *
     * Selects one or multiple files from a local storage
     *
     * @return List of files
     */
    public List<File> selectMultipleFiles() {
        FileChooser fileChooser = prepareFileChooser("Select resources");
        List<File> files = fileChooser.showOpenMultipleDialog(new Stage());
        if (files.size() == 0) throw new IllegalStateException("No files selected");
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
     * @throws ModelSyncException throws it when connection error occurs
     */
    @Concurrent(info = "Handles both database and media server")
    public Resource uploadLocalFile(File file, String remoteName, String description, boolean isForAchievement,
                                    ResourceType type, List<Object> args)
            throws ModelSyncException, ConnectionException {

        //fool protection
        if (file == null || remoteName == null || description == null || type == null)
            throw new NullPointerException();
        if (file.getAbsolutePath().isEmpty() || file.getName().isEmpty())
            throw new IllegalArgumentException("File cannot be empty!");
        if (remoteName.isEmpty()) throw new IllegalStateException("Name cannot be empty");
        if (description.isEmpty()) throw new IllegalStateException("Description cannot be empty");

        ResourceUploader resourceUploader = new ResourceUploader();

        Resource resource = ResourceFactory.getResource(remoteName, description, isForAchievement, type.get());
        assert resource != null;
        resource.setFile(file);
        resource.setRemoteFileName(remoteName);

        String url = "";
        if (!type.equals(ResourceType.TEXT)) {
            url = (String) resourceUploader.upload(resource, Destination.CDN_SERVER);
            System.out.println("New resource available at: " + url);
        }

        //load record into the database
        if (type.equals(ResourceType.TEXT)) {
            TextResource textResource = Resource.ofType(resource);
            assert textResource != null;
            textResource.setContent((String) args.get(0));
            resource = textResource;
        } else if (type.equals(ResourceType.IMAGE)) {
            ImageResource imageResource = Resource.ofType(resource);
            assert imageResource != null;
            imageResource.setURL(url);
            resource = imageResource;
        } else {
            AudioResource audioResource = Resource.ofType(resource);
            assert audioResource != null;
            audioResource.setUrl(url);
            audioResource.setDuration(new Time(1234));
            resource = audioResource;
        }
        return (Resource) resourceUploader.upload(resource, Destination.DATABASE);
    }

    public Resource uploadTextResource(String name, boolean isForAchievement, String content) throws ModelSyncException {
        if (name == null || content == null) throw new NullPointerException();
        if (name.isEmpty() || content.isEmpty()) throw new IllegalArgumentException("Parameters cannot be empty!");

        TextResource textResource = Resource.ofType(ResourceFactory.getResource(name, "", false, ResourceType.TEXT.get()));
        assert textResource != null;
        textResource.setContent(content);

        return resourcesDAO.persist(textResource);
    }

    /***
     *
     * Assigns resource to a course
     *
     * @param course Course object
     * @param resource Resource object
     * @throws DatabaseOutOfSyncException
     * @throws ModelSyncException
     */
    public void assignToCourse(Course course, Resource resource)
            throws DatabaseOutOfSyncException, ModelSyncException {
        if (course == null) throw new IllegalStateException("Course cannot be null");
        if (resource == null) throw new IllegalStateException("Resource cannot be null");
        resourcesDAO.assignToCourse(resource, course);
    }

    /***
     *
     * Assigns a set of resources to a course
     *
     * @param course Course object
     * @param resources List of Resources
     * @throws DatabaseOutOfSyncException
     * @throws ModelSyncException
     */
    public void assignToCourse(Course course, List<Resource> resources)
            throws DatabaseOutOfSyncException, ModelSyncException {
        for (Resource r : resources) {
            resourcesDAO.assignToCourse(r, course);
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
    @Override
    @Concurrent
    public void edit(Resource selectedResource)
            throws DatabaseOutOfSyncException, ModelSyncException {
        resourcesDAO.update(selectedResource);
    }

    /***
     *
     * Returns image object so it can be previewed
     *
     * @param imageResource ImageResource object. Child of a Resource interface
     * @return returns javafx.scene.image.Image Object
     */
    public Image getImageFromResource(ImageResource imageResource) {
        return new Image(imageResource.getURL());
    }

    /***
     *
     * Deletes an object from the database
     *
     * @param resource resource object
     * @throws DatabaseOutOfSyncException
     * @throws ModelSyncException
     */
    @Override
    public void delete(Resource resource)
            throws DatabaseOutOfSyncException, ModelSyncException {
        resourcesDAO.delete(resource);
    }

    @Override
    public Resource createNew(Resource resource) throws ModelSyncException {
        return null;
    }

    @Override
    public Resource getById(int id) throws ModelSyncException {
        return resourcesDAO.getById(id);
    }

    public boolean validateInput(List<String> values) {
        return values.stream()
                .filter(e -> e.isEmpty() || e.contains(";") || e.contains(":") || e.contains("^"))
                .count() == 0;
    }

    public List<Resource> getAudioResById(int id) throws ModelSyncException {
        return resourcesDAO.getByCourseID(id).stream().filter(e -> e.getType() == ResourceType.AUDIO).collect(Collectors.toList());
    }

    public List<Resource> getImageResById(int id) throws ModelSyncException {
        return resourcesDAO.getByCourseID(id).stream().filter(e -> e.getType() == ResourceType.IMAGE).collect(Collectors.toList());
    }

    public List<Resource> getTextResById(int id) throws ModelSyncException {
        return resourcesDAO.getByCourseID(id).stream().filter(e -> e.getType() == ResourceType.TEXT).collect(Collectors.toList());
    }
}
