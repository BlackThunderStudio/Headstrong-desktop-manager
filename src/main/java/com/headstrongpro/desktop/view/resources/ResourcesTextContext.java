package com.headstrongpro.desktop.view.resources;

import com.headstrongpro.desktop.controller.ResourcesController;
import com.headstrongpro.desktop.core.exception.DatabaseOutOfSyncException;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.core.fxControls.Footer;
import com.headstrongpro.desktop.model.resource.Resource;
import com.headstrongpro.desktop.model.resource.TextResource;
import com.headstrongpro.desktop.view.ContextView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Created by jakub on 26/05/2017.
 */
public class ResourcesTextContext extends ContextView<TextResource> implements Initializable {

    @FXML
    public TextField textResourcesNameTextfield;
    @FXML
    public HTMLEditor textResourcesEditor;
    @FXML
    public WebView textResourcesPreviewWeb;

    private ResourcesController controller;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        controller = new ResourcesController();
    }

    @FXML
    public void editButtonOnClick() {
        if(validateInput(textResourcesNameTextfield)){
            contextItem.setName(contextItem.getName());
            contextItem.setContent(textResourcesEditor.getHtmlText());
            try {
                mainWindowView.getContentView().footer.show("Updating " + contextItem.getName() + "...", Footer.NotificationType.LOADING);
                controller.editResource(contextItem);
                mainWindowView.getContentView().footer.show("Resource updated.", Footer.NotificationType.COMPLETED, Footer.FADE_QUICK);
                mainWindowView.getContentView().refreshButton.fire();
            } catch (ModelSyncException e) {
                e.printStackTrace();
                mainWindowView.getContentView().footer.show(e.getMessage(), Footer.NotificationType.ERROR, Footer.FADE_LONG);
            } catch (DatabaseOutOfSyncException e) {
                e.printStackTrace();
                handleInconsistency();
            }
        } else {
            mainWindowView.getContentView().footer.show("Values are invalid!", Footer.NotificationType.WARNING);
        }
    }

    private void handleInconsistency() {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        mainWindowView.getContentView().footer.show("Warning! Data inconsistency!", Footer.NotificationType.WARNING);
        a.setHeaderText("Warning! Database contains newer data.");
        a.setContentText("Do you want to reload the data? Clicking 'Cancel' will clear all the input");
        Optional<ButtonType> response = a.showAndWait();
        response.ifPresent(e -> {
            if(ButtonType.OK.equals(e)){
                try {
                    changeContextItem(Resource.ofType(controller.getResourceById(contextItem.getID())));
                } catch (ModelSyncException e1) {
                    mainWindowView.getContentView().footer.show(e1.getMessage(), Footer.NotificationType.ERROR);
                    clearFields();
                }
            } else {
                clearFields();
            }
        });
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
                    mainWindowView.getContentView().footer.show(e.getMessage(), Footer.NotificationType.ERROR, Footer.FADE_LONG);
                }
            }
        });
    }

    @Override
    public void setFields() {
        textResourcesNameTextfield.setText(contextItem.getName());
        textResourcesEditor.setHtmlText(contextItem.getContent());
    }

    @Override
    protected void clearFields() {
        textResourcesNameTextfield.clear();
    }

    @FXML
    public void editorOnKeyReleased(KeyEvent keyEvent) {
        System.out.println("editorOnKeyReleased");
        sendToPreview();
    }

    private void sendToPreview() {
        String html = textResourcesEditor.getHtmlText();
        textResourcesPreviewWeb.getEngine().load(html); //TODO: for some reason doesn't work
    }

    @FXML
    public void editorOnMouseClicked(MouseEvent mouseEvent) {
        System.out.println("editorOnMouseClicked");
        sendToPreview();
    }
}
