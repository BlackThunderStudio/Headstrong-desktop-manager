package com.headstrongpro.desktop.view.resources;

import com.headstrongpro.desktop.controller.ResourcesController;
import com.headstrongpro.desktop.core.exception.DatabaseOutOfSyncException;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.core.fxControls.Footer;
import com.headstrongpro.desktop.model.resource.Resource;
import com.headstrongpro.desktop.model.resource.ResourceType;
import com.headstrongpro.desktop.view.ContentSource;
import com.headstrongpro.desktop.view.ContentView;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Resources Content View
 */
public class ResourcesContentView extends ContentView<Resource> implements Initializable {

    private static final String[] TYPES = {"Audio", "Image", "Text", "All"};

    // Top controls
    @FXML
    public ComboBox<String> resourcesComboBox;

    //Table columns
    @FXML
    public TableColumn<Resource, String> nameCol, descCol;
    @FXML
    public TableColumn<Resource, ResourceType> typeCol;

    // Bottom controls
    @FXML
    public Button assignToCourseButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        resourcesComboBox.getItems().addAll(TYPES);
        resourcesComboBox.getSelectionModel().select(3);

        setColumns();
        controller = new ResourcesController();

        loadData();

        ResourcesController controller = (ResourcesController) this.controller;

        mainTable.getSelectionModel()
                .selectedItemProperty()
                .addListener(((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        if (selected != null && selected.getType() == ResourceType.AUDIO) {
                            AudioResourcesContextView audioController = (AudioResourcesContextView) mainWindowView.getContextView();
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
                        //noinspection unchecked
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
                            mainTable.setItems(this.data);
                            break;
                    }
                }));
    }

    private void setColumns() {
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
    }

    @Override
    @FXML
    public void handleSearch() {
        ResourcesController controller = (ResourcesController) this.controller;
        mainTable.setItems(FXCollections.observableArrayList(
                controller.filterSearch(searchField.getText(), resourcesComboBox.getValue())
        ));
    }

    @FXML
    public void assignToCourseButton_onClick() {
        Resource selected = mainTable.getSelectionModel().getSelectedItem();
        ResourcesController controller = (ResourcesController) this.controller;
        if (selected != null) {
            //TODO: assign to course

            try {
                controller.assignToCourse(null, selected); //TODO: correct session to be given
            } catch (DatabaseOutOfSyncException | ModelSyncException e) {
                e.printStackTrace();
                //TODO: handle error
            }
        }
    }

    @FXML
    public void addNewButtonOnClick() {
        if (resourcesComboBox.getSelectionModel().getSelectedItem().equals("All")) {
            footer.show("Please specify resource type first!", Footer.NotificationType.WARNING);
        } else if (resourcesComboBox.getSelectionModel().getSelectedItem().equals("Text")) {
            mainWindowView.changeContext(ContentSource.RESOURCES_NEW_TEXT);
        } else {
            mainWindowView.changeContext(ContentSource.RESOURCES_NEW_FILE);
        }
    }

}
