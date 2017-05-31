package com.headstrongpro.desktop;

import com.headstrongpro.desktop.controller.UserController;
import com.headstrongpro.desktop.view.RootLayoutView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
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
    private Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
//        SplashScreen.viewSplashScreen(3000, SplashScreen.Style.FADE, SplashScreen.Style.FADE);
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Headstrong Company Manager");

        // Set minimum window size
        this.primaryStage.setMinHeight(700);
        this.primaryStage.setMinWidth(800);

        this.primaryStage.setOnCloseRequest(e -> {
            e.consume();
            closeProgram();
        });

        initLayout();
    }

    public void initLayout() {
        try {
            // Load root layout from fxml file based on whether user is logged in
            FXMLLoader loader = new FXMLLoader();
            String location = UserController.isLoggedIn() ? "/layout/mainWindow.fxml" : "/layout/login.fxml";
            loader.setLocation(getClass().getResource(location));

            // Show the scene containing the root layout.
            Scene scene = new Scene(loader.load());
            primaryStage.setScene(scene);

            RootLayoutView rootLayoutView = loader.getController();
            rootLayoutView.setMainApp(this);

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                primaryStage.close();
            }
        });
    }
}
