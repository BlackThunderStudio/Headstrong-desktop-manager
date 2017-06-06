package com.headstrongpro.desktop.view.courses;

import com.headstrongpro.desktop.controller.CoursesController;
import com.headstrongpro.desktop.core.fxControls.Footer;
import com.headstrongpro.desktop.model.Course;
import com.headstrongpro.desktop.view.ContentSource;
import com.headstrongpro.desktop.view.ContentView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Courses Content View
 */
public class CoursesContentView extends ContentView<Course> implements Initializable {

    // Table columns
    @FXML
    public TableColumn<Course, String> nameCol, descCol;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setColumns();
        controller = new CoursesController();

        loadData();

        mainTable.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue != null) {
                footer.show(newValue.getName() + " selected.",
                        Footer.NotificationType.INFORMATION, Footer.FADE_SUPER_QUICK);
                //noinspection unchecked
                mainWindowView.getContextView().changeContextItem(newValue);
            }
        }));
    }

    @FXML
    public void addNewButtonOnClick() {
        mainWindowView.changeContext(ContentSource.COURSES_NEW);
    }

    private void setColumns() {
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
    }

}
