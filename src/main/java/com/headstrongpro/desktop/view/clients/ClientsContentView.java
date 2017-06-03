package com.headstrongpro.desktop.view.clients;

import com.headstrongpro.desktop.controller.ClientsController;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.core.fxControls.Footer;
import com.headstrongpro.desktop.model.entity.Person;
import com.headstrongpro.desktop.view.ContentSource;
import com.headstrongpro.desktop.view.ContentView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

import static javafx.concurrent.Worker.State.*;

/**
 * Clients ContentView
 */
public class ClientsContentView extends ContentView<Person> implements Initializable {

    // Table columns
    @FXML
    public TableColumn<Person, String> clientNameCol, clientEmailCol, clientPhoneCol, clientGenderCol;

    // Bottom controls
    @FXML
    public Button assignMoreButton;

    private ClientsController controller;
    private ObservableList<Person> clients;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        clients = FXCollections.observableArrayList();

        setColumns();

        Task<Void> init = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Platform.runLater(() -> footer.show("Loading clients...", Footer.NotificationType.LOADING));
                controller = new ClientsController();
                loadClients();
                return null;
            }
        };

        init.stateProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue.equals(SUCCEEDED)) {
                loadTable(clients);
                footer.show("Clients loaded successfully!", Footer.NotificationType.COMPLETED);
            } else if (newValue.equals(FAILED) || newValue.equals(CANCELLED)) {
                footer.show("Error while loading clients!", Footer.NotificationType.ERROR, Footer.FADE_LONG);
            }
        }));

        mainTable.getSelectionModel().selectedItemProperty().addListener((o, e, c) -> {
            if (c != null) {
                if (mainWindowView.getCurrentContentSource().equals(ContentSource.CLIENTS_NEW)) {
                    mainWindowView.changeContent(ContentSource.CLIENTS);
                }
                footer.show(c.getName() + " selected.", Footer.NotificationType.INFORMATION, Footer.FADE_SUPER_QUICK);
                mainWindowView.getContextView().changeContextItem(c);
            }
        });

        Thread th = new Thread(init);
        th.setDaemon(true);
        th.start();
    }

    @FXML
    public void clientSearch() {
        try {
            loadTable(FXCollections.observableArrayList(controller.search(searchField.getText())));
        } catch (ModelSyncException e) {
            e.printStackTrace();
            //TODO: handle dis with care too~
        }
    }

    @FXML
    public void refreshButtonOnClick() {
        searchField.clear();
        //TODO protected bullshit context clearing stufff

        Task<Void> sync = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Platform.runLater(() -> footer.show("Synchronising data...", Footer.NotificationType.LOADING));
                loadClients();
                return null;
            }
        };

        sync.stateProperty().addListener((q, w, e) -> {
            if (e.equals(SUCCEEDED)) {
                loadTable(clients);
                footer.show("Clients reloaded successfully!", Footer.NotificationType.COMPLETED, Footer.FADE_NORMAL);
            } else if (e.equals(FAILED) || e.equals(CANCELLED)) {
                footer.show("Error while loading clients!", Footer.NotificationType.ERROR, Footer.FADE_LONG);
            }
        });

        Thread th = new Thread(sync);
        th.setDaemon(true);
        th.start();
    }

    @FXML
    public void addNewOnClick() {
        mainWindowView.changeContext(ContentSource.CLIENTS_NEW);
    }

    @FXML
    public void assignMoreOnClick() {
        footer.show("Feature not yet implemented, patience is advised.", Footer.NotificationType.INFORMATION);
    }

    private void setColumns() {
        clientNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        clientEmailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        clientPhoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        clientGenderCol.setCellValueFactory(new PropertyValueFactory<>("gender"));
    }

    private void loadClients() {
        try {
            clients = controller.getClients();
        } catch (ModelSyncException e) {
            e.printStackTrace();
            //TODO: pls handle with care <3
        }
    }
}