package com.headstrongpro.desktop.view;

import com.sun.org.apache.xml.internal.security.Init;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.image.ImageView;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by jakub on 25/05/2017.
 */
public class ResourcesContext implements Initializable {
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
    @FXML

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(()->{
            textResourcesEditor.lookupAll("ToolBar")
                    .forEach(node->((ToolBar)node).setPrefWidth(259));
        });
    }

}
