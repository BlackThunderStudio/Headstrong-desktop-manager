package com.headstrongpro.desktop.view;

import com.headstrongpro.desktop.core.Utils;
import com.headstrongpro.desktop.core.controller.ResourcesController;
import com.headstrongpro.desktop.core.exception.DatabaseOutOfSyncException;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.model.resource.Resource;
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
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

import static javafx.concurrent.Worker.*;
import static javafx.concurrent.Worker.State.*;

/**
 * Created by Ondřej Soukup on 23.05.2017.
 */
public class ResourcesView implements Initializable {

    @FXML
    public TextField searchResourcesTextfield;
    @FXML
    public ComboBox resourcesComboBox;
    @FXML
    public TableView resourcesTable;
    @FXML
    public Button newResourceButton;
    @FXML
    public Text resourcesHeader;
    @FXML
    public Button assignToCourseButton;

    //Table columns
    @FXML
    public TableColumn nameCol;
    @FXML
    public TableColumn descCol;
    @FXML
    public TableColumn typeCol;
    @FXML
    public JFXSpinner loadingSpinner;
    @FXML
    public Label loadingLabel;

    private ResourcesController controller;
    private ObservableList<Resource> resources;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadingSpinner.setVisible(false);
        loadingLabel.setVisible(false);
        this.resources = FXCollections.observableArrayList();
        Utils.WaitingSpinner waitingSpinner = new Utils.WaitingSpinner(loadingSpinner, loadingLabel);
        Task<Void> init = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                waitingSpinner.init("Loading resources...");
                controller = new ResourcesController();
                loadResources();
                return null;
            }
        };

        init.stateProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.equals(SUCCEEDED)){
                waitingSpinner.close();
                initTable(this.resources);
            } else if(newValue.equals(FAILED) || newValue.equals(CANCELLED)){
                //TODO: handle error
            }
        });

        Thread initThread = new Thread(init);
        initThread.start();
    }

    private void loadResources(){
        resources = FXCollections.observableArrayList(controller.getAll());
    }

    @SuppressWarnings("unchecked")
    private void initTable(ObservableList<Resource> resources){
        resourcesTable.getColumns().removeAll(nameCol, descCol, typeCol);
        resourcesTable.setItems(resources);

        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));

        resourcesTable.getColumns().addAll(nameCol, descCol, typeCol);
    }

    @FXML
    public void searchResourcesTextfield_onKeyReleased(KeyEvent keyEvent) {
        initTable(FXCollections.observableArrayList(controller.searchByPhrase(searchResourcesTextfield.getText())));
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
}
