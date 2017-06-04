package com.headstrongpro.desktop.view;

import com.headstrongpro.desktop.controller.IContentController;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.core.fxControls.Footer;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import static javafx.concurrent.Worker.State.*;

/**
 * ContentView
 */
public abstract class ContentView<T> {

    // Common controls
    @FXML
    public TextField searchField;
    @FXML
    public Text headerText;
    @FXML
    public Button addNewButton, refreshButton;

    @FXML
    public Footer footer; // Notifications display area

    // Main table
    @FXML
    public TableView<T> mainTable;

    // MainWindow parent controller
    protected MainWindowView mainWindowView;

    protected IContentController<T> controller;

    protected T selected;

    protected ObservableList<T> data = FXCollections.observableArrayList();

    public void setMainWindowView(MainWindowView mainWindowView) {
        this.mainWindowView = mainWindowView;
    }

    protected void loadTable(ObservableList<T> items) {
        mainTable.setItems(items);
    }

    protected void loadData() {
        Task<Void> initData = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Platform.runLater(() -> footer.show("Loading entries...", Footer.NotificationType.LOADING));
                try {
                    data = FXCollections.emptyObservableList();
                    data = FXCollections.observableArrayList(controller.getAll());
                } catch (ModelSyncException e) {
                    e.printStackTrace();
                    footer.show(e.getMessage(), Footer.NotificationType.ERROR, Footer.FADE_QUICK);
                }
                return null;
            }
        };

        initData.stateProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue.equals(SUCCEEDED)) {
                loadTable(data);
//                try {
//                    mainTable.getSelectionModel().selectFirst();
//                } catch (NullPointerException ignored) {
//                }
                footer.show("Entries loaded successfully!", Footer.NotificationType.COMPLETED, Footer.FADE_NORMAL);
            } else if (newValue.equals(FAILED) || newValue.equals(CANCELLED)) {
                footer.show("Error while loading entries!", Footer.NotificationType.ERROR, Footer.FADE_LONG);
            }
        }));

        Thread th = new Thread(initData);
        th.setDaemon(true);
        th.start();
    }

    public Footer getFooter() {
        return footer;
    }

    @FXML
    public void handleSearch() {
        try {
            loadTable(controller.searchByPhrase(searchField.getText()));
        } catch (NullPointerException e) {
            e.printStackTrace();
            footer.show(e.getMessage(), Footer.NotificationType.ERROR);
        }
    }

    @FXML
    public void handleRefresh() {
        searchField.clear();
        loadData();
    }

    protected void displayNotImplementedError() {
        mainWindowView.getContentView().footer.show("Feature not yet implemented, patience is advised.", Footer.NotificationType.INFORMATION);
    }
}
