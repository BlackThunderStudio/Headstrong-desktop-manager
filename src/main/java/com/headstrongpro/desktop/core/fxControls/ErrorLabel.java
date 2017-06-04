package com.headstrongpro.desktop.core.fxControls;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;


/**
 * ErrorLabel
 */
public class ErrorLabel extends Label {

    @FXML
    protected Label label;
    @FXML
    protected ImageView image;

    public ErrorLabel() {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/layout/customComponents/errorLabel.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
            label.setVisible(false);
            image.setVisible(false);
            image.setImage(new Image("/img/error-xl.png"));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void show(String message) {
        label.setText(message);
        label.setVisible(true);
        image.setVisible(true);
    }

    public void show(String message, int milliseconds) {
        show(message);
        Task<Void> waitForTime = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Thread.sleep(milliseconds);
                Platform.runLater(() -> hide());
                return null;
            }
        };
        Thread th = new Thread(waitForTime);
        th.setDaemon(true);
        th.start();
    }

    public void hide() {
        label.setVisible(false);
        image.setVisible(false);
    }

    public boolean isActive() {
        return label.isVisible() || image.isVisible();
    }
}
