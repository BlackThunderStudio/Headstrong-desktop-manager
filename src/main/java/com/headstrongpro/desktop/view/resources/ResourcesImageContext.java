package com.headstrongpro.desktop.view.resources;

import com.headstrongpro.desktop.controller.Concurrent;
import com.headstrongpro.desktop.controller.ResourcesController;
import com.headstrongpro.desktop.core.SyncHandler;
import com.headstrongpro.desktop.core.exception.DatabaseOutOfSyncException;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.core.fxControls.Footer;
import com.headstrongpro.desktop.model.resource.ImageResource;
import com.headstrongpro.desktop.model.resource.Resource;
import com.headstrongpro.desktop.view.ContextView;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Created by jakub on 26/05/2017.
 */
public class ResourcesImageContext extends ContextView<ImageResource> implements Initializable {
    @FXML
    public GridPane resourceImageGrid;
    @FXML
    public HBox imageViewContainer;
    @FXML
    public TextField nameField;
    @FXML
    public TextField descriptionField;
    @FXML
    public ImageView imageView;
    @FXML
    public Button editButton;
    @FXML
    public Button deleteButton;

    private ResourcesController controller;
    private SyncHandler<ImageResource> syncHandler = () -> {
        try {
            return Resource.ofType(controller.getResourceById(contextItem.getID()));
        } catch (ModelSyncException e1) {
            e1.printStackTrace();
            mainWindowView.getContentView().footer.show(e1.getMessage(), Footer.NotificationType.ERROR, Footer.FADE_LONG);
        }
        return null;
    };

    @FXML
    public void editButtonOnClick(ActionEvent event) {
        if (validateInput(nameField, descriptionField)) {
            contextItem.setName(nameField.getText());
            contextItem.setDescription(descriptionField.getText());
            try {
                mainWindowView.getContentView().footer.show("Updating " + contextItem.getName() + "...", Footer.NotificationType.LOADING);
                controller.editResource(contextItem);
                mainWindowView.getContentView().footer.show("Resource updated.", Footer.NotificationType.COMPLETED, Footer.FADE_QUICK);
                mainWindowView.getContentView().refreshButton.fire();
            } catch (DatabaseOutOfSyncException e) {
                e.printStackTrace();
                handleOutOfSync(syncHandler);
            } catch (ModelSyncException e) {
                e.printStackTrace();
                mainWindowView.getContentView().footer.show(e.getMessage(), Footer.NotificationType.ERROR, Footer.FADE_LONG);
            }
        } else {
            mainWindowView.getContentView().footer.show("Values are invalid!", Footer.NotificationType.WARNING);
        }
    }

    @FXML
    public void deleteButtonOnClick(ActionEvent event) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setHeaderText("Are you sure you want to delete " + contextItem.getName() + "?");
        a.setContentText("You cannot take that action back");
        Optional<ButtonType> response = a.showAndWait();
        response.ifPresent(btn -> {
            if (ButtonType.OK.equals(btn)) {
                mainWindowView.getContentView().footer.show("Deleting " + contextItem.getName() + "...", Footer.NotificationType.LOADING);
                try {
                    controller.delete(contextItem);
                    mainWindowView.getContentView().footer.show("Resource deleted.", Footer.NotificationType.COMPLETED);
                    mainWindowView.getContentView().refreshButton.fire();
                } catch (DatabaseOutOfSyncException e) {
                    e.printStackTrace();
                    handleOutOfSync(syncHandler);
                } catch (ModelSyncException e) {
                    e.printStackTrace();
                    mainWindowView.getContentView().footer.show(e.getMessage(), Footer.NotificationType.ERROR, Footer.FADE_LONG);
                }
            }
        });
    }

    @Concurrent
    @Override
    public void populateForm() {
        nameField.setText(contextItem.getName());
        descriptionField.setText(contextItem.getDescription());
        Task<Image> loadImage = new Task<Image>() {
            @Override
            protected Image call() throws Exception {
                return new Image(contextItem.getURL());
            }
        };

        loadImage.stateProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue.equals(Worker.State.SUCCEEDED)) {
                mainWindowView.getContentView().footer.show("Image loaded.", Footer.NotificationType.COMPLETED, Footer.FADE_QUICK);
            }
        }));

        loadImage.valueProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue != null) {
                imageView.setImage(newValue);
            }
        }));

        new Thread(loadImage).start();
    }

    @Override
    protected void clearFields() {
        nameField.clear();
        descriptionField.clear();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        controller = new ResourcesController();
        imageView.fitWidthProperty().bind(resourceImageGrid.widthProperty());
        imageView.fitHeightProperty().bind(imageViewContainer.heightProperty());
    }
}
