package com.headstrongpro.desktop.view.companies;

import com.headstrongpro.desktop.core.Utils;
import com.headstrongpro.desktop.core.controller.CompaniesController;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.model.entity.Company;
import com.jfoenix.controls.JFXSpinner;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

import static javafx.concurrent.Worker.State.*;

/**
 * Created by Ondřej Soukup on 20.05.2017.
 */
public class CompaniesContentView implements Initializable {

    // Content view controls
    @FXML
    public TextField searchCompaniesTextfield;
    @FXML
    public TableView companiesTable;
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
    public JFXSpinner loadingSpinner;
    @FXML
    public Label loadingLabel;

    private CompaniesController companiesController;
    private ObservableList<Company> companies;

    @SuppressWarnings("unchecked")
    private void loadTable(ObservableList<Company> companies){
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
        loadingLabel.setVisible(false);
        loadingSpinner.setVisible(false);
        Utils.WaitingSpinner waitingSpinner = new Utils.WaitingSpinner(loadingSpinner, loadingLabel);
        Task<Void> init = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                waitingSpinner.init("Loading companies...");
                companiesController = new CompaniesController();
                loadCompanies();
                return null;
            }
        };

        init.stateProperty().addListener(((observable, oldValue, newValue) -> {
            if(newValue.equals(SUCCEEDED)){
                waitingSpinner.close();
                loadTable(companies);
            }
        }));

        Thread th = new Thread(init);
        th.start();
    }

    private void loadCompanies(){
        try {
            companies = companiesController.getCompanies();
        } catch (ModelSyncException e) {
            e.printStackTrace();
            //TODO: handle error
        }
    }

    public void companiesTableOnMouseClicked() throws ModelSyncException{
        //companyNameTextfield.setText(String.valueOf(companiesController.getCompanyById(((Company)companiesTable.getSelectionModel().getSelectedItem()).getId()).getId()));
    }

    public void companySearch() throws ModelSyncException{
        loadTable(companiesController.search(searchCompaniesTextfield.getText()));
    }
}
