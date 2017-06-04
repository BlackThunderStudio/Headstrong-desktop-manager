package com.headstrongpro.desktop.view.resources;


import com.headstrongpro.desktop.controller.ResourcesController;
import com.headstrongpro.desktop.core.exception.ConnectionException;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.core.fxControls.Footer;
import com.headstrongpro.desktop.model.resource.Resource;
import com.headstrongpro.desktop.view.ContentSource;
import com.headstrongpro.desktop.view.ContextView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by jakub on 26/05/2017.
 */
public class ResourcesNewFileContext extends ContextView<Resource> implements Initializable {
    @FXML
    public TextField newImageNameTextfield;
    @FXML
    public TextField newImageDescriptionTextfield;
    @FXML
    public Button selectFileButton;
    @FXML
    public Label newImageFilenameLabel;

    private ResourcesController controller;
    private File selectedFile;

    @FXML
    public void saveButtonOnClick() {
        if (selectedFile != null) {
            if (validateInput(newImageNameTextfield, newImageDescriptionTextfield)) {
                try {
                    mainWindowView.getContentView().footer.show("Uploading resource...", Footer.NotificationType.LOADING);
                    controller.uploadLocalFile(selectedFile,
                            newImageNameTextfield.getText(),
                            newImageDescriptionTextfield.getText(),
                            false, //TODO: would be nice to include into a file checkbox "Is for achievement"
                            controller.getResourceType(selectedFile),
                            null);
                    mainWindowView.getContentView().footer.show("File uploaded.", Footer.NotificationType.COMPLETED);
                } catch (ModelSyncException | ConnectionException e) {
                    e.printStackTrace();
                    mainWindowView.getContentView().footer.show(e.getMessage(), Footer.NotificationType.ERROR, Footer.FADE_LONG);
                }
            } else {
                mainWindowView.getContentView().footer.show("Invalid input", Footer.NotificationType.WARNING);
            }
        } else {
            mainWindowView.getContentView().footer.show("No file selected!", Footer.NotificationType.WARNING);
        }
    }

    @FXML
    public void cancelButtonOnClick() {
        mainWindowView.changeContent(ContentSource.RESOURCES);
    }

    @FXML
    public void selectFileButtonOnClick() {
        selectedFile = controller.selectLocalFile();
        if (selectedFile != null) {
            newImageFilenameLabel.setText(selectedFile.getName());
        } else {
            newImageFilenameLabel.setText("");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        controller = new ResourcesController();
        newImageFilenameLabel.setText("");
    }

    @Override
    public void populateForm() {
        //do nothing
    }

    @Override
    protected void clearFields() {
        //do nothing
    }
}
