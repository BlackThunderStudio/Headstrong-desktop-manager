package com.headstrongpro.desktop.view.companies;

import com.headstrongpro.desktop.controller.CompaniesController;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.core.fxControls.Footer;
import com.headstrongpro.desktop.model.entity.Company;
import com.headstrongpro.desktop.view.ContentSource;
import com.headstrongpro.desktop.view.ContentView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

import static javafx.concurrent.Worker.State.*;

/**
 * Companies ContentView
 */
public class CompaniesContentView extends ContentView<Company> implements Initializable {

    // Table columns
    @FXML
    public TableColumn<Company, String> nameCol, cvrCol, streetCol, postalCol, cityCol, countryCol;

    private CompaniesController controller; // Data controller
    private ObservableList<Company> companies = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setColumns();

        Task<Void> init = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Platform.runLater(() -> footer.show("Loading companies...", Footer.NotificationType.LOADING));
                controller = new CompaniesController();
                loadCompanies();
                return null;
            }
        };

        init.stateProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue.equals(SUCCEEDED)) {
                loadTable(companies);
                footer.show("Companies loaded successfully!", Footer.NotificationType.COMPLETED);
            } else if (newValue.equals(FAILED) || newValue.equals(CANCELLED)) {
                footer.show("Error while loading companies!", Footer.NotificationType.ERROR, Footer.FADE_LONG);
            }
        }));

        mainTable.getSelectionModel().selectedItemProperty().addListener((o, e, c) -> {
            if (c != null) {
                footer.show(c.getName() + " selected.", Footer.NotificationType.INFORMATION, Footer.FADE_SUPER_QUICK);
                mainWindowView.getContextView().changeContextItem(c);
            }
        });

        Thread th = new Thread(init);
        th.setDaemon(true);
        th.start();
    }

    @FXML
    public void handleSearch() {
        try {
            loadTable(controller.search(searchField.getText()));
        } catch (ModelSyncException e2) {
            e2.printStackTrace();
            footer.show(e2.getMessage(), Footer.NotificationType.ERROR);
        }
    }

    @FXML
    public void refreshButtonOnClick() {
        searchField.clear();
        Task<Void> sync = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Platform.runLater(() -> footer.show("Synchronising data...", Footer.NotificationType.LOADING));
                loadCompanies();
                return null;
            }
        };

        sync.stateProperty().addListener((q, w, e) -> {
            if (e.equals(SUCCEEDED)) {
                loadTable(companies);
                footer.show("Companies reloaded successfully!", Footer.NotificationType.COMPLETED, Footer.FADE_NORMAL);
            } else if (e.equals(FAILED) || e.equals(CANCELLED)) {
                footer.show("Error while loading companies!", Footer.NotificationType.ERROR, Footer.FADE_LONG);
            }
        });

        Thread th = new Thread(sync);
        th.setDaemon(true);
        th.start();
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

    private void loadCompanies() {
        try {
            companies = FXCollections.emptyObservableList();
            companies = FXCollections.observableArrayList(controller.getCompanies());
        } catch (ModelSyncException e) {
            e.printStackTrace();
            footer.show(e.getMessage(), Footer.NotificationType.ERROR);
        }
    }
}
