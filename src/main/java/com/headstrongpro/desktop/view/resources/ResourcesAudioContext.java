package com.headstrongpro.desktop.view.resources;

import com.headstrongpro.desktop.core.controller.ResourcesController;
import com.headstrongpro.desktop.core.exception.DatabaseOutOfSyncException;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.core.fxControls.Footer;
import com.headstrongpro.desktop.model.resource.AudioResource;
import com.headstrongpro.desktop.model.resource.Resource;
import com.headstrongpro.desktop.view.ContextView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;


/**
 * Created by jakub on 26/05/2017.
 */
public class ResourcesAudioContext extends ContextView<AudioResource> implements Initializable {
    @FXML
    public TextField nameField;
    @FXML
    public TextField descriptionField;
    @FXML
    public TextField urlField;
    @FXML
    public Button changeFileButton;
    @FXML
    public Button playPauseButton;

    private ResourcesController controller;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        controller = new ResourcesController();
    }

    @FXML
    public void editButtonOnClick() {
        if(validateInput(nameField, descriptionField)){
            contextItem.setName(nameField.getText());
            contextItem.setDescription(descriptionField.getText());
            try {
                mainWindowView.getContentView().footer.show("Updating " + contextItem.getName() + "...", Footer.NotificationType.LOADING);
                controller.delete(contextItem);
                mainWindowView.getContentView().footer.show("Resource updated.", Footer.NotificationType.COMPLETED, Footer.FADE_QUICK);
                mainWindowView.getContentView().refreshButton.fire();
            } catch (DatabaseOutOfSyncException e) {
                e.printStackTrace();
                handleInconsistency();
            } catch (ModelSyncException e) {
                e.printStackTrace();
                mainWindowView.getContentView().footer.show(e.getMessage(), Footer.NotificationType.ERROR, Footer.FADE_LONG);
            }
        } else {
            mainWindowView.getContentView().footer.show("Values are invalid!", Footer.NotificationType.WARNING);
        }
    }

    @FXML
    public void deleteButtonOnClick() {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setHeaderText("Are you sure you want to delete " + contextItem.getName() + "?");
        a.setContentText("You cannot take that action back");
        Optional<ButtonType> response = a.showAndWait();
        response.ifPresent(btn -> {
            if(ButtonType.OK.equals(btn)){
                mainWindowView.getContentView().footer.show("Deleting " + contextItem.getName() + "...", Footer.NotificationType.LOADING);
                try {
                    controller.delete(contextItem);
                    mainWindowView.getContentView().footer.show("Resource deleted.", Footer.NotificationType.COMPLETED);
                    mainWindowView.getContentView().refreshButton.fire();
                } catch (DatabaseOutOfSyncException e) {
                    e.printStackTrace();
                    handleInconsistency();
                } catch (ModelSyncException e) {
                    e.printStackTrace();
                    mainWindowView.getContentView().footer.show(e.getMessage(), Footer.NotificationType.ERROR, Footer.FADE_LONG);
                }
            }
        });
    }

    @FXML
    public void changeFileButtonOnClick() {
        //TODO: implement selecting local file and uploading it somewhere when saved
    }

    @FXML
    public void playPauseButtonOnClick() {
        //TODO: implement playing and pausing the audio
    }

    @Override
    public void setFields() {
        nameField.setText(contextItem.getName());
        descriptionField.setText(contextItem.getDescription());
    }

    @Override
    protected void clearFields() {
        nameField.clear();
        descriptionField.clear();
        urlField.clear();
    }

    private void handleInconsistency(){
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        mainWindowView.getContentView().footer.show("Warning! Data inconsistency!", Footer.NotificationType.WARNING);
        a.setHeaderText("Warning! Database contains newer data.");
        a.setContentText("Do you want to reload the data? Clicking 'Cancel' will clear all the input");
        Optional<ButtonType> response = a.showAndWait();
        response.ifPresent(e -> {
            if(ButtonType.OK.equals(e)){
                try {
                    changeContextItem(
                            Resource.ofType(
                                    controller.getResourceById(
                                            contextItem.getID()))
                    );
                } catch (ModelSyncException e1) {
                    e1.printStackTrace();
                    mainWindowView.getContentView().footer.show(e1.getMessage(), Footer.NotificationType.ERROR);
                    clearFields();
                }
            } else {
                clearFields();
            }
        });
    }

    //TODO: there should be a listener (or some onAction method) on the slider for seeking through the audio file
}
