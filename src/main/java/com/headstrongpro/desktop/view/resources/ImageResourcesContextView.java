package com.headstrongpro.desktop.view.resources;

import com.headstrongpro.desktop.controller.Concurrent;
import com.headstrongpro.desktop.controller.ResourcesController;
import com.headstrongpro.desktop.core.exception.DatabaseOutOfSyncException;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.core.fxControls.Footer;
import com.headstrongpro.desktop.model.resource.ImageResource;
import com.headstrongpro.desktop.model.resource.Resource;
import com.headstrongpro.desktop.view.ContextView;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

/**
 * Image Resources Context View
 */
public class ImageResourcesContextView extends ContextView<Resource> implements Initializable {

    @FXML
    public GridPane resourceImageGrid;
    @FXML
    public HBox imageViewContainer;
    @FXML
    public TextField nameField, descriptionField;
    @FXML
    public ImageView imageView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        textInputControls.addAll(Arrays.asList(
                nameField,
                descriptionField
        ));
        controller = new ResourcesController();
        imageView.fitWidthProperty().bind(resourceImageGrid.widthProperty());
        imageView.fitHeightProperty().bind(imageViewContainer.heightProperty());
    }

    @FXML
    public void handleEdit() {
        if (validateInput(nameField, descriptionField)) {
            contextItem.setName(nameField.getText());
            contextItem.setDescription(descriptionField.getText());
            try {
                mainWindowView.getContentView().footer.show(String.format("Updating %s...", contextItem.getName()),
                        Footer.NotificationType.LOADING);
                controller.edit(contextItem);
                mainWindowView.getContentView().footer.show("Resource updated.",
                        Footer.NotificationType.COMPLETED, Footer.FADE_QUICK);
                mainWindowView.getContentView().refreshButton.fire();
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
                    Footer.NotificationType.WARNING);
        }
    }

    @Concurrent
    @Override
    public void populateForm() {
        ImageResource imageResource = (ImageResource) contextItem;
        nameField.setText(imageResource.getName());
        descriptionField.setText(imageResource.getDescription());
        Task<Image> loadImage = new Task<Image>() {
            @Override
            protected Image call() throws Exception {
                return new Image(imageResource.getURL());
            }
        };

        loadImage.stateProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue.equals(Worker.State.SUCCEEDED)) {
                mainWindowView.getContentView().footer.show("Image loaded.",
                        Footer.NotificationType.COMPLETED, Footer.FADE_QUICK);
            }
        }));

        loadImage.valueProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue != null) {
                imageView.setImage(newValue);
            }
        }));

        new Thread(loadImage).start();
    }

}
