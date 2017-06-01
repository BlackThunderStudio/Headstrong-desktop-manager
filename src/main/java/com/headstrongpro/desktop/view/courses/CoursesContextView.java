package com.headstrongpro.desktop.view.courses;

import com.headstrongpro.desktop.model.Course;
import com.headstrongpro.desktop.view.ContextView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Ondřej Soukup on 26.05.2017.
 */
public class CoursesContextView extends ContextView<Course> implements Initializable {

    /**
     * Be careful to handle actions on correct elements, since there are 4 layouts
     */

    // coursesContextPane.fxml
    @FXML
    public Button coursesEditButton;
    @FXML
    public Button coursesDeleteButton;
    @FXML
    public TextField coursesNameTextfield;
    @FXML
    public TextField coursesDescriptionTextfield;
    @FXML
    public Button coursesAssignImageButton;
    @FXML
    public Button coursesAssignAudioButton;
    @FXML
    public Button coursesAssignTextButton;

    // coursesNewCourseContextPane.fxml
    @FXML
    public TextField coursesNewNameTextfield;
    @FXML
    public TextField coursesNewDescriptionTextfield;
    @FXML
    public Button coursesNewSaveButton;
    @FXML
    public Button coursesNewCancelButton;

    // coursesCategoryContextPane.fxml
    @FXML
    public TextField coursesCatNameTextfield;
    @FXML
    public Button coursesCatEditButton;
    @FXML
    public Button coursesCatDeleteButton;

    // coursesNewCategoryContextPane.fxml
    @FXML
    public TextField coursesNewCatNameTextfield;
    @FXML
    public Button coursesNewCatSaveButton;
    @FXML
    public Button coursesNewCatCancelButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @Override
    public void clearFields(){

    }

    @Override
    public void populateForm(){

    }
}
