package com.headstrongpro.desktop.view;

import com.headstrongpro.desktop.core.fxControls.Footer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

/**
 * ContentView
 */
public abstract class ContentView {

    // Common topControls
    @FXML
    public Text headerText;
    @FXML
    public Button addNewButton;
    @FXML
    public Button refreshButton;
    @FXML
    public Footer footer;

    // MainWindow parent controller
    protected MainWindowView mainWindowView;

    public void setMainWindowView(MainWindowView mainWindowView) {
        this.mainWindowView = mainWindowView;
    }

    public Footer getFooter() {
        return footer;
    }
}
