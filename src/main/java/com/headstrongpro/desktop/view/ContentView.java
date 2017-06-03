package com.headstrongpro.desktop.view;

import com.headstrongpro.desktop.core.fxControls.Footer;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

/**
 * ContentView
 */
public abstract class ContentView<T> {

    // Common top controls
    @FXML
    public TextField searchField;
    @FXML
    public Text headerText;
    @FXML
    public Button addNewButton;
    @FXML
    public Button refreshButton;
    @FXML
    public Footer footer;

    // Main table
    @FXML
    public TableView<T> mainTable;

    // MainWindow parent controller
    protected MainWindowView mainWindowView;

    public void setMainWindowView(MainWindowView mainWindowView) {
        this.mainWindowView = mainWindowView;
    }

    protected void loadTable(ObservableList<T> items) {
        mainTable.setItems(items);
    }

    public Footer getFooter() {
        return footer;
    }
}
