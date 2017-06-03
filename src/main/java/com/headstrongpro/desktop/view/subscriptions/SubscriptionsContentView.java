package com.headstrongpro.desktop.view.subscriptions;

import com.headstrongpro.desktop.controller.SubscriptionsController;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.core.fxControls.Footer;
import com.headstrongpro.desktop.model.Subscription;
import com.headstrongpro.desktop.view.ContentView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

import static javafx.concurrent.Worker.State.*;

/**
 * Subscriptions ContentView
 */
public class SubscriptionsContentView extends ContentView<Subscription> implements Initializable {

    // Table columns
    @FXML
    public TableColumn<Subscription, String> companyCol, startCol, endCol, usersCol, rateCol;

    private SubscriptionsController controller;
    private ObservableList<Subscription> subscriptions;
    private Subscription selected;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        controller = new SubscriptionsController();
        subscriptions = FXCollections.emptyObservableList();

        setColumns();

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

        mainTable.getSelectionModel().selectedItemProperty().addListener((o, e, c) -> {
            if (c != null) {
                System.out.println(c.getStartDate());
                selected = c;
                footer.show(c.getCompany().getName() + " selected.", Footer.NotificationType.INFORMATION, Footer.FADE_SUPER_QUICK);
                mainWindowView.getContextView().changeContextItem(selected);
            }
        });
    }

    private void loadSubscriptions() {
        try {
            System.out.println(subscriptions.size());
            subscriptions = FXCollections.observableArrayList(controller.getAll());
        } catch (ModelSyncException e) {
            e.printStackTrace();
            footer.show(e.getMessage(), Footer.NotificationType.ERROR, Footer.FADE_QUICK);
        }
    }

    private void setColumns() {
        companyCol.setCellValueFactory(new PropertyValueFactory<>("companyName"));
        startCol.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endCol.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        usersCol.setCellValueFactory(new PropertyValueFactory<>("noOfUsers"));
        rateCol.setCellValueFactory(new PropertyValueFactory<>("rateName"));
    }

}
