package com.headstrongpro.desktop.view.subscriptions;

import com.headstrongpro.desktop.controller.SubscriptionsController;
import com.headstrongpro.desktop.core.SyncHandler;
import com.headstrongpro.desktop.core.Utils;
import com.headstrongpro.desktop.core.exception.ConnectionException;
import com.headstrongpro.desktop.core.exception.DatabaseOutOfSyncException;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.core.fxControls.Footer;
import com.headstrongpro.desktop.model.PaymentRate;
import com.headstrongpro.desktop.model.Subscription;
import com.headstrongpro.desktop.view.ContextView;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.util.StringConverter;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import static com.headstrongpro.desktop.core.Utils.dateFormatter;

/**
 * Subscriptions ContextView
 */
public class SubscriptionsContextView extends ContextView<Subscription> implements Initializable {

    private static final double PRICE_PER_USER = 200.0;

    // Form fields
    @FXML
    public DatePicker startDatePicker, endDatePicker;
    @FXML
    public ComboBox<String> rateComboBox;
    @FXML
    public Label noOfUsersLabel, pricePrUserLabel, paymentRateLabel, totalPriceLabel, nextPaymentLabel, dueLabel;

    // Links to related items
    @FXML
    public Button subscriptionsCompanyButton, subscriptionsPaymentsButton;

    private SubscriptionsController controller; // Data controller

    private List<PaymentRate> rates;

    private SyncHandler<Subscription> handler = () -> {
        try {
            return controller.getById(contextItem.getId());
        } catch (ModelSyncException e) {
            e.printStackTrace();
            mainWindowView.getContentView().footer.show(e.getMessage(), Footer.NotificationType.ERROR);
        }
        return null;
    };

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        controller = new SubscriptionsController();
        startDatePicker.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate object) {
                if (object != null) {
                    return dateFormatter(Utils.FormatterType.DATE).format(object);
                } else return "";
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter(Utils.FormatterType.DATE));
                } else return null;
            }
        });

        endDatePicker.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate object) {
                if (object != null) {
                    return dateFormatter(Utils.FormatterType.DATE).format(object);
                } else return "";
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter(Utils.FormatterType.DATE));
                } else return null;
            }
        });

        try {
            for (PaymentRate pr : controller.getRates())
                rateComboBox.getItems().add(pr.getName());
        } catch (ConnectionException e) {
            e.printStackTrace();
        }

        rateComboBox.getSelectionModel()
                .selectedItemProperty()
                .addListener(((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        paymentRateLabel.setText(rateComboBox.getValue());
                        initTotalAmount();
                    }
                }));

        setDefaults();
    }

    @Override
    protected void clearFields() {
        endDatePicker.getEditor().clear();
        startDatePicker.getEditor().clear();
        totalPriceLabel.setText("");
        paymentRateLabel.setText("");
        pricePrUserLabel.setText("");
        noOfUsersLabel.setText("");
        dueLabel.setText("");
        nextPaymentLabel.setText("");
    }

    @Override
    public void populateForm() {
        startDatePicker.getEditor()
                .setText(contextItem.getStartDate().toString());
        endDatePicker.getEditor()
                .setText(contextItem.getEndDate().toString());

        //labels
        noOfUsersLabel.setText(String.valueOf(contextItem.getNoOfUsers()));
        pricePrUserLabel.setText(String.valueOf(
                contextItem.getRate().getNumberOfMonths() * PRICE_PER_USER
        ));
        paymentRateLabel.setText(rateComboBox.getValue());

        Task<Void> rates = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                getRates();
                return null;
            }
        };

        rates.stateProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue.equals(Worker.State.SUCCEEDED)) {
                rateComboBox.getSelectionModel().select(contextItem.getRate().getName());
                initTotalAmount();
            }
        }));

        new Thread(rates).start();
    }

    @FXML
    public void handleEdit() {
        if (validateInput(endDatePicker.getEditor(), startDatePicker.getEditor())) {
            contextItem.setStartDate(Date.valueOf(startDatePicker.getEditor().getText()));
            contextItem.setEndDate(Date.valueOf(endDatePicker.getEditor().getText()));
            contextItem.setActive(true);
            contextItem.setRate(rates.stream()
                    .filter(e -> e.getName().equals(rateComboBox.getValue()))
                    .findFirst().orElse(null));
            try {
                mainWindowView.getContentView().footer.show("Updating subscription...", Footer.NotificationType.LOADING);
                controller.edit(contextItem);
                mainWindowView.getContentView().footer.show("Subscription updated.", Footer.NotificationType.COMPLETED);
                mainWindowView.getContentView().refreshButton.fire();
            } catch (DatabaseOutOfSyncException e) {
                e.printStackTrace();
                handleOutOfSync(handler);
            } catch (ModelSyncException e) {
                e.printStackTrace();
                mainWindowView.getContentView().footer.show(e.getMessage(), Footer.NotificationType.ERROR, Footer.FADE_LONG);
            }
        } else {
            mainWindowView.getContentView().footer.show("Invalid input!", Footer.NotificationType.WARNING);
        }
    }

    private void getRates() {
        try {
            rates = controller.getRates();
        } catch (ConnectionException e) {
            e.printStackTrace();
            mainWindowView.getContentView().footer.show(e.getMessage(), Footer.NotificationType.ERROR);
        }
    }

    private void initTotalAmount() {
        if (contextItem != null) {
            totalPriceLabel.setText(String.valueOf(
                    PRICE_PER_USER * contextItem.getNoOfUsers() * rates.stream()
                            .filter(e -> e.getName().equals(rateComboBox.getValue()))
                            .map(PaymentRate::getNumberOfMonths)
                            .findFirst().orElse(null)
            ));
        }
    }
}
