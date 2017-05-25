package com.headstrongpro.desktop;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

/**
 * Headstrong - Desktop Manager
 *
 * @author Marek Cervinka
 * @author Andrei Katona
 * @author Jakub Sliva
 * @author Ondrej Soukup
 * @author Rajmund Staniek
 * @version 1.0
 */
public class Main extends Application {
    private Stage window;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
//        SplashScreen.viewSplashScreen(3000, SplashScreen.Style.FADE, SplashScreen.Style.FADE);
        window = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("/layout/mainWindow.fxml"));
        primaryStage.setTitle("Headstrong Company Manager");
        // set minimum window size
        primaryStage.setMinHeight(700);
        primaryStage.setMinWidth(800);

        primaryStage.setOnCloseRequest(e -> {
            e.consume();
            closeProgram();
        });
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    private void closeProgram() {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setHeaderText("Are you sure you want to quit?");
        a.setContentText("There might be some unsaved changes");
        Button exitButton = (Button) a.getDialogPane().lookupButton(ButtonType.OK);
        exitButton.setText("Exit");
        Optional<ButtonType> closeResponse = a.showAndWait();
        closeResponse.ifPresent(buttonType -> {
            if (ButtonType.OK.equals(buttonType)) {
                window.close();
            }
        });
    }
}
