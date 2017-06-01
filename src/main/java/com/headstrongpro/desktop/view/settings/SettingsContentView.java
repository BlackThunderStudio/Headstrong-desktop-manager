package com.headstrongpro.desktop.view.settings;

import com.headstrongpro.desktop.controller.LogsController;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.core.fxControls.Footer;
import com.headstrongpro.desktop.model.Log;
import com.headstrongpro.desktop.view.ContentView;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;

import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;

/**
 * desktop-manager
 * <p>
 * <p>
 * Created by rajmu on 17.06.01.
 */
public class SettingsContentView extends ContentView implements Initializable {

    @FXML
    public TextField searchResourcesTextfield;
    @FXML
    public TableView<Log> logsTable;
    @FXML
    public TableColumn<Log, Integer> empIdCol;
    @FXML
    public TableColumn<Log, String> tableCol;
    @FXML
    public TableColumn<Log, Integer> itemIdCol;
    @FXML
    public TableColumn<Log, String> actionCol;
    @FXML
    public TableColumn<Log, Date> timeCol;

    ObservableList<Log> logs;

    @FXML
    public void searchResourcesTextfield_onKeyReleased(KeyEvent keyEvent) {
    }

    @FXML
    public void refreshOnClick(ActionEvent event) {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        logs = FXCollections.emptyObservableList();

        Task<Void> initLogs = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Platform.runLater(() -> footer.show("Loading logs...", Footer.NotificationType.LOADING));
                loadLogs();
                return null;
            }
        };

        initLogs.stateProperty().addListener(((observable, oldValue, newValue) -> {
            if(newValue.equals(Worker.State.SUCCEEDED)){
                loadTable(logs);
                footer.show("Logs loaded.", Footer.NotificationType.COMPLETED);
            } else if(newValue.equals(Worker.State.FAILED) || newValue.equals(Worker.State.CANCELLED)){
                footer.show("Loading failed!", Footer.NotificationType.ERROR, Footer.FADE_LONG);
            }
        }));

        new Thread(initLogs).start();
    }

    private void loadTable(ObservableList<Log> logs) {
        logsTable.getColumns().removeAll(empIdCol, tableCol, itemIdCol, actionCol, timeCol);
        logsTable.setItems(logs);
        empIdCol.setCellValueFactory(new PropertyValueFactory<>("headstrongEmpID"));
        tableCol.setCellValueFactory(new PropertyValueFactory<>("tableName"));
        itemIdCol.setCellValueFactory(new PropertyValueFactory<>("itemID"));
        actionCol.setCellValueFactory(new PropertyValueFactory<>("actionType"));
        timeCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        logsTable.getColumns().addAll(empIdCol, tableCol, itemIdCol, actionCol, timeCol);
    }

    private void loadLogs(){
        try {
            logs = new LogsController().getAll();
        } catch (ModelSyncException e) {
            e.printStackTrace();
            footer.show(e.getMessage(), Footer.NotificationType.ERROR, Footer.FADE_LONG);
        }
    }
}
