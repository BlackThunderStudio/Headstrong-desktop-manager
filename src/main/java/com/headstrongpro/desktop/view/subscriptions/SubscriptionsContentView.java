package com.headstrongpro.desktop.view.subscriptions;

import com.headstrongpro.desktop.controller.SubscriptionsController;
import com.headstrongpro.desktop.core.fxControls.Footer;
import com.headstrongpro.desktop.model.Subscription;
import com.headstrongpro.desktop.view.ContentView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Subscriptions Content View
 */
public class SubscriptionsContentView extends ContentView<Subscription> implements Initializable {

    // Table columns
    @FXML
    public TableColumn<Subscription, String> companyCol, startCol, endCol, usersCol, rateCol;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setColumns();
        controller = new SubscriptionsController();

        loadData();

        mainTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                footer.show(newValue.getCompany().getName() + " selected.",
                        Footer.NotificationType.INFORMATION, Footer.FADE_SUPER_QUICK);
                //noinspection unchecked
                mainWindowView.getContextView().changeContextItem(newValue);
            }
        });
    }

    private void setColumns() {
        companyCol.setCellValueFactory(new PropertyValueFactory<>("company"));
        startCol.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endCol.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        usersCol.setCellValueFactory(new PropertyValueFactory<>("noOfUsers"));
        rateCol.setCellValueFactory(new PropertyValueFactory<>("rate"));
    }

}
