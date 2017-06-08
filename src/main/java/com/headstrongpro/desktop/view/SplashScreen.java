package com.headstrongpro.desktop.view;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by rajmu on 17.04.06.
 */
public class SplashScreen implements Initializable{

    @FXML
    public AnchorPane layout;
    
    @FXML
    public ProgressBar progressBar;

    private static Style style;

    private static int durationMillis;

    public void viewSplashScreen(int durationMillis, Style styleOpen, Style styleClose) {
        Stage window = new Stage();
        SplashScreen.durationMillis = durationMillis;

        style = styleClose;

        window.initModality(Modality.APPLICATION_MODAL);
        window.initStyle(StageStyle.TRANSPARENT);
        window.setMinWidth(640);
        window.setMinHeight(360);
        window.setMaxWidth(700);
        window.setMaxHeight(400);
        Scene scene = null;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/layout/splashScreen.fxml"));
        try {
            scene = new Scene(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        scene.setFill(null);
        window.setScene(scene);
        window.setResizable(false);
        window.setAlwaysOnTop(true);

        Task waitForTime = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Thread.sleep(durationMillis);

                Task fadeOut = new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        fadeOut();
                        return null;
                    }
                };

                Thread th = new Thread(fadeOut);
                th.setDaemon(true);
                th.start();

                if (styleClose == Style.FADE) Thread.sleep(1000);

                Platform.runLater(window::close);
                return null;
            }
        };

        Thread wait = new Thread(waitForTime);
        wait.setDaemon(true);
        wait.start();
        if (styleOpen == Style.FADE) {
            FadeTransition ft = new FadeTransition(Duration.millis(300), layout);
            ft.setFromValue(0.0);
            ft.setToValue(1.0);
            ft.play();
        }

        window.showAndWait();
    }

    private void fadeOut() {
        if (style == Style.FADE) {
            FadeTransition fadeTransition = new FadeTransition(Duration.millis(300), layout);
            fadeTransition.setFromValue(1.0);
            fadeTransition.setToValue(0.0);
            fadeTransition.play();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        final double[] timePassed = {0.0};
        Timer timer = new Timer();
        TimerTask updateProgress = new TimerTask() {
            @Override
            public void run() {
                if (timePassed[0] > durationMillis) timer.cancel();
                else timePassed[0] += 100;
                progressBar.setProgress(timePassed[0] / durationMillis);
            }
        };
        timer.scheduleAtFixedRate(updateProgress, 0, 150);
    }

    public enum Style {
        FADE,
        SOLID
    }


    //STATIC IMPLEMENTATION

    private static ImageView imgView;
    private static ProgressBar progress;

    public static void view(int durationMillis, Style open, Style close){
        Stage window = new Stage();

        style = close;

        window.initModality(Modality.APPLICATION_MODAL);
        window.initStyle(StageStyle.TRANSPARENT);
        window.setMinWidth(540);
        window.setMinHeight(360);
        window.setMaxWidth(540);
        window.setMaxHeight(360);

        ImageView img = new ImageView();
        img.setImage(new Image("/img/splash_screen.jpg"));
        img.setFitHeight(360.0);
        img.setFitWidth(540.0);
        img.setPickOnBounds(true);
        img.setPreserveRatio(true);
        imgView = img;

        ProgressBar progressBar = new ProgressBar();
        progressBar.setProgress(0.0);
        progressBar.setPrefHeight(8.0);
        progressBar.setPrefWidth(200.0);
        progressBar.getStylesheets().add("/style/main.css");
        progress = progressBar;

        AnchorPane layout = new AnchorPane();
        layout.setPrefHeight(360);
        layout.setPrefWidth(540);
        layout.getChildren().addAll(img, progressBar);
        layout.getChildren().get(1).setLayoutX(85.0);
        layout.getChildren().get(1).setLayoutY(277.0);
        layout.setBackground(Background.EMPTY);

        Scene scene = new Scene(layout);
        scene.setFill(null);
        window.setScene(scene);
        window.setResizable(false);
        window.setAlwaysOnTop(true);

        Task waitForTime = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Thread.sleep(durationMillis);

                Task fadeOut = new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        fadeOutStatic();
                        return null;
                    }
                };

                Thread th = new Thread(fadeOut);
                th.setDaemon(true);
                th.start();

                if (close == Style.FADE) Thread.sleep(1000);

                Platform.runLater(window::close);
                return null;
            }
        };

        new Timeline(new KeyFrame(Duration.millis(durationMillis), new KeyValue(progressBar.progressProperty(), 1))).play();
        Thread wait = new Thread(waitForTime);
        wait.setDaemon(true);
        wait.start();
        if (open == Style.FADE) {
            FadeTransition ft = new FadeTransition(Duration.millis(1000), img);
            ft.setFromValue(0.0);
            ft.setToValue(1.0);
            ft.play();
        }

        window.showAndWait();
    }

    private static void fadeOutStatic(){
        if (style == Style.FADE) {
            FadeTransition fadeTransition = new FadeTransition(Duration.millis(1000), imgView);
            FadeTransition progressFade = new FadeTransition(Duration.millis(1000), progress);
            fadeTransition.setFromValue(1.0);
            fadeTransition.setToValue(0.0);
            progressFade.setFromValue(1.0);
            progressFade.setToValue(0.0);
            fadeTransition.play();
            progressFade.play();
        }
    }
}