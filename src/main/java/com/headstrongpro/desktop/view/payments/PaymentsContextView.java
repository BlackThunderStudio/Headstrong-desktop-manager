package com.headstrongpro.desktop.view.payments;

import com.headstrongpro.desktop.controller.PaymentsController;
import com.headstrongpro.desktop.core.SyncHandler;
import com.headstrongpro.desktop.core.exception.DatabaseOutOfSyncException;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.core.fxControls.Footer;
import com.headstrongpro.desktop.model.Payment;
import com.headstrongpro.desktop.view.ContextView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.StringConverter;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;
import java.util.ResourceBundle;

import static com.headstrongpro.desktop.core.Utils.FormatterType;
import static com.headstrongpro.desktop.core.Utils.dateFormatter;

/**
 * Payments Context View
 */
public class PaymentsContextView extends ContextView<Payment> implements Initializable {

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

    private PaymentsController controller;
    private SyncHandler<Payment> syncHandler = () -> {
        try {
            return controller.getById(contextItem.getId());
        } catch (ModelSyncException e1) {
            e1.printStackTrace();
            mainWindowView.getContentView().footer.show(e1.getMessage(), Footer.NotificationType.ERROR);
        }
        return null;
    };

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        controller = new PaymentsController();
        paymentsPaidDatePicker.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate object) {
                if (object != null) {
                    return dateFormatter(FormatterType.DATE).format(object);
                } else return "";
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter(FormatterType.DATE));
                } else return null;
            }
        });

        paymentsDueDatePicker.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate object) {
                if (object != null) {
                    return dateFormatter(FormatterType.DATE).format(object);
                } else return "";
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter(FormatterType.DATE));
                } else return null;
            }
        });

        textInputControls.addAll(Arrays.asList(paymentsValueTextfield));
        datePickers.addAll(Arrays.asList(paymentsDueDatePicker, paymentsPaidDatePicker));
        setDefaults();

    }

    @Override
    public void populateForm() {
        paymentsValueTextfield.setText(String.valueOf(contextItem.getValue()));
        paymentsDueDatePicker.getEditor()
                .setText(contextItem
                        .getDueDate()
                        .toString());
        if (contextItem.getTimestamp() != null) {
            paymentsPaidDatePicker.getEditor()
                    .setText(contextItem.getTimestamp()
                            .toString());
        } else paymentsPaidDatePicker.getEditor().setText("");
    }

    @Override
    protected void clearFields() {
        paymentsValueTextfield.clear();
        paymentsDueDatePicker.getEditor().clear();
        paymentsPaidDatePicker.getEditor().clear();
    }

    @FXML
    public void btnMarkAsPaidOnClick() {
        if (paymentsPaidDatePicker.getEditor().getText().isEmpty()) {
            paymentsPaidDatePicker.setValue(LocalDate.now());
        }
        contextItem.setPaid();
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setHeaderText("Invoice marked as paid.");
        a.setContentText("Do you want to update its status?");
        Optional<ButtonType> response = a.showAndWait();
        response.ifPresent(e -> {
            if (ButtonType.OK.equals(e)) {
                //refresh.fire();
            }
        });
    }

    @FXML
    public void handleEdit() {
        if (validateInput(paymentsValueTextfield)) {
            contextItem.setValue(Double.parseDouble(paymentsValueTextfield.getText()));
            contextItem.setDueDate(Date.valueOf(paymentsDueDatePicker.getEditor().getText()));
            contextItem.setTimestamp(Date.valueOf(paymentsPaidDatePicker.getEditor().getText()));
            try {
                mainWindowView.getContentView().footer.show("Updating...", Footer.NotificationType.LOADING);
                controller.edit(contextItem);
                mainWindowView.getContentView().footer.show("Payment updated", Footer.NotificationType.COMPLETED, Footer.FADE_QUICK);
                mainWindowView.getContentView().refreshButton.fire();
            } catch (DatabaseOutOfSyncException e) {
                e.printStackTrace();
                handleOutOfSync(syncHandler);
            } catch (ModelSyncException e) {
                e.printStackTrace();
                mainWindowView.getContentView().footer.show(e.getMessage(), Footer.NotificationType.ERROR, Footer.FADE_LONG);
            }
        } else {
            mainWindowView.getContentView().footer.show("Values are invalid!", Footer.NotificationType.WARNING, Footer.FADE_QUICK);
        }
    }

    /*
    @FXML
    public void handleDelete() {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setHeaderText("Are you sure you want to delete a payment from " + contextItem.getCompanyName() + "?");
        a.setContentText("You cannot take that action back");
        Optional<ButtonType> response = a.showAndWait();
        response.ifPresent(btn -> {
            if (ButtonType.OK.equals(btn)) {
                mainWindowView.getContentView().footer.show("Deleting...", Footer.NotificationType.LOADING);
                try {
                    controller.delete(contextItem);
                    mainWindowView.getContentView().footer.show("Resource deleted.", Footer.NotificationType.COMPLETED);
                    mainWindowView.getContentView().refreshButton.fire();
                } catch (DatabaseOutOfSyncException e) {
                    e.printStackTrace();
                    handleOutOfSync(syncHandler);
                } catch (ModelSyncException e) {
                    mainWindowView.getContentView().footer.show(e.getMessage(), Footer.NotificationType.ERROR, Footer.FADE_LONG);
                }
            }
        });
    }*/

    @FXML
    public void paymentsCompanyButtonOnClick() {
        displayNotImplementedError();
    }

    @FXML
    public void paymentsSubscriptionButtonOnClick() {
        displayNotImplementedError();
    }
}
