package com.headstrongpro.desktop.view.resources;

import com.headstrongpro.desktop.model.resource.Resource;
import com.headstrongpro.desktop.view.ContentView;
import com.headstrongpro.desktop.view.ContextView;
import com.sun.org.apache.xml.internal.security.Init;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by jakub on 25/05/2017.
 */
public class ResourcesContext extends ContextView<Resource> implements Initializable {
    // Context bar - Images
    @FXML
    public TextField nameField;
    @FXML
    public TextField descriptionField;
    @FXML
    public TextField urlField;
    @FXML
    public Button editButton;
    @FXML
    public Button deleteButton;
    @FXML
    public ImageView imageView;
    @FXML
    public Button changeFileButton;

    @FXML
    public Slider audioSeekbarSlider;
    @FXML
    public Button plauPauseButton;

    //text resources
    @FXML
    public HTMLEditor textResourcesEditor;
    @FXML
    public WebView textResourcesPreviewWeb;
    @FXML
    public TextField textResourcesNameTextfield;
    @FXML
    public Button textResourcesEditButton;
    @FXML
    public Button textResourcesDeleteButton;

    //new image
    @FXML
    public Button selectFileButton;
    @FXML
    public Button newImageSaveButton;
    @FXML
    public Button newImageCancelButton;
    @FXML
    public Label newImageFilenameLabel;
    @FXML
    public TextField newImageNameTextfield;
    @FXML
    public TextField newImageDescriptionTextfield;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO: Figure out why does this throw the loadStylesheetUnprivileged error
        Platform.runLater(()->{
            textResourcesEditor.lookupAll("ToolBar")
                    .forEach(node->((ToolBar)node).setPrefWidth(259));
        });
    }

    @Override
    public void setFields() {

    }

    @Override
    protected void clearFields() {

    }
}
