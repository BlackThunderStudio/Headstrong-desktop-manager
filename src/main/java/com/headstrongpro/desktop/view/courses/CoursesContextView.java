package com.headstrongpro.desktop.view.courses;

import com.headstrongpro.desktop.controller.CourseController;
import com.headstrongpro.desktop.core.SyncHandler;
import com.headstrongpro.desktop.core.exception.DatabaseOutOfSyncException;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.core.fxControls.Footer;
import com.headstrongpro.desktop.model.Course;
import com.headstrongpro.desktop.view.ContextView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
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
    public TextArea coursesNewDescriptionTextfield;
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

    CourseController courseController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        textFields = new ArrayList<>(Arrays.asList(
                coursesNameTextfield, coursesDescriptionTextfield
        ));

        courseController = new CourseController();
        // TODO: description doesnt autobreak multiple lines
        // coursesDescriptionTextarea.setWrapText(true);
    }

    @Override
    public void clearFields(){
        textFields.forEach(TextInputControl::clear);
    }

    @Override
    public void populateForm(){
        coursesNameTextfield.setText(contextItem.getName());
        coursesDescriptionTextfield.setText(contextItem.getDescription());
    }

    private SyncHandler<Course> syncHandler = () -> {
        try {
            return courseController.getByID(contextItem.getId());
        } catch (ModelSyncException e1) {
            e1.printStackTrace();
            mainWindowView.getContentView().footer.show(e1.getMessage(), Footer.NotificationType.ERROR);
        }
        return null;
    };

    @FXML
    public void courseEditOnClick(){
        if (validateInput(coursesNameTextfield)){
            contextItem.setName(coursesNameTextfield.getText());
            contextItem.setDescription(coursesDescriptionTextfield.getText());
            try {
                mainWindowView.getContentView().footer.show("Updating...", Footer.NotificationType.LOADING);
                courseController.edit(contextItem);
                mainWindowView.getContentView().footer.show("Course updated", Footer.NotificationType.COMPLETED, Footer.FADE_QUICK);
                //mainWindowView.getContentView()
            } catch (DatabaseOutOfSyncException e) {
                e.printStackTrace();
                handleOutOfSync(syncHandler);
            } catch (ModelSyncException e) {
                e.printStackTrace();
                mainWindowView.getContentView().footer.show(e.getMessage(), Footer.NotificationType.ERROR, Footer.FADE_LONG);
            }
        } else {
            mainWindowView.getContentView().footer.show("Values are invalid!", Footer.NotificationType.WARNING, Footer.FADE_QUICK);
        }
    }

    @FXML
    public void courseDeleteOnClick(){

    }
}
