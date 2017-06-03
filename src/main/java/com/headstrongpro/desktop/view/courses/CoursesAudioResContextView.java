package com.headstrongpro.desktop.view.courses;

import com.headstrongpro.desktop.controller.CourseController;
import com.headstrongpro.desktop.controller.ResourcesController;
import com.headstrongpro.desktop.model.resource.AudioResource;
import com.headstrongpro.desktop.model.resource.Resource;
import com.headstrongpro.desktop.model.resource.ResourceType;
import com.headstrongpro.desktop.view.ContextView;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.controlsfx.control.CheckComboBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * desktop-manager
 * <p>
 * <p>
 * Created by rajmu on 17.06.03.
 */
public class CoursesAudioResContextView extends ContextView<Resource> implements Initializable {

    @FXML
    public Label labelName;
    @FXML
    public Button coursesNewSaveButton;
    @FXML
    public Button coursesNewCancelButton;
    @FXML
    public CheckComboBox<String> resourcesCombo;

    private CourseController controller;
    private ResourcesController resourcesController;

    private List<Resource> resources;

    @FXML
    public void saveCourseOnClick(ActionEvent event) {

    }

    @FXML
    public void cancelCourseOnClick(ActionEvent event) {
    }

    @Override
    public void populateForm() {
        if(contextItem != null){
            labelName.setText(contextItem.getName());
        }
    }

    @Override
    protected void clearFields() {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        controller = new CourseController();
        resourcesController = new ResourcesController();

        Task<List<Resource>> initResources = new Task<List<Resource>>() {
            @Override
            protected List<Resource> call() throws Exception {
                return resourcesController.filterByType(ResourceType.AUDIO);
            }
        };

        initResources.valueProperty().addListener(((observable, oldValue, newValue) -> {
            if(newValue != null){
                this.resources = newValue;
                resourcesCombo.getItems().addAll(this.resources.stream().map(Resource::getName).collect(Collectors.toList()));
            }
        }));
    }
}
