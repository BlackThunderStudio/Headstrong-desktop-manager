package com.headstrongpro.desktop.view.groups;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Ondřej Soukup on 26.05.2017.
 */
public class GroupsContextView implements Initializable {

    @FXML
    public Button groupsEditButton;
    @FXML
    public Button groupsDeleteButton;
    @FXML
    public Button groupsSelectParentButton;
    @FXML
    public Button groupsClientsButton;
    @FXML
    public Button groupsCompanyButton;
    @FXML
    public TextField groupsNameTextfield;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
