package com.headstrongpro.desktop.view.courses;

import com.headstrongpro.desktop.controller.CourseController;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.core.fxControls.Footer;
import com.headstrongpro.desktop.model.Course;
import com.headstrongpro.desktop.view.ContentSource;
import com.headstrongpro.desktop.view.ContentView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;

import static javafx.concurrent.Worker.State.CANCELLED;
import static javafx.concurrent.Worker.State.FAILED;
import static javafx.concurrent.Worker.State.SUCCEEDED;

/**
 * Created by Ondřej Soukup on 23.05.2017.
 */
public class CoursesView extends ContentView implements Initializable {

    @FXML
    public Text coursesHeader;
    @FXML
    public TextField searchCoursesTextfield;
    @FXML
    public TableView<Course> coursesTable;
    @FXML
    public TableColumn<Course, String> courseName;
    @FXML
    public TableColumn<Course, String> courseDesc;
    @FXML
    public Button newCourseButton;
    @FXML
    public Button courseRefreshButton;

    private CourseController courseController;
    private ObservableList<Course> courses;
    private Course selected;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        courseController = new CourseController();
        courses = FXCollections.emptyObservableList();

        Task<Void> init = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Platform.runLater(() -> footer.show("Initializing courses", Footer.NotificationType.LOADING));
                loadCourses();
                return null;
            }
        };

        init.stateProperty().addListener(((observable, oldValue, newValue) -> {
            if(newValue.equals(SUCCEEDED)){
                loadTable(courses);
                footer.show("Courses loaded", Footer.NotificationType.COMPLETED);
            } else if(newValue.equals(FAILED) || newValue.equals(CANCELLED)){
                footer.show("Error! Courses could not be loaded!", Footer.NotificationType.ERROR, Footer.FADE_LONG);
            }
        }));

        new Thread(init).start();

        coursesTable.getSelectionModel()
                .selectedItemProperty()
                .addListener(((observable, oldValue, newValue) -> {
                    if(newValue != null) {
                        selected = newValue;
                        mainWindowView.getContextView().changeContextItem(selected);
                    }
                }));
    }

    public void loadCourses(){
        try{
            courses = courseController.getAll();
        } catch (ModelSyncException e){
            e.printStackTrace();
            footer.show(e.getMessage(), Footer.NotificationType.ERROR, Footer.FADE_QUICK);
        }
    }

    public void loadTable(ObservableList<Course> courses){
        coursesTable.getColumns().removeAll(courseName, courseDesc);
        coursesTable.setItems(courses);
        courseName.setCellValueFactory(new PropertyValueFactory<>("name"));
        courseDesc.setCellValueFactory(new PropertyValueFactory<>("description"));
        coursesTable.getColumns().addAll(courseName, courseDesc);
    }

    @FXML
    public void handleSearch(){
        loadTable(courseController.searchByPhrase(searchCoursesTextfield.getText()));
    }

    @FXML
    public void courseRefresh(){
        searchCoursesTextfield.clear();
        Task<Void> sync = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Platform.runLater(() -> footer.show("Synchronising data...", Footer.NotificationType.LOADING));
                loadCourses();
                return null;
            }
        };


        sync.stateProperty().addListener(((observable, oldValue, newValue) -> {
            if(newValue.equals(SUCCEEDED)){
                loadTable(courses);
                footer.show("Courses reloaded successfully", Footer.NotificationType.COMPLETED);
            } else if(newValue.equals(CANCELLED) || newValue.equals(FAILED)){
                footer.show("Could not reload courses!", Footer.NotificationType.ERROR, Footer.FADE_LONG);
            }
        }));

        new Thread(sync).start();
    }

    @FXML
    public void courseNewOnClick(){
        mainWindowView.changeContext(ContentSource.COURSES_NEW);
    }
}
