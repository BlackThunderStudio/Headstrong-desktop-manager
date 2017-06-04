package com.headstrongpro.desktop.view.courses;

import com.headstrongpro.desktop.controller.CoursesController;
import com.headstrongpro.desktop.core.exception.DatabaseOutOfSyncException;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.core.fxControls.Footer;
import com.headstrongpro.desktop.model.Course;
import com.headstrongpro.desktop.view.ContentSource;
import com.headstrongpro.desktop.view.ContextView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

/**
 * Courses Context View
 */
public class CoursesContextView extends ContextView<Course> implements Initializable {

    // Form text fields
    @FXML
    public TextField nameField;
    @FXML
    public TextArea descriptionField;

    // Bottom controls
    @FXML
    public Button coursesAssignAudioButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        textInputControls = new ArrayList<>(Arrays.asList(
                nameField, descriptionField
        ));

        controller = new CoursesController();

        setDefaults();
    }

    @Override
    public void populateForm() {
        nameField.setText(contextItem.getName());
        descriptionField.setText(contextItem.getDescription());
    }

    @FXML
    public void handleEdit() {
        if (validateInput(nameField)) {
            contextItem.setName(nameField.getText());
            contextItem.setDescription(descriptionField.getText());
            try {
                mainWindowView.getContentView().footer.show("Updating...",
                        Footer.NotificationType.LOADING);
                controller.edit(contextItem);
                mainWindowView.getContentView().footer.show("Course updated",
                        Footer.NotificationType.COMPLETED, Footer.FADE_QUICK);
                mainWindowView.getContentView().handleRefresh();
            } catch (DatabaseOutOfSyncException e) {
                e.printStackTrace();
                handleOutOfSync(handler);
            } catch (ModelSyncException e) {
                e.printStackTrace();
                mainWindowView.getContentView().footer.show(e.getMessage(),
                        Footer.NotificationType.ERROR, Footer.FADE_LONG);
            }
        } else {
            mainWindowView.getContentView().footer.show("Values are invalid!",
                    Footer.NotificationType.WARNING, Footer.FADE_QUICK);
        }
    }

    @FXML
    public void coursesAssignAudioButtonOnClick() {
        if (contextItem != null) {
            mainWindowView.changeContext(ContentSource.COURSES_RES_AUDIO, contextItem);
        }
    }

}
