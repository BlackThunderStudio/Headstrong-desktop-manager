package com.headstrongpro.desktop.core.fxControls;

import com.jfoenix.controls.JFXSpinner;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.io.IOException;

/**
 * LoadingBar
 */
public class LoadingBar extends HBox {

    @FXML
    public Label label;
    @FXML
    public JFXSpinner spinner;
    @FXML
    private AnchorPane myAnchorPane;

    public LoadingBar() {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/layout/customControls/loadingBar.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
            //renders element invisible in the FXML builder
            label.setVisible(false);
            spinner.setVisible(false);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void show() {
        spinner.setVisible(true);
    }

    public void show(String message) {
        label.setText(message);
        label.setVisible(true);
        spinner.setVisible(true);
    }

    public void hide() {
        label.setVisible(false);
        spinner.setVisible(false);
    }
}
