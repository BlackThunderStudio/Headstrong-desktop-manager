package com.headstrongpro.desktop.view.subscriptions;

import com.headstrongpro.desktop.controller.CompaniesController;
import com.headstrongpro.desktop.controller.SubscriptionsController;
import com.headstrongpro.desktop.core.Utils;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.core.fxControls.Footer;
import com.headstrongpro.desktop.model.PaymentRate;
import com.headstrongpro.desktop.model.Subscription;
import com.headstrongpro.desktop.model.entity.Company;
import com.headstrongpro.desktop.view.ContentSource;
import com.headstrongpro.desktop.view.ContextView;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import static com.headstrongpro.desktop.core.Utils.dateFormatter;

/**
 * Created by 1062085 on 02-Jun-17.
 */
public class SubscriptionsNewContextView extends ContextView<Subscription> implements Initializable {

    //subscriptionsNewContextPane.fxml - when adding a new subscription
    @FXML
    public DatePicker subscriptionsNewStartDatePicker;
    @FXML
    public DatePicker subscriptionsNewEndDatePicker;
    @FXML
    public ComboBox<PaymentRate> subscriptionsNewRateCombo;
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
    @FXML
    public ComboBox<Company> companyCombo;

    private CompaniesController companiesController;
    private int noOfUsers = -1;

    private static final int PRICE_PER_PERSON = 200;

    private StringConverter<LocalDate> dateConverter = new StringConverter<LocalDate>() {
        @Override
        public String toString(LocalDate object) {
            if(object != null){
                return dateFormatter(Utils.FormatterType.DATE).format(object);
            }
            else return "";
        }

        @Override
        public LocalDate fromString(String string) {
            if(string != null && !string.isEmpty()){
                return LocalDate.parse(string, dateFormatter(Utils.FormatterType.DATE));
            } else return null;
        }
    };

    @Override
    protected void populateForm() {

    }

    @FXML
    public void subscriptionsNewSaveButtonOnClick(ActionEvent event) {
        if(validateInput(subscriptionsNewNoOfUsersTextfield, subscriptionsNewEndDatePicker.getEditor(), subscriptionsNewStartDatePicker.getEditor())){
            Subscription subscription = new Subscription(noOfUsers,
                    true,
                    Date.valueOf(subscriptionsNewStartDatePicker.getValue()),
                    Date.valueOf(subscriptionsNewEndDatePicker.getValue()),
                    subscriptionsNewRateCombo.getValue(),
                    companyCombo.getValue());
            try {
                mainWindowView.getContentView().footer.show("Creating new subscription...", Footer.NotificationType.LOADING);
                controller.createNew(subscription);
                mainWindowView.getContentView().footer.show("Subscription created.", Footer.NotificationType.COMPLETED);
            } catch (ModelSyncException e) {
                e.printStackTrace();
                mainWindowView.getContentView().footer.show(e.getMessage(), Footer.NotificationType.ERROR);
            }
        } else {
            mainWindowView.getContentView().footer.show("Invalid input!", Footer.NotificationType.WARNING, Footer.FADE_LONG);
        }
    }

    @FXML
    public void subscriptionsNewCancelButtonOnClick(ActionEvent event) {
        mainWindowView.changeContext(ContentSource.SUBSCRIPTIONS);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        controller = new SubscriptionsController();
        companiesController = new CompaniesController();

        subscriptionsNewPricePrUserLabel.setText(String.valueOf(PRICE_PER_PERSON));
        subscriptionsNewTotalPriceLabel.setText("");

        subscriptionsNewEndDatePicker.setConverter(dateConverter);
        subscriptionsNewStartDatePicker.setConverter(dateConverter);

        subscriptionsNewNoOfUsersTextfield.textProperty().addListener(((observable, oldValue, newValue) -> {
            int number;
            if(!newValue.isEmpty()){
                try{
                    number = Integer.parseInt(newValue);
                    noOfUsers = number;
                    subscriptionsNewTotalPriceLabel.setText(String.format("%d DKK", PRICE_PER_PERSON * noOfUsers));
                } catch (NumberFormatException e){
                    mainWindowView.getContentView().footer.show("Given input is not a number!", Footer.NotificationType.WARNING);
                    subscriptionsNewNoOfUsersTextfield.clear();
                } finally {
                    mainWindowView.getContentView().refreshButton.fire();
                }
            } else subscriptionsNewTotalPriceLabel.setText("0 DKK");
        }));

        subscriptionsNewEndDatePicker.setDayCellFactory(new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(DatePicker param) {
                return new DateCell(){
                    @Override
                    public void updateItem(LocalDate item, boolean empty){
                        super.updateItem(item, empty);
                        if(!subscriptionsNewStartDatePicker.getEditor().getText().isEmpty()){
                            if(item.isBefore(subscriptionsNewStartDatePicker.getValue())){
                                setDisable(true);
                                setStyle("-fx-background-color: #ffc0cb;");
                            }
                        }
                    }
                };
            }
        });

        subscriptionsNewRateCombo.setCellFactory(new Callback<ListView<PaymentRate>, ListCell<PaymentRate>>() {
            @Override
            public ListCell<PaymentRate> call(ListView<PaymentRate> param) {
                return new ListCell<PaymentRate>(){
                    @Override
                    protected void updateItem(PaymentRate item, boolean empty) {
                        super.updateItem(item, empty);
                        if(item != null){
                            setText(item.getName());
                        }
                    }
                };
            }
        });

        companyCombo.setCellFactory(new Callback<ListView<Company>, ListCell<Company>>() {
            @Override
            public ListCell<Company> call(ListView<Company> param) {
                return new ListCell<Company>(){
                    @Override
                    protected void updateItem(Company item, boolean empty){
                        super.updateItem(item, empty);
                        if(item != null){
                            setText(item.getName());
                        }
                    }
                };
            }
        });

        Task<ObservableList<Company>> loadCompanies = new Task<ObservableList<Company>>() {
            @Override
            protected ObservableList<Company> call() throws Exception {
                return companiesController.getAll();
            }
        };

        Task<List<PaymentRate>> loadrates = new Task<List<PaymentRate>>() {
            @Override
            protected List<PaymentRate> call() throws Exception {
                return new SubscriptionsController().getRates();
            }
        };

        loadCompanies.valueProperty().addListener(((observable, oldValue, newValue) -> {
            if(newValue != null){
                companyCombo.getItems().addAll(newValue);
                companyCombo.getSelectionModel().selectFirst();
            }
        }));

        loadrates.valueProperty().addListener(((observable, oldValue, newValue) -> {
            if(newValue != null){
                subscriptionsNewRateCombo.getItems().addAll(newValue);
                subscriptionsNewRateCombo.getSelectionModel().selectFirst();
            }
        }));

        new Thread(loadCompanies).start();
        new Thread(loadrates).start();
    }
}
