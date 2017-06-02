package com.headstrongpro.desktop.core;

import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXSpinner;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Label;

import java.time.format.DateTimeFormatter;
import java.util.function.Predicate;

/**
 * Created by rajmu on 17.04.06.
 */
public class Utils {

    public static class WaitingBar{
        private JFXProgressBar bar;
        private boolean isAlive;

        public WaitingBar(JFXProgressBar bar){
            this.bar = bar;
            isAlive = false;
        }
        public void init(){
            bar.setVisible(true);
            isAlive = true;

            Task<Void> waitForTrigger = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    while (isAlive) {
                        Thread.sleep(10);
                    }
                    Platform.runLater(() -> bar.setVisible(false));
                    return null;
                }
            };
            Thread wait = new Thread(waitForTrigger);
            wait.start();
        }
        
        public void close(){
            isAlive = false;
        }
    }

    public static class WaitingSpinner{
        private JFXSpinner spinner;
        private Label label;
        private boolean isAlive = false;

        public WaitingSpinner(JFXSpinner spinner, Label label){
            this.spinner = spinner;
            this.label = label;
        }

        public void init(){
            spinner.setVisible(true);
            isAlive = true;
            Task<Void> waitForTrigger = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    while (isAlive){
                        Thread.sleep(10);
                    }
                    Platform.runLater(() -> spinner.setVisible(false));
                    return null;
                }
            };
            Thread th = new Thread(waitForTrigger);
            th.start();
        }

        public void init(String message){
            spinner.setVisible(true);
            label.setVisible(true);
            label.setText(message);
            isAlive = true;
            Task<Void> waitForTrigger = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    while (isAlive){
                        Thread.sleep(10);
                    }
                    Platform.runLater(() -> {
                        spinner.setVisible(false);
                        label.setVisible(false);
                    });
                    return null;
                }
            };
            Thread th = new Thread(waitForTrigger);
            th.start();
        }

        public void close(){
            isAlive = false;
        }
    }

    public enum FormatterType {
        DATE, DATETIME, TIME
    }

    public static DateTimeFormatter dateFormatter(FormatterType type) {
        String pattern = "dd-MM-yyyy";
        switch (type) {
            case DATE:
                pattern = "yyyy-MM-dd";
                break;
            case DATETIME:
                pattern = "dd-MM-yyyy hh:mm";
                break;
            case TIME:
                pattern = "hh:mm";
        }
        return DateTimeFormatter.ofPattern(pattern);
    }
}
