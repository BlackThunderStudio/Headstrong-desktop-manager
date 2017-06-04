package com.headstrongpro.desktop.view.resources;

import com.headstrongpro.desktop.controller.ResourcesController;
import com.headstrongpro.desktop.core.exception.DatabaseOutOfSyncException;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.core.fxControls.Footer;
import com.headstrongpro.desktop.model.Course;
import com.headstrongpro.desktop.model.resource.Resource;
import com.headstrongpro.desktop.model.resource.ResourceType;
import com.headstrongpro.desktop.view.ContentSource;
import com.headstrongpro.desktop.view.ContentView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

import static javafx.concurrent.Worker.State.*;

/**
 * Resources ContentView
 */
public class ResourcesContentView extends ContentView<Resource> implements Initializable {

    private static final String[] TYPES = {"Audio", "Image", "Text", "All"};

    // Top controls
    @FXML
    public ComboBox<String> resourcesComboBox;

    //Table columns
    @FXML
    public TableColumn<Resource, String> nameCol;
    @FXML
    public TableColumn<Resource, String> descCol;
    @FXML
    public TableColumn<Resource, ResourceType> typeCol;

    // Bottom controls
    @FXML
    public Button assignToCourseButton;

    private ResourcesController controller;
    private ObservableList<Resource> resources;
    private Resource selected;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        resourcesComboBox.getItems().addAll(TYPES);
        resourcesComboBox.getSelectionModel().select(3);
        this.resources = FXCollections.observableArrayList();

        setColumns();

        Task<Void> init = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Platform.runLater(() -> footer.show("Initializing window.", Footer.NotificationType.LOADING));
                controller = new ResourcesController();
                loadResources();
                return null;
            }
        };

        init.stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals(SUCCEEDED)) {

                try {
                    if (mainWindowView.getContextView().getContextItem() instanceof Resource) {
                        Course c = (Course) mainWindowView.getContextView().getContextItem();
                        mainTable.setItems(FXCollections.observableList(controller.getTextResById(c.getId())));
                        mainWindowView.getContextView().changeContextItem(null);
                        //TODO: needs a bit of tweaking to display from courses
                    } else
                        mainTable.setItems(this.resources);
                } catch (ModelSyncException e) {
                    e.printStackTrace();
                }
                footer.show("Resources loaded.", Footer.NotificationType.COMPLETED);
            } else if (newValue.equals(FAILED) || newValue.equals(CANCELLED)) {
                footer.show("Error! Could not load resources!", Footer.NotificationType.ERROR, Footer.FADE_LONG);
            }
        });

        Thread initThread = new Thread(init);
        initThread.setDaemon(true);
        initThread.start();

        mainTable.getSelectionModel()
                .selectedItemProperty()
                .addListener(((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        if (selected != null && selected.getType() == ResourceType.AUDIO) {
                            ResourcesAudioContext audioController = (ResourcesAudioContext) mainWindowView.getContextView();
                            audioController.stopAudio();
                        }
                        selected = newValue;
                        footer.show(selected.getName() + " selected.", Footer.NotificationType.INFORMATION, Footer.FADE_SUPER_QUICK);
                        if (newValue.getType().equals(ResourceType.AUDIO)) {
                            mainWindowView.changeContext(ContentSource.RESOURCES_AUDIO);
                        } else if (newValue.getType().equals(ResourceType.IMAGE)) {
                            mainWindowView.changeContext(ContentSource.RESOURCES_IMAGE);
                        } else if (newValue.getType().equals(ResourceType.VIDEO)) {
                            mainWindowView.changeContext(ContentSource.RESOURCES_AUDIO); //TODO: video type to be added
                        } else if (newValue.getType().equals(ResourceType.TEXT)) {
                            mainWindowView.changeContext(ContentSource.RESOURCES_TEXT);
                        }
                        mainWindowView.getContextView().changeContextItem(Resource.ofType(selected));
                    }
                }));

        resourcesComboBox.getSelectionModel()
                .selectedItemProperty()
                .addListener(((observable, oldValue, newValue) -> {
                    switch (newValue) {
                        case "Audio":
                            addNewButton.setText("New Audio");
                            mainTable.setItems(FXCollections.observableArrayList(
                                    controller.filterByType(ResourceType.AUDIO)
                            ));
                            break;
                        case "Image":
                            addNewButton.setText("New Image");
                            mainTable.setItems(FXCollections.observableArrayList(
                                    controller.filterByType(ResourceType.IMAGE)
                            ));
                            break;
                        case "Text":
                            addNewButton.setText("New Text");
                            mainTable.setItems(FXCollections.observableArrayList(
                                    controller.filterByType(ResourceType.TEXT)
                            ));
                            break;
                        case "All":
                            addNewButton.setText("New");
                            mainTable.setItems(this.resources);
                            break;
                    }
                }));
    }

    private void loadResources() {
        try {
            resources = FXCollections.observableArrayList(controller.getAll());
        } catch (ModelSyncException e) {
            mainWindowView.getContentView().footer.show(e.getMessage(), Footer.NotificationType.ERROR);
        }
    }

    private void refresh() {
        Task<Void> refresh = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Platform.runLater(() -> footer.show("Refreshing resources...", Footer.NotificationType.LOADING));
                loadResources();
                return null;
            }
        };

        refresh.stateProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue.equals(SUCCEEDED)) {
                footer.show("Resources loaded successfully.", Footer.NotificationType.COMPLETED);
                mainTable.setItems(this.resources);
            } else if (newValue.equals(FAILED) || newValue.equals(CANCELLED)) {
                footer.show("Error! Could not refresh the list!", Footer.NotificationType.ERROR, Footer.FADE_LONG);
            }
        }));

        Thread th = new Thread(refresh);
        th.setDaemon(true);
        th.start();
    }

    private void setColumns() {
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
    }

    @FXML
    public void handleSearch() {
        mainTable.setItems(FXCollections.observableArrayList(
                controller.filterSearch(searchField.getText(), resourcesComboBox.getValue())
        ));
    }

    @FXML
    public void assignToCourseButton_onClick() {
        Resource selected = mainTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            //TODO: assign to course

            try {
                controller.assignToCourse(null, selected); //TODO: correct session to be given
            } catch (DatabaseOutOfSyncException e) {
                e.printStackTrace();
                //TODO: handle error
            } catch (ModelSyncException e) {
                e.printStackTrace();
                //TODO: handle error
            }
        }
    }

    @FXML
    public void newResourceButton_onClick() {
        if (resourcesComboBox.getSelectionModel().getSelectedItem().equals("All")) {
            footer.show("Please specify resource type first!", Footer.NotificationType.WARNING);
        } else if (resourcesComboBox.getSelectionModel().getSelectedItem().equals("Text")) {
            mainWindowView.changeContext(ContentSource.RESOURCES_NEW_TEXT);
        } else {
            mainWindowView.changeContext(ContentSource.RESOURCES_NEW_FILE);
        }
    }

    @FXML
    public void handleRefresh() {
        searchField.clear();
        refresh();
    }
}
