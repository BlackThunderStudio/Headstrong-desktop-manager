package com.headstrongpro.desktop.view.subscriptions;

import com.headstrongpro.desktop.controller.CompaniesController;
import com.headstrongpro.desktop.controller.SubscriptionsController;
import com.headstrongpro.desktop.core.fxControls.Footer;
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

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

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
    @FXML
    public ComboBox<String> companyCombo;

    private CompaniesController companiesController;
    private ObservableList<Company> companies;

    @Override
    protected void populateForm() {
        Task<ObservableList<Company>> loadCompanies = new Task<ObservableList<Company>>() {
            @Override
            protected ObservableList<Company> call() throws Exception {
                return companiesController.getAll();
            }
        };

        loadCompanies.valueProperty().addListener(((observable, oldValue, newValue) -> {
            if(newValue != null){
                companies = newValue;
                companyCombo.getItems().addAll(
                        newValue.stream()
                                .map(Company::getName)
                                .collect(Collectors.toList())
                );
                companyCombo.getSelectionModel().selectFirst();
            }
        }));

        new Thread(loadCompanies).start();
    }

    @FXML
    public void subscriptionsNewSaveButtonOnClick(ActionEvent event) {
        if(validateInput(subscriptionsNewNoOfUsersTextfield, subscriptionsNewEndDatePicker.getEditor(), subscriptionsNewStartDatePicker.getEditor())){
            //TODO: implement adding
            displayNotImplementedError();
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
    }
}
