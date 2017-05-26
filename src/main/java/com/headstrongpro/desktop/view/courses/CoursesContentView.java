package com.headstrongpro.desktop.view.courses;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeTableView;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Ondřej Soukup on 23.05.2017.
 */
public class CoursesContentView implements Initializable {

    @FXML
    public Text coursesHeader;
    @FXML
    public TextField searchCoursesTextfield;
    @FXML
    public TreeTableView coursesTreeTable;
    @FXML
    public Button newCourseButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

}
