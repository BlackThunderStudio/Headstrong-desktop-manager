package com.headstrongpro.desktop.view.payments;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Ondřej Soukup on 28.05.2017.
 */
public class PaymentsContextView implements Initializable {

    @FXML
    public Button paymentsEditButton;
    @FXML
    public Button paymentsDeleteButton;
    @FXML
    public Button paymentsCompanyButton;
    @FXML
    public Button paymentsSubscriptionButton;
    @FXML
    public DatePicker paymentsDueDatePicker;
    @FXML
    public DatePicker paymentsPaidDatePicker;
    @FXML
    public TextField paymentsValueTextfield;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
