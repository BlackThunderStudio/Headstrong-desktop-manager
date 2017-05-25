package com.headstrongpro.desktop.core.fxControls;

import com.jfoenix.controls.JFXSpinner;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

/**
 * Created by rajmu on 17.05.25.
 */
public class Footer extends AnchorPane {

    @FXML
    protected Label label;
    @FXML
    protected JFXSpinner spinner;
    @FXML
    protected ImageView image;

    public static final int FADE_MANUAL = -1;
    public static final int FADE_QUICK = 2000;
    public static final int FADE_NORMAL = 4000;
    public static final int FADE_LONG = 8000;

    public Footer(){
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/layout/customControls/footer.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try{
            loader.load();
            label.setVisible(false);
            image.setVisible(false);
            spinner.setVisible(false);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void show(String message, NotificationType type, int milliseconds){
        if(type.equals(NotificationType.LOADING)){
            image.setVisible(false);
            spinner.setVisible(true);
            label.setText(message);
            label.setVisible(true);
        } else if(type.equals(NotificationType.COMPLETED) ||
                type.equals(NotificationType.ERROR) ||
                type.equals(NotificationType.WARNING)) {
            spinner.setVisible(false);
            image.setImage(new Image(type.getPath()));
            image.setVisible(true);
            label.setText(message);
            label.setVisible(true);
            if(milliseconds != -1){
                Task<Void> waitForTime = new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        Thread.sleep(milliseconds);
                        Platform.runLater(Footer.this::hide);
                        return null;
                    }
                };
                Thread th = new Thread(waitForTime);
                th.setDaemon(true);
                th.start();
            }
        }
    }

    public void hide(){
        image.setVisible(false);
        spinner.setVisible(false);
        label.setVisible(false);
    }


    public enum NotificationType{
        LOADING,
        ERROR("/img/icons/err-icon.png"),
        COMPLETED("/img/icons/check-icon.png"),
        WARNING("/img/icons/warning-icon.png");

        private String path;

        NotificationType(){}
        NotificationType(String path){
            this.path = path;
        }

        public String getPath(){
            return path;
        }

    }
}
