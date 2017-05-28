package com.headstrongpro.desktop.view.subscriptions;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Ondřej Soukup on 28.05.2017.
 */
public class SubscriptionsContextView implements Initializable {

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
    public ComboBox subscriptionsRateCombo;
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

    //subscriptionsNewContextPane.fxml - when adding a new subscription
    @FXML
    public TextField subscriptionsNewCompanyTextfield;
    @FXML
    public DatePicker subscriptionsNewStartDatePicker;
    @FXML
    public DatePicker subscriptionsNewEndDatePicker;
    @FXML
    public ComboBox subscriptionsNewRateCombo;
    @FXML
    public TextField subscriptionsNewNoOfUsersTextfield;
    @FXML
    public Label subscriptionsNewPricePrUserLabel;
    @FXML
    public Label subscriptionsNewTotalPriceLabel;
    @FXML
    public Button subscriptionsNewSaveButton;
    @FXML
    public Button subscriptionsNewCancelButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
