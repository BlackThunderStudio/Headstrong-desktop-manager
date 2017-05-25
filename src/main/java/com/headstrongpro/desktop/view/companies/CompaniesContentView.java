package com.headstrongpro.desktop.view.companies;

import com.headstrongpro.desktop.core.controller.CompaniesController;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.core.exception.SyncException;
import com.headstrongpro.desktop.core.fxControls.Footer;
import com.headstrongpro.desktop.model.entity.Company;
import com.headstrongpro.desktop.view.ContentView;
import javafx.beans.value.ChangeListener;
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

import java.net.URL;
import java.util.ResourceBundle;

import static javafx.concurrent.Worker.State.*;

/**
 * Companies ContentView
 */
public class CompaniesContentView extends ContentView implements Initializable {

    // Content view controls
    @FXML
    public TextField searchCompaniesTextfield;
    @FXML
    public TableView<Company> companiesTable;
    @FXML
    public TableColumn companyIdCol;
    @FXML
    public TableColumn companyNameCol;
    @FXML
    public TableColumn companyCvrCol;
    @FXML
    public TableColumn companyStreetCol;
    @FXML
    public TableColumn companyPostalCol;
    @FXML
    public TableColumn companyCityCol;
    @FXML
    public TableColumn companyCountryCol;
    @FXML
    public Button newCompanyButton;
    @FXML
    public Text companiesHeader;
    @FXML
    public Footer footer;

    private CompaniesController companiesController;
    private ObservableList<Company> companies;

    @SuppressWarnings("unchecked")
    private void loadTable(ObservableList<Company> companies) {
        companiesTable.getColumns().removeAll(companyIdCol, companyNameCol, companyCvrCol, companyStreetCol, companyPostalCol, companyCityCol, companyCountryCol);
        companiesTable.setItems(companies);
        companyIdCol.setMinWidth(20);
        companyNameCol.setMinWidth(150);
        companyCvrCol.setMinWidth(65);
        companyStreetCol.setMinWidth(120);
        companyPostalCol.setMinWidth(50);
        companyCityCol.setMinWidth(20);
        companyCountryCol.setMinWidth(65);
        companyIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        companyNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        companyCvrCol.setCellValueFactory(new PropertyValueFactory<>("cvr"));
        companyStreetCol.setCellValueFactory(new PropertyValueFactory<>("street"));
        companyPostalCol.setCellValueFactory(new PropertyValueFactory<>("postal"));
        companyCityCol.setCellValueFactory(new PropertyValueFactory<>("city"));
        companyCountryCol.setCellValueFactory(new PropertyValueFactory<>("country"));
        companiesTable.getColumns().addAll(companyIdCol, companyNameCol, companyCvrCol, companyStreetCol, companyPostalCol, companyCityCol, companyCountryCol);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        companies = FXCollections.observableArrayList();
        Task<Void> init = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                footer.show("Loading companies...", Footer.NotificationType.LOADING);
                companiesController = new CompaniesController();
                loadCompanies();
                return null;
            }
        };

        init.stateProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue.equals(SUCCEEDED)) {
                loadTable(companies);
                footer.show("Companies loaded successfully!", Footer.NotificationType.COMPLETED);
            } else if (newValue.equals(FAILED) || newValue.equals(CANCELLED)) {
                footer.show("Error while loading comapnie!", Footer.NotificationType.ERROR, Footer.FADE_LONG);
            }
        }));

        companiesTable.getSelectionModel().selectedItemProperty().addListener((o, e, c) -> {
            if(c != null){
                footer.show(c.getName() + " selected.", Footer.NotificationType.INFORMATION, Footer.FADE_SUPER_QUICK);
                contextView.changeContextItem(c);
            }
        });

        Thread th = new Thread(init);
        th.setDaemon(true);
        th.start();
    }

    private void loadCompanies() {
        try {
            companies = companiesController.getCompanies();
        } catch (ModelSyncException e) {
            e.printStackTrace();
            //TODO: handle error
        }
    }

    public void companySearch() {
        try {
            loadTable(companiesController.search(searchCompaniesTextfield.getText()));
        } catch (SyncException e) {
            Task<Void> sync = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    footer.show("Synchronising data...", Footer.NotificationType.LOADING);
                    loadCompanies();
                    return null;
                }
            };

            sync.stateProperty().addListener((q,w,r) -> {
                if(r.equals(SUCCEEDED)){
                    footer.hide();
                }
            });

            new Thread(sync).start();
        } catch (ModelSyncException e2){
            e2.printStackTrace();
            //TODO: handle the error
        }
    }
}
