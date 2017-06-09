package com.headstrongpro.desktop.view.resources;

import com.headstrongpro.desktop.controller.ResourcesController;
import com.headstrongpro.desktop.core.exception.DatabaseOutOfSyncException;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.core.fxControls.Footer;
import com.headstrongpro.desktop.model.resource.Resource;
import com.headstrongpro.desktop.model.resource.TextResource;
import com.headstrongpro.desktop.view.ContextView;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.Collections;
import java.util.ResourceBundle;

/**
 * Text Resources Context View
 */
public class TextResourcesContextView extends ContextView<Resource> implements Initializable {

    @FXML
    public TextField nameField;
    @FXML
    public HTMLEditor textResourcesEditor;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        textInputControls.addAll(Collections.singletonList(
                nameField
        ));
        controller = new ResourcesController();
    }

    @FXML
    public void handleEdit() {
        TextResource textResource = (TextResource) contextItem;

        if (validateInput(nameField)) {
            textResource.setName(textResource.getName());
            textResource.setContent(textResourcesEditor.getHtmlText());
            try {
                mainWindowView.getContentView().footer.show(String.format("Updating %s...", textResource.getName()),
                        Footer.NotificationType.LOADING);
                controller.edit(textResource);
                mainWindowView.getContentView().footer.show("Resource updated.",
                        Footer.NotificationType.COMPLETED, Footer.FADE_QUICK);
                mainWindowView.getContentView().refreshButton.fire();
            } catch (ModelSyncException e) {
                e.printStackTrace();
                mainWindowView.getContentView().footer.show(e.getMessage(),
                        Footer.NotificationType.ERROR, Footer.FADE_LONG);
            } catch (DatabaseOutOfSyncException e) {
                e.printStackTrace();
                handleOutOfSync(handler);
            }
        } else {
            mainWindowView.getContentView().footer.show("Values are invalid!",
                    Footer.NotificationType.WARNING);
        }
    }

    @Override
    public void populateForm() {
        TextResource textResource = (TextResource) contextItem;

        nameField.setText(textResource.getName());
        textResourcesEditor.setHtmlText(textResource.getContent());

        Task<String> loadTextContent = new Task<String>() {
            @Override
            protected String call() throws Exception {
                return textResource.getContent();
            }
        };

        loadTextContent.valueProperty().addListener((q, w, e) -> {
            if (e != null) {
                textResourcesEditor.setHtmlText(e);
            }
        });

        new Thread(loadTextContent).start();
    }
}
