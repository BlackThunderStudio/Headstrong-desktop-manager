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
 * Abstract base class for content views
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

    @FXML
    public TableView<T> mainTable; // Main table

    protected MainWindowView mainWindowView; // MainWindow parent controller

    protected IContentController<T> controller; // Data controller

    protected T selected; // Currently selected item

    private ObservableList<T> data = FXCollections.observableArrayList(); // Data collections for the main table

    /**
     * Loads data via content controller and populates the main table
     */
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
                try {
                    mainTable.getSelectionModel().selectFirst();
                } catch (NullPointerException ignored) {
                }
                footer.show("Entries loaded successfully!", Footer.NotificationType.COMPLETED, Footer.FADE_NORMAL);
            } else if (newValue.equals(FAILED) || newValue.equals(CANCELLED)) {
                footer.show("Error while loading entries!", Footer.NotificationType.ERROR, Footer.FADE_LONG);
            }
        }));

        Thread th = new Thread(initData);
        th.setDaemon(true);
        th.start();
    }

    /**
     * Filters the data collection by a keyword entered into the search field
     */
    @FXML
    public void handleSearch() {
        try {
            loadTable(controller.searchByPhrase(searchField.getText()));
        } catch (NullPointerException e) {
            e.printStackTrace();
            footer.show(e.getMessage(), Footer.NotificationType.ERROR);
        }
    }

    /**
     * Clears the search field and reloads the data collection
     */
    @FXML
    public void handleRefresh() {
        searchField.clear();
        loadData();
    }

    /**
     * Displays an error for not yet implemented features
     */
    protected void displayNotImplementedError() {
        mainWindowView.getContentView().footer.show("Feature not yet implemented, patience is advised.", Footer.NotificationType.INFORMATION);
    }

    /**
     * Populates the main table with data
     *
     * @param items data collection
     */
    private void loadTable(ObservableList<T> items) {
        mainTable.setItems(items);
    }

    public Footer getFooter() {
        return footer;
    }

    public void setMainWindowView(MainWindowView mainWindowView) {
        this.mainWindowView = mainWindowView;
    }

}
