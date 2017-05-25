package com.headstrongpro.desktop.core.fxControls;

import com.jfoenix.controls.JFXSpinner;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

/**
 * Created by Rajmund Staniek on 25-May-17.
 */
public class LoadingBar extends AnchorPane {

    @FXML
    private AnchorPane myAnchorPane;
    @FXML
    public Label label;
    @FXML
    public JFXSpinner spinner;

    public LoadingBar() {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/layout/loadingBar.fxml"));
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
