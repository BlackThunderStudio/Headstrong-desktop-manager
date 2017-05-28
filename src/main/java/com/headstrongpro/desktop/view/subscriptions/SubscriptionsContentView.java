package com.headstrongpro.desktop.view.subscriptions;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Ondřej Soukup on 23.05.2017.
 */
public class SubscriptionsContentView implements Initializable {

    @FXML
    public Text subscriptionsHeader;
    @FXML
    public TextField searchSubscriptionsTextfield;
    @FXML
    public TableView subscriptionsTable;
    @FXML
    public Button newSubscriptionButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
