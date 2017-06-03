package com.headstrongpro.desktop.view.courses;

import com.headstrongpro.desktop.controller.CourseController;
import com.headstrongpro.desktop.controller.ResourcesController;
import com.headstrongpro.desktop.core.exception.DatabaseOutOfSyncException;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.core.fxControls.Footer;
import com.headstrongpro.desktop.model.Course;
import com.headstrongpro.desktop.model.resource.AudioResource;
import com.headstrongpro.desktop.model.resource.Resource;
import com.headstrongpro.desktop.model.resource.ResourceType;
import com.headstrongpro.desktop.view.ContentSource;
import com.headstrongpro.desktop.view.ContextView;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.controlsfx.control.CheckComboBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * desktop-manager
 * <p>
 * <p>
 * Created by rajmu on 17.06.03.
 */
public class CoursesAudioResContextView extends ContextView<Course> implements Initializable {

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
    private List<Resource> assignedBefore;

    @FXML
    public void saveCourseOnClick(ActionEvent event) {
        Task<Void> assign = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                mainWindowView.getContentView().footer.show("Assigning resources to course", Footer.NotificationType.LOADING);
                handleAssign();
                return null;
            }
        };

        assign.stateProperty().addListener(((observable, oldValue, newValue) -> {
            if(newValue.equals(Worker.State.SUCCEEDED)){
                mainWindowView.getContentView().footer.show("Resources assigned", Footer.NotificationType.COMPLETED);
                mainWindowView.changeContext(ContentSource.COURSES);
            }
        }));

        new Thread(assign).start();
    }

    private void handleAssign(){
        List<Resource> newlyAssigned = new ArrayList<>();
        resourcesCombo.getCheckModel()
                .getCheckedItems()
                .forEach(name -> newlyAssigned.add(resources.stream()
                        .filter(resource -> resource.getName().equals(name))
                        .findFirst()
                        .get()));
        newlyAssigned.removeAll(assignedBefore);
        newlyAssigned.forEach(e -> {
            try {
                controller.assignResources(contextItem, e);
            } catch (DatabaseOutOfSyncException | ModelSyncException e1) {
                e1.printStackTrace();
            }
        });
    }

    @FXML
    public void cancelCourseOnClick(ActionEvent event) {
        mainWindowView.changeContext(ContentSource.COURSES);
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
                try {
                    assignedBefore = controller.getAssignedResources(contextItem).stream()
                            .filter(e -> e.getType().equals(ResourceType.AUDIO)).collect(Collectors.toList());
                    assignedBefore.stream()
                            .map(Resource::getName)
                            .forEach(e -> resourcesCombo.getCheckModel().check(e));
                } catch (ModelSyncException e) {
                    e.printStackTrace();
                    mainWindowView.getContentView().footer.show(e.getMessage(), Footer.NotificationType.ERROR);
                }
            }
        }));

        new Thread(initResources).start();
    }
}
