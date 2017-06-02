package com.headstrongpro.desktop.view.courses;

import com.headstrongpro.desktop.controller.CourseController;
import com.headstrongpro.desktop.model.Course;
import com.headstrongpro.desktop.view.ContextView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by 1062085 on 02-Jun-17.
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


    CourseController courseController;

    @Override public void initialize(URL location, ResourceBundle resources) { courseController = new CourseController(); }
    @Override public void clearFields(){}
    @Override public void populateForm(){}

    @FXML
    public void saveCourseOnClick(){

    }

    @FXML
    public void cancelCourseOnClick(){

    }

}
