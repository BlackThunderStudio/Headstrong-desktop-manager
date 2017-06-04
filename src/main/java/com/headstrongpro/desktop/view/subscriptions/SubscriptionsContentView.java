package com.headstrongpro.desktop.view.subscriptions;

import com.headstrongpro.desktop.controller.SubscriptionsController;
import com.headstrongpro.desktop.core.fxControls.Footer;
import com.headstrongpro.desktop.model.Subscription;
import com.headstrongpro.desktop.view.ContentView;
import javafx.application.Platform;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setColumns();

        Task<Void> init = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Platform.runLater(() -> footer.show("Loading subscriptions...", Footer.NotificationType.LOADING));
                controller = new SubscriptionsController();
                loadData();
                return null;
            }
        };

        init.stateProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue.equals(SUCCEEDED)) {
                loadTable(data);
                footer.show("Subscriptions loaded successfully!", Footer.NotificationType.COMPLETED);
            } else if (newValue.equals(FAILED) || newValue.equals(CANCELLED)) {
                footer.show("Error while loading subscriptions!", Footer.NotificationType.ERROR, Footer.FADE_LONG);
            }
        }));

        mainTable.getSelectionModel().selectedItemProperty().addListener((o, e, c) -> {
            if (c != null) {
                System.out.println(c.getStartDate());
                selected = c;
                footer.show(c.getCompany().getName() + " selected.", Footer.NotificationType.INFORMATION, Footer.FADE_SUPER_QUICK);
                mainWindowView.getContextView().changeContextItem(selected);
            }
        });

        Thread th = new Thread(init);
        th.setDaemon(true);
        th.start();
    }

    private void setColumns() {
        companyCol.setCellValueFactory(new PropertyValueFactory<>("companyName"));
        startCol.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endCol.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        usersCol.setCellValueFactory(new PropertyValueFactory<>("noOfUsers"));
        rateCol.setCellValueFactory(new PropertyValueFactory<>("rateName"));
    }

}
