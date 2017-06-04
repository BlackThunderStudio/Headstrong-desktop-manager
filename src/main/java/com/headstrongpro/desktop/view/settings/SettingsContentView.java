package com.headstrongpro.desktop.view.settings;

import com.headstrongpro.desktop.controller.LogsController;
import com.headstrongpro.desktop.model.Log;
import com.headstrongpro.desktop.view.ContentView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;

/**
 * Settings Content View
 */
public class SettingsContentView extends ContentView<Log> implements Initializable {

    // Table columns
    @FXML
    public TableColumn<Log, Integer> empIdCol, itemIdCol;
    @FXML
    public TableColumn<Log, String> tableCol, actionCol;
    @FXML
    public TableColumn<Log, Date> timeCol;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setColumns();
        controller = new LogsController();

        loadData();
    }

    private void setColumns() {
        empIdCol.setCellValueFactory(new PropertyValueFactory<>("headstrongEmpID"));
        tableCol.setCellValueFactory(new PropertyValueFactory<>("tableName"));
        itemIdCol.setCellValueFactory(new PropertyValueFactory<>("itemID"));
        actionCol.setCellValueFactory(new PropertyValueFactory<>("actionType"));
        timeCol.setCellValueFactory(new PropertyValueFactory<>("date"));
    }

}
