package com.headstrongpro.desktop.view.resources;

import com.headstrongpro.desktop.model.resource.Resource;
import com.headstrongpro.desktop.view.ContextNewView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;
import javafx.event.ActionEvent;

import java.net.URL;
import java.util.ResourceBundle;


/**
 * Created by jakub on 26/05/2017.
 */
public class ResourcesNewTextContext extends ContextNewView<Resource> implements Initializable{
    @FXML
    public TextField resourcesNewTextNameTextfield;
    @FXML
    public HTMLEditor resourcesNewTextEditor;

    @FXML
    public void saveButtonOnClick(ActionEvent event) {
        //TODO: implement saving new resource
    }

    @FXML
    public void cancelButtonOnClick(ActionEvent event) {
        //TODO: implement cancelling adding new resource (load previous context bar, maybe ask the user if he's sure
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
