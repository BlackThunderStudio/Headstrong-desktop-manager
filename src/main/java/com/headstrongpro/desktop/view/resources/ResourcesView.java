package com.headstrongpro.desktop.view.resources;

import com.headstrongpro.desktop.core.Utils;
import com.headstrongpro.desktop.core.controller.ResourcesController;
import com.headstrongpro.desktop.core.exception.DatabaseOutOfSyncException;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.core.fxControls.Footer;
import com.headstrongpro.desktop.core.fxControls.LoadingBar;
import com.headstrongpro.desktop.model.resource.Resource;
import com.headstrongpro.desktop.model.resource.ResourceType;
import com.headstrongpro.desktop.view.ContentSource;
import com.headstrongpro.desktop.view.ContentView;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXSpinner;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

import static javafx.concurrent.Worker.*;
import static javafx.concurrent.Worker.State.*;

/**
 * Created by Ondřej Soukup on 23.05.2017.
 */
public class ResourcesView extends ContentView implements Initializable {

    @FXML
    public TextField searchResourcesTextfield;
    @FXML
    public ComboBox resourcesComboBox;
    @FXML
    public TableView<Resource> resourcesTable;
    @FXML
    public Button assignToCourseButton;

    //Table columns
    @FXML
    public TableColumn nameCol;
    @FXML
    public TableColumn descCol;
    @FXML
    public TableColumn typeCol;


    private ResourcesController controller;
    private ObservableList<Resource> resources;
    private Resource selected;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = FXCollections.observableArrayList();
        Task<Void> init = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                footer.show("Initializing window.", Footer.NotificationType.LOADING);
                controller = new ResourcesController();
                loadResources();
                return null;
            }
        };

        init.stateProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.equals(SUCCEEDED)){
                initTable(this.resources);
                footer.show("Resources loaded.", Footer.NotificationType.COMPLETED);
            } else if(newValue.equals(FAILED) || newValue.equals(CANCELLED)){
                footer.show("Error! Could not load resources!", Footer.NotificationType.ERROR, Footer.FADE_LONG);
            }
        });

        Thread initThread = new Thread(init);
        initThread.setDaemon(true);
        initThread.start();

        resourcesTable.getSelectionModel()
                .selectedItemProperty()
                .addListener(((observable, oldValue, newValue) -> {
            if(newValue != null){
                selected = newValue;
                footer.show(selected.getName() + " selected.", Footer.NotificationType.INFORMATION, Footer.FADE_SUPER_QUICK);
                if(newValue.getType().equals(ResourceType.AUDIO)){
                    mainWindowView.changeContext(ContentSource.RESOURCES_AUDIO);
                    mainWindowView.getContextView().changeContextItem(selected);
                } else if(newValue.getType().equals(ResourceType.IMAGE)){
                    mainWindowView.changeContext(ContentSource.RESOURCES_IMAGE);
                    mainWindowView.getContextView().changeContextItem(selected);
                } else if(newValue.getType().equals(ResourceType.VIDEO)){
                    mainWindowView.changeContext(ContentSource.RESOURCES_AUDIO); //TODO: video type to be added
                    mainWindowView.getContextView().changeContextItem(selected);
                } else if(newValue.getType().equals(ResourceType.TEXT)){
                    mainWindowView.changeContext(ContentSource.RESOURCES_TEXT);
                    mainWindowView.getContextView().changeContextItem(selected);
                }
            }
        }));
    }

    private void loadResources(){
        resources = FXCollections.observableArrayList(controller.getAll());
    }

    private void refresh(){
        Task<Void> refresh = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                footer.show("Refreshing resources...", Footer.NotificationType.LOADING);
                loadResources();
                return null;
            }
        };

        refresh.stateProperty().addListener(((observable, oldValue, newValue) -> {
            if(newValue.equals(SUCCEEDED)){
                footer.show("Resources loaded successfully.", Footer.NotificationType.COMPLETED);
                initTable(resources);
            } else if(newValue.equals(FAILED) || newValue.equals(CANCELLED)) {
                footer.show("Error! Could not refresh the list!", Footer.NotificationType.ERROR, Footer.FADE_LONG);
            }
        }));

        Thread th = new Thread(refresh);
        th.setDaemon(true);
        th.start();
    }

    @SuppressWarnings("unchecked")
    private void initTable(ObservableList<Resource> resources){
        resourcesTable.getColumns().removeAll(nameCol, descCol, typeCol);
        resourcesTable.setItems(resources);

        nameCol.setMinWidth(150);
        descCol.setMinWidth(350);
        typeCol.setMinWidth(80);

        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));

        resourcesTable.getColumns().addAll(nameCol, descCol, typeCol);
    }

    @FXML
    public void searchResourcesTextfield_onKeyReleased(KeyEvent keyEvent) {
        initTable(
                FXCollections.observableArrayList(
                        controller.searchByPhrase(
                                searchResourcesTextfield.getText())));
    }

    @FXML
    public void assignToCourseButton_onClick(ActionEvent actionEvent) {
        Resource selected = (Resource) resourcesTable.getSelectionModel().getSelectedItem();
        if(selected != null){
            //TODO: assign to course

            try {
                controller.assignToCourse(null, selected); //TODO: correct session to be given
            } catch (DatabaseOutOfSyncException e) {
                e.printStackTrace();
                //TODO: handle error
            } catch (ModelSyncException e) {
                e.printStackTrace();
                //TODO: handle error
            }
        }
    }

    @FXML
    public void newResourceButton_onClick(ActionEvent actionEvent) {
        //TODO: to be implemented
    }

    @FXML
    public void refreshOnClick(ActionEvent actionEvent) {
        refresh();
    }
}
