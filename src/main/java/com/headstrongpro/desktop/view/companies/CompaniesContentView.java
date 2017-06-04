package com.headstrongpro.desktop.view.companies;

import com.headstrongpro.desktop.controller.CompaniesController;
import com.headstrongpro.desktop.core.fxControls.Footer;
import com.headstrongpro.desktop.model.entity.Company;
import com.headstrongpro.desktop.view.ContentSource;
import com.headstrongpro.desktop.view.ContentView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Companies ContentView
 */
public class CompaniesContentView extends ContentView<Company> implements Initializable {

    // Table columns
    @FXML
    public TableColumn<Company, String> nameCol, cvrCol, streetCol, postalCol, cityCol, countryCol;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setColumns();
        controller = new CompaniesController();

        loadData();

        mainTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                footer.show(newValue.getName() + " selected.", Footer.NotificationType.INFORMATION, Footer.FADE_SUPER_QUICK);
                //noinspection unchecked
                mainWindowView.getContextView().changeContextItem(newValue);
            }
        });
    }

    @FXML
    public void addNewButtonOnClick() {
        mainWindowView.changeContext(ContentSource.COMPANIES_NEW);
    }

    private void setColumns() {
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        cvrCol.setCellValueFactory(new PropertyValueFactory<>("cvr"));
        streetCol.setCellValueFactory(new PropertyValueFactory<>("street"));
        postalCol.setCellValueFactory(new PropertyValueFactory<>("postal"));
        cityCol.setCellValueFactory(new PropertyValueFactory<>("city"));
        countryCol.setCellValueFactory(new PropertyValueFactory<>("country"));
    }

}
