package com.headstrongpro.desktop.core;

import com.jfoenix.controls.JFXProgressBar;
import javafx.application.Platform;
import javafx.concurrent.Task;

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
}
