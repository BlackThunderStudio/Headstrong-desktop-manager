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
 * Departments Context View
 */
public class DepartmentsContextView extends ContextView<Department> implements Initializable {

    // Form text fields
    @FXML
    public TextField nameField, descriptionField;

    // Links to related items
    @FXML
    public Button departmentsClientsButton, departmentsCompanyButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @Override
    protected void populateForm() {

    }
}
