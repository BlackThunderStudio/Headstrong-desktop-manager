package com.headstrongpro.desktop.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

/**
 * Created by jakub on 25/05/2017.
 */
public class ResourcesContext {
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

}
