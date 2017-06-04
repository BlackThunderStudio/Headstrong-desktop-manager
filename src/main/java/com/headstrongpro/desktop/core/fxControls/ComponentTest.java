package com.headstrongpro.desktop.core.fxControls;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by Rajmund Staniek on 25-May-17.
 */
public class ComponentTest extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/layout/test.fxml"));
        Parent root = loader.load();
        /*primaryStage.setWidth(1400);
        primaryStage.setHeight(750);*/
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
