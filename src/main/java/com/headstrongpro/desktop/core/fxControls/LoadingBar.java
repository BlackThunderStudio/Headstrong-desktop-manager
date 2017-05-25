package com.headstrongpro.desktop.core.fxControls;

import com.jfoenix.controls.JFXSpinner;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import org.omg.PortableInterceptor.ServerRequestInfo;

import java.awt.*;
import java.io.IOException;

/**
 * Created by Rajmund Staniek on 25-May-17.
 */
public class LoadingBar extends HBox {

    @FXML
    protected Label label;
    @FXML
    protected JFXSpinner spinner;

    public LoadingBar() {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/com/headstrongpro/desktop/core/fxControls/loadingBar.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
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
