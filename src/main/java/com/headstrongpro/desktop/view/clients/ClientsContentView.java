package com.headstrongpro.desktop.view.clients;

import com.headstrongpro.desktop.core.controller.ClientsController;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.core.fxControls.Footer;
import com.headstrongpro.desktop.model.entity.Client;
import com.headstrongpro.desktop.model.entity.Person;
import com.headstrongpro.desktop.view.ContentView;
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

import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;

import static javafx.concurrent.Worker.State.CANCELLED;
import static javafx.concurrent.Worker.State.FAILED;
import static javafx.concurrent.Worker.State.SUCCEEDED;

/**
 * Created by Ondřej Soukup on 23.05.2017.
 */
public class ClientsContentView extends ContentView implements Initializable {

    @FXML
    public TextField searchClientsTextfield;
    @FXML
    public TableView<Person> clientsTable;

    @FXML
    public TableColumn<Person, Integer> clientIdCol, clientCompanyCol;
    @FXML
    public TableColumn<Person, String> clientNameCol, clientEmailCol, clientPhoneCol, clientGenderCol, clientLoginCol, clientPassCol, clientDateCol;
    @FXML
    public Button newClientButton;
    @FXML
    public Text clientsHeader;
    @FXML
    public Button assignMoreButton;

    private ClientsController clientsController;
    private ObservableList<Person> clients;

    private void loadClients(){
        try{
            clients = clientsController.getClients();
        } catch (ModelSyncException e){
            e.printStackTrace();
            //TODO: pls handle with care <3
        }
    }

    private void loadTable(ObservableList<Person> clients){
        clientsTable.getColumns().removeAll(clientIdCol, clientNameCol, clientEmailCol, clientPhoneCol, clientGenderCol, clientDateCol, clientCompanyCol);
        clientsTable.setItems(clients);
        clientIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        clientNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        clientEmailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        clientPhoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        clientGenderCol.setCellValueFactory(new PropertyValueFactory<>("gender"));
        //clientLoginCol.setCellValueFactory(new PropertyValueFactory<>("login"));
        //clientPassCol.setCellValueFactory(new PropertyValueFactory<>("pass"));
        clientDateCol.setCellValueFactory(new PropertyValueFactory<>("registrationDate"));
        clientCompanyCol.setCellValueFactory(new PropertyValueFactory<>("companyId"));
        clientsTable.getColumns().addAll(clientIdCol, clientNameCol, clientEmailCol, clientPhoneCol, clientGenderCol, clientDateCol, clientCompanyCol);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        clients = FXCollections.observableArrayList();
        Task<Void> init = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                footer.show("Loading clients...", Footer.NotificationType.LOADING);
                clientsController = new ClientsController();
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

        clientsTable.getSelectionModel().selectedItemProperty().addListener((o, e, c) -> {
            if (c != null) {
                footer.show(c.getName() + " selected.", Footer.NotificationType.INFORMATION, Footer.FADE_SUPER_QUICK);
                //TODO: context bar
                mainWindowView.getContextView().changeContextItem(c);
            }
        });

        Thread th = new Thread(init);
        th.setDaemon(true);
        th.start();
    }

    public void clientSearch(){
        try{

            System.out.println("before" + clients.size());
            loadTable(clientsController.search(searchClientsTextfield.getText()));
        } catch (ModelSyncException e) {
            e.printStackTrace();
            //TODO: handle dis with care too~
        }
    }
}
