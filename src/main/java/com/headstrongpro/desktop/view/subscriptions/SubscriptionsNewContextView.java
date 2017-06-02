package com.headstrongpro.desktop.view.subscriptions;

import javafx.fxml.FXML;
import javafx.scene.control.*;

/**
 * Created by 1062085 on 02-Jun-17.
 */
public class SubscriptionsNewContextView {

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
}
