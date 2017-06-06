package com.headstrongpro.desktop.view.courses;

import com.headstrongpro.desktop.controller.CoursesController;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.core.fxControls.Footer;
import com.headstrongpro.desktop.model.Course;
import com.headstrongpro.desktop.view.ContentSource;
import com.headstrongpro.desktop.view.ContextView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Courses New Context View
 */
public class CoursesNewContextView extends ContextView<Course> implements Initializable {


    // coursesNewCourseContextPane.fxml
    @FXML
    public TextField coursesNewNameTextfield;
    @FXML
    public TextField coursesNewDescriptionTextfield;
    @FXML
    public Button coursesNewSaveButton;
    @FXML
    public Button coursesNewCancelButton;


    private CoursesController coursesController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        coursesController = new CoursesController();
    }

    @Override
    public void clearFields() {
        coursesNewNameTextfield.clear();
        coursesNewDescriptionTextfield.clear();
    }

    @Override
    public void populateForm() {
    }

    @FXML
    public void saveCourseOnClick() {
        if (!coursesNewNameTextfield.getText().isEmpty() && !coursesNewDescriptionTextfield.getText().isEmpty())
            try {
                mainWindowView.getContentView().footer.show("Creating new course...", Footer.NotificationType.LOADING);
                coursesController.createNew(new Course(coursesNewNameTextfield.getText(), coursesNewDescriptionTextfield.getText()));
                mainWindowView.getContentView().footer.show("Course created.", Footer.NotificationType.COMPLETED);
            } catch (ModelSyncException e) {
                e.printStackTrace();
                mainWindowView.getContentView().footer.show(e.getMessage(), Footer.NotificationType.ERROR, Footer.FADE_LONG);
            }
        mainWindowView.getContentView().refreshButton.fire();
        mainWindowView.changeContext(ContentSource.COURSES);
    }

    @FXML
    public void cancelCourseOnClick() {
        clearFields();
        mainWindowView.changeContext(ContentSource.COURSES);
    }

}
