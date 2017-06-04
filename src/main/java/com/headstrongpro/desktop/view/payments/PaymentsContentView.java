package com.headstrongpro.desktop.view.payments;

import com.headstrongpro.desktop.controller.PaymentsController;
import com.headstrongpro.desktop.model.Payment;
import com.headstrongpro.desktop.view.ContentView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;

/**
 * Payments ContentView
 */
public class PaymentsContentView extends ContentView<Payment> implements Initializable {

    // Table columns
    @FXML
    public TableColumn<Payment, Double> valCol;
    @FXML
    public TableColumn<Payment, Date> dueCol, payCol;
    @FXML
    public TableColumn<Payment, String> companyNameCol, cvrCol;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setColumns();
        controller = new PaymentsController();

        loadData();

        mainTable.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue != null) {
                //noinspection unchecked
                mainWindowView.getContextView().changeContextItem(newValue);
            }
        }));
    }

    private void setColumns() {
        companyNameCol.setCellValueFactory(new PropertyValueFactory<>("companyName"));
        cvrCol.setCellValueFactory(new PropertyValueFactory<>("companyCvr"));
        valCol.setCellValueFactory(new PropertyValueFactory<>("value"));
        dueCol.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        payCol.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
    }

}
