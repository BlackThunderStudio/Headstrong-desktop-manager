package com.headstrongpro.desktop.view.payments;

import com.headstrongpro.desktop.model.Payment;
import com.headstrongpro.desktop.view.ContextView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import static com.headstrongpro.desktop.core.Utils.*;

/**
 * Created by Ondřej Soukup on 28.05.2017.
 */
public class PaymentsContextView extends ContextView<Payment> implements Initializable {

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
    @FXML
    public Button btnMarkAsPaid;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        paymentsPaidDatePicker.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate object) {
                if(object != null){
                    return dateFormatter(FormatterType.DATE).format(object);
                } else return "";
            }

            @Override
            public LocalDate fromString(String string) {
                if(string != null && !string.isEmpty()){
                    return LocalDate.parse(string, dateFormatter(FormatterType.DATE));
                } else return null;
            }
        });

        paymentsDueDatePicker.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate object) {
                if(object != null){
                    return dateFormatter(FormatterType.DATE).format(object);
                } else return "";
            }

            @Override
            public LocalDate fromString(String string) {
                if(string != null && !string.isEmpty()){
                    return LocalDate.parse(string, dateFormatter(FormatterType.DATE));
                } else return null;
            }
        });
    }

    @Override
    public void populateForm() {
        paymentsValueTextfield.setText(String.valueOf(contextItem.getValue()));
        paymentsDueDatePicker.getEditor()
                .setText(contextItem
                        .getDueDate()
                        .toString()
                        .replace('-', '.'));
        if(contextItem.getTimestamp() != null){
            paymentsPaidDatePicker.getEditor()
                    .setText(contextItem.getTimestamp()
                            .toString()
                            .replace('-', '.'));
        } else paymentsPaidDatePicker.getEditor().setText("");
    }

    @Override
    protected void clearFields() {
        paymentsValueTextfield.clear();
        paymentsDueDatePicker.getEditor().clear();
        paymentsPaidDatePicker.getEditor().clear();
    }

    @FXML
    public void btnMarkAsPaidOnClick(ActionEvent event) {
        //TODO: to be implemented
    }

    @FXML
    public void paymentsEditButtonOnClick(ActionEvent event) {
        //TODO: to be implemented
    }

    @FXML
    public void paymentsDeleteButtonOnClick(ActionEvent event) {
        //TODO: to be implemented
    }
}
