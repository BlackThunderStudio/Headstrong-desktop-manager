package com.headstrongpro.desktop.view.subscriptions;

import com.headstrongpro.desktop.controller.SubscriptionsController;
import com.headstrongpro.desktop.core.Utils;
import com.headstrongpro.desktop.core.exception.ConnectionException;
import com.headstrongpro.desktop.model.PaymentRate;
import com.headstrongpro.desktop.model.Subscription;
import com.headstrongpro.desktop.view.ContextView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.StringConverter;

import java.net.ConnectException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import static com.headstrongpro.desktop.core.Utils.dateFormatter;

/**
 * Created by Ondřej Soukup on 28.05.2017.
 */
public class SubscriptionsContextView extends ContextView<Subscription> implements Initializable {

    // subscriptionsContextPane.fxml
    @FXML
    public Button subscriptionsEditButton;
    @FXML
    public Button subscriptionsDeleteButton;
    @FXML
    public DatePicker subscriptionsStartDatePicker;
    @FXML
    public DatePicker subscriptionsEndDatePicker;
    @FXML
    public ComboBox<String> subscriptionsRateCombo;
    @FXML
    public Label subscriptionsNoOfUsersLabel;
    @FXML
    public Label subscriptionsPricePrUserLabel;
    @FXML
    public Label subscriptionsPaymentRateLabel;
    @FXML
    public Label subscriptionsTotalPriceLabel;
    @FXML
    public Label subscriptionsNextPaymentLabel;
    @FXML
    public Label subscriptionsDueLabel;
    @FXML
    public Button subscriptionsCompanyButton;
    @FXML
    public Button subscriptionsPaymentsButton;



    private SubscriptionsController subscriptionsController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        subscriptionsController = new SubscriptionsController();
        subscriptionsStartDatePicker.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate object) {
                if(object != null){
                    return dateFormatter(Utils.FormatterType.DATE).format(object);
                } else return "";
            }

            @Override
            public LocalDate fromString(String string) {
                if(string != null && !string.isEmpty()){
                    return LocalDate.parse(string, dateFormatter(Utils.FormatterType.DATE));
                } else return null;
            }
        });

        subscriptionsEndDatePicker.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate object) {
                if(object != null){
                    return dateFormatter(Utils.FormatterType.DATE).format(object);
                } else return "";
            }

            @Override
            public LocalDate fromString(String string) {
                if(string != null && !string.isEmpty()){
                    return LocalDate.parse(string, dateFormatter(Utils.FormatterType.DATE));
                } else return null;
            }
        });


        try {
            for (PaymentRate pr : subscriptionsController.getRates())
                subscriptionsRateCombo.getItems().add(pr.getName());
        } catch (ConnectionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clearFields(){

    }

    @Override
    public void populateForm(){
        subscriptionsStartDatePicker.getEditor()
                .setText(contextItem.getStartDate().toString());
        subscriptionsEndDatePicker.getEditor()
                .setText(contextItem.getEndDate().toString());
        //TODO combobox change item
    }
}
