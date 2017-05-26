package com.headstrongpro.desktop.view.resources;

import com.headstrongpro.desktop.model.resource.Resource;
import com.headstrongpro.desktop.view.ContextView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

/**
 * Created by jakub on 26/05/2017.
 */
public class ResourcesImageContext extends ContextView<Resource>{

    @FXML
    public TextField nameField;
    @FXML
    public TextField descriptionField;
    @FXML
    public TextField urlField;
    @FXML
    public ImageView imageView;
    @FXML
    public Button changeFileButton;

    @FXML
    public void editButtonOnClick(ActionEvent event) {
        //TODO: implement editing of the resource
    }

    @FXML
    public void deleteButtonOnClick(ActionEvent event) {
        //TODO: implement deleting of the resource
    }

    @FXML
    public void changeFileButtonOnClick(ActionEvent event) {
        //TODO: implement selecting local file and uploading it somewhere when saved
    }


    @Override
    public void setFields() {

    }

    @Override
    protected void clearFields() {

    }
}
