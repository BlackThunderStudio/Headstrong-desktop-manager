package com.headstrongpro.desktop.view.clients;

import com.headstrongpro.desktop.controller.ClientsController;
import com.headstrongpro.desktop.core.fxControls.Footer;
import com.headstrongpro.desktop.model.entity.Client;
import com.headstrongpro.desktop.view.ContentSource;
import com.headstrongpro.desktop.view.ContentView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Clients ContentView
 */
public class ClientsContentView extends ContentView<Client> implements Initializable {

    // Table columns
    @FXML
    public TableColumn<Client, String> clientNameCol, clientEmailCol, clientPhoneCol, clientGenderCol;

    // Bottom controls
    @FXML
    public Button assignMoreButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setColumns();
        controller = new ClientsController();

        loadData();

        mainTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                footer.show(newValue.getName() + " selected.",
                        Footer.NotificationType.INFORMATION, Footer.FADE_SUPER_QUICK);
                //noinspection unchecked
                mainWindowView.getContextView().changeContextItem(newValue);
            }
        });
    }

    @FXML
    public void addNewButtonOnClick() {
        mainWindowView.changeContext(ContentSource.CLIENTS_NEW);
    }

    @FXML
    public void assignMoreOnClick() {
        displayNotImplementedError();
    }

    private void setColumns() {
        clientNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        clientEmailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        clientPhoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        clientGenderCol.setCellValueFactory(new PropertyValueFactory<>("gender"));
    }
}