package com.headstrongpro.desktop.view.departments;

import com.headstrongpro.desktop.model.Department;
import com.headstrongpro.desktop.view.ContextView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Ondřej Soukup on 26.05.2017.
 */
public class DepartmentsContextView extends ContextView<Department> implements Initializable {

    @FXML
    public Button departmentsDeleteButton;
    @FXML
    public Button departmentsEditButton;
    @FXML
    public Button departmentsClientsButton;
    @FXML
    public Button departmentsCompanyButton;
    @FXML
    public TextField departmentsNameTextfield;
    @FXML
    public TextField departmentsDescriptionTextfield;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @Override
    public void setFields() {

    }

    @Override
    protected void clearFields() {

    }
}
