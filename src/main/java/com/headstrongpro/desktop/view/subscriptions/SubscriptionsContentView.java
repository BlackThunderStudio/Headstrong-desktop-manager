package com.headstrongpro.desktop.view.subscriptions;

import com.headstrongpro.desktop.controller.SubscriptionsController;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.core.fxControls.Footer;
import com.headstrongpro.desktop.model.Subscription;
import com.headstrongpro.desktop.model.entity.Company;
import com.headstrongpro.desktop.view.ContentView;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import javafx.util.Callback;
import java.net.URL;
import java.util.ResourceBundle;

import static javafx.concurrent.Worker.State.CANCELLED;
import static javafx.concurrent.Worker.State.FAILED;
import static javafx.concurrent.Worker.State.SUCCEEDED;

/**
 * Created by Ondřej Soukup on 23.05.2017.
 */
public class SubscriptionsContentView extends ContentView implements Initializable {

    @FXML
    public Text subscriptionsHeader;
    @FXML
    public TextField searchSubscriptionsTextfield;
    @FXML
    public TableView<Subscription> subscriptionsTable;
    @FXML
    public TableColumn<Subscription, String> subscriptionsCompanyCol;
    @FXML
    public TableColumn<Subscription, String> subscriptionsStartCol;
    @FXML
    public TableColumn<Subscription, String> subscriptionsEndCol;
    @FXML
    public TableColumn<Subscription, String> subscriptionsUsersCol;
    @FXML
    public TableColumn<Subscription, String> subscriptionsRateCol;
    @FXML
    public Button newSubscriptionButton;

    private SubscriptionsController subscriptionsController;
    private ObservableList<Subscription> subscriptions;
    private Subscription selected;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        subscriptionsController = new SubscriptionsController();
        subscriptions = FXCollections.emptyObservableList();
        Task<Void> init = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Platform.runLater(() -> footer.show("Loading subscriptions...", Footer.NotificationType.LOADING));
                loadSubscriptions();
                return null;
            }
        };

        init.stateProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue.equals(SUCCEEDED)) {
                loadTable(subscriptions);
                footer.show("Subscriptions loaded successfully!", Footer.NotificationType.COMPLETED);
            } else if (newValue.equals(FAILED) || newValue.equals(CANCELLED)) {
                footer.show("Error while loading subscriptions!", Footer.NotificationType.ERROR, Footer.FADE_LONG);
            }
        }));

        new Thread(init).start();

        subscriptionsTable.getSelectionModel().selectedItemProperty().addListener((o, e, c) -> {
            if (c != null) {
                System.out.println(c.getStartDate());
                selected = c;
                footer.show(c.getCompany().getName() + " selected.", Footer.NotificationType.INFORMATION, Footer.FADE_SUPER_QUICK);
                mainWindowView.getContextView().changeContextItem(selected);
            }
        });
    }

    public void loadSubscriptions(){
        try{
            System.out.println(subscriptions.size());
            subscriptions = FXCollections.observableArrayList(subscriptionsController.getSubscriptions());
        } catch (ModelSyncException e) {
            e.printStackTrace();
            footer.show(e.getMessage(), Footer.NotificationType.ERROR, Footer.FADE_QUICK);
        }
    }

    public void loadTable(ObservableList<Subscription> subscriptions){
        subscriptionsTable.getColumns().removeAll(subscriptionsCompanyCol, subscriptionsStartCol, subscriptionsEndCol, subscriptionsUsersCol, subscriptionsRateCol);
        subscriptionsTable.setItems(subscriptions);
        subscriptionsCompanyCol.setCellValueFactory(new PropertyValueFactory<>("companyName"));
        subscriptionsStartCol.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        subscriptionsEndCol.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        subscriptionsUsersCol.setCellValueFactory(new PropertyValueFactory<>("noOfUsers"));
        subscriptionsRateCol.setCellValueFactory(new PropertyValueFactory<>("rateName"));
        subscriptionsTable.getColumns().addAll(subscriptionsCompanyCol, subscriptionsStartCol, subscriptionsEndCol, subscriptionsUsersCol, subscriptionsRateCol);
    }
}
