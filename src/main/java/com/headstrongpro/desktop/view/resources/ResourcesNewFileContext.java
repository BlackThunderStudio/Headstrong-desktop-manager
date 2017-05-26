package com.headstrongpro.desktop.view.resources;


import com.headstrongpro.desktop.model.resource.Resource;
import com.headstrongpro.desktop.view.ContextNewView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by jakub on 26/05/2017.
 */
public class ResourcesNewFileContext extends ContextNewView<Resource> implements Initializable{
    @FXML
    public TextField newImageNameTextfield;
    @FXML
    public TextField newImageDescriptionTextfield;
    @FXML
    public Button selectFileButton;
    @FXML
    public Label newImageFilenameLabel;


    @FXML
    public void saveButtonOnClick() {
        //TODO: implement saving new resource and uploading the file somewhere
    }
    @FXML
    public void cancelButtonOnClick() {
        //TODO: implement canceling adding new resource (load previous context bar, maybe ask the user if he's sure
    }
    @FXML
    public void selectFileButtonOnClick() {
        //TODO: implement selecting local file, which should then be uploaded somewhere
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
