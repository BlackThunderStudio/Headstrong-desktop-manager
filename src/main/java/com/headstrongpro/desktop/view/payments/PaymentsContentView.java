package com.headstrongpro.desktop.view.payments;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Ondřej Soukup on 23.05.2017.
 */
public class PaymentsContentView implements Initializable {

    @FXML
    public Text paymentsHeader;
    @FXML
    public TextField searchPaymentsTextfield;
    @FXML
    public TableView paymentsTable;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
