package com.headstrongpro.desktop.view.resources;

import com.headstrongpro.desktop.model.resource.Resource;
import com.headstrongpro.desktop.view.ContextView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by jakub on 26/05/2017.
 */
public class ResourcesTextContext extends ContextView<Resource> implements Initializable {

    @FXML
    public TextField textResourcesNameTextfield;
    @FXML
    public HTMLEditor textResourcesEditor;
    @FXML
    public WebView textResourcesPreviewWeb;

    @Override

    @FXML
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void editButtonOnClick() {
        //TODO: implement editing the resource
    }

    @FXML
    public void deleteButtonOnClick() {
        //TODO: implement deleting the resource
    }

    @Override
    public void setFields() {

    }

    @Override
    protected void clearFields() {

    }
}
