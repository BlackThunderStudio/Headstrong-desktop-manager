package com.headstrongpro.desktop.view.groups;

import com.headstrongpro.desktop.model.Group;
import com.headstrongpro.desktop.view.ContextView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Groups Context View
 */
public class GroupsContextView extends ContextView<Group> implements Initializable {

    // Form text fields
    @FXML
    public TextField nameField;

    // Links to related items
    @FXML
    public Button groupsSelectParentButton, groupsClientsButton, groupsCompanyButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @Override
    protected void populateForm() {

    }
}
