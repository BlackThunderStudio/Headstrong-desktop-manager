package com.headstrongpro.desktop.view;

import com.headstrongpro.desktop.core.controller.CompaniesController;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.model.entity.Company;
import com.headstrongpro.desktop.modelCollections.DBCompany;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import org.bouncycastle.math.raw.Mod;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Ondřej Soukup on 20.05.2017.
 */
public class CompaniesView implements Initializable {

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

    // Context view controls
    @FXML
    public Button companyEditButton;
    @FXML
    public Button companyDeleteButton;
    @FXML
    public TextField companyNameTextfield;
    @FXML
    public TextField companyCvrTextfield;
    @FXML
    public TextField companyStreetTextfield;
    @FXML
    public TextField companyPostalTextfield;
    @FXML
    public TextField companyCityTextfield;
    @FXML
    public TextField companyCountryTextfield;
    @FXML
    public Button companyDepartmentsButton;
    @FXML
    public Button companyClientsButton;
    @FXML
    public Button companyGroupsButton;
    @FXML
    public Button companySubscriptionsButton;

    // "New" context view controls
    @FXML
    public TextField newCompanyNameTextfield;
    @FXML
    public TextField newCompanyCvrTextfield;
    @FXML
    public TextField newCompanyStreetTextfield;
    @FXML
    public TextField newCompanyPostalTextfield;
    @FXML
    public TextField newCompanyCityTextfield;
    @FXML
    public TextField newCompanyCountryTextfield;
    @FXML
    public Button companySaveButton;
    @FXML
    public Button companyCancelButton;

    CompaniesController companiesController;

    @SuppressWarnings("unchecked")
    private void loadTable(ObservableList<Company> companies) throws ModelSyncException{
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
        try {
            companiesController = new CompaniesController();
            loadTable(companiesController.getCompanies());
        }catch (ModelSyncException e){
            e.printStackTrace();
            //TODO: to be handled
        }
    }

    public void companiesTableOnMouseClicked(){

    }

    public void companySearch() throws ModelSyncException{
        loadTable(companiesController.search(searchCompaniesTextfield.getText()));
    }
}
