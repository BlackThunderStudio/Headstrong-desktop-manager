package com.headstrongpro.desktop.view.resources;

import com.headstrongpro.desktop.controller.ResourcesController;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.core.fxControls.Footer;
import com.headstrongpro.desktop.model.resource.Resource;
import com.headstrongpro.desktop.view.ContentSource;
import com.headstrongpro.desktop.view.ContextView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Resources New Text Context View
 */
public class ResourcesNewTextContextView extends ContextView<Resource> implements Initializable {
    @FXML
    public TextField resourcesNewTextNameTextfield;
    @FXML
    public HTMLEditor resourcesNewTextEditor;

    private ResourcesController controller;

    @FXML
    public void saveButtonOnClick() {
        if (!(resourcesNewTextNameTextfield.getText().isEmpty() && resourcesNewTextNameTextfield == null && resourcesNewTextEditor.getHtmlText().isEmpty() && resourcesNewTextEditor == null)) {
            try {
                mainWindowView.getContentView().footer.show("Uploading new resource...", Footer.NotificationType.LOADING);
                controller.uploadTextResource(resourcesNewTextNameTextfield.getText(), false, resourcesNewTextEditor.getHtmlText());
                mainWindowView.getContentView().footer.show("Resource uploaded.", Footer.NotificationType.COMPLETED);
            } catch (ModelSyncException e) {
                e.printStackTrace();
                mainWindowView.getContentView().footer.show(e.getMessage(), Footer.NotificationType.ERROR, Footer.FADE_LONG);
            }
        } else {
            mainWindowView.getContentView().footer.show("Invalid input", Footer.NotificationType.WARNING);
        }
    }

    @FXML
    public void cancelButtonOnClick() {
        mainWindowView.changeContent(ContentSource.RESOURCES);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        controller = new ResourcesController();
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
