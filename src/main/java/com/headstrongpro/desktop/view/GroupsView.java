package com.headstrongpro.desktop.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeTableView;
import javafx.scene.text.Text;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Ondřej Soukup on 23.05.2017.
 */
public class GroupsView implements Initializable {

    @FXML
    public Text groupsHeader;
    @FXML
    public TextField searchGroupsTextfield;
    @FXML
    public TreeTableView groupsTreeTableView;
    @FXML
    public Button newGroupButton;
    @FXML
    public Button setButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
