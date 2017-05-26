package com.headstrongpro.desktop.view.resources;

import com.headstrongpro.desktop.model.resource.AudioResource;
import com.headstrongpro.desktop.model.resource.Resource;
import com.headstrongpro.desktop.view.ContextView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * desktop-manager
 * <p>
 * <p>
 * Created by rajmu on 17.05.26.
 */
public class ResourceAudioContext extends ContextView<Resource> implements Initializable {


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
    public Button changeFileButton;

    @FXML
    public Slider audioSeekbarSlider;

    @FXML
    public Button plauPauseButton;

    private AudioResource audio;

    @Override
    public void setFields() {
        nameField.setText(contextItem.getName());
        descriptionField.setText(contextItem.getDescription());
        audio = Resource.ofType(contextItem);
    }

    @Override
    protected void clearFields() {
        nameField.clear();
        descriptionField.clear();
        urlField.clear();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
