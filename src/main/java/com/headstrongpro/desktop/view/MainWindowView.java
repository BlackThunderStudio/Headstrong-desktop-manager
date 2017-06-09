package com.headstrongpro.desktop.view;

import com.headstrongpro.desktop.Main;
import com.headstrongpro.desktop.core.fxControls.Footer;
import javafx.application.Platform;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import jfxtras.resources.JFXtrasFontRoboto;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Main Window View
 */
public class MainWindowView extends RootLayoutView implements Initializable {

    @FXML
    public SplitPane layout;
    @FXML
    public Pane navigationBar;
    @FXML
    public Pane contentBar;
    @FXML
    public Pane contextBar;

    private ContentView contentView;
    private ContextView contextView;

    private ContentSource currentContentSource;

    private boolean fullContentMode;
    private boolean updateDisplayedBefore;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateDisplayedBefore = false;
        checkForUpdates.valueProperty().addListener(((observable, oldValue, newValue) -> {
            if(newValue != null){
                if(newValue && !updateDisplayedBefore){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION,
                            "Do you want to restart the application in order to install the latest update? Make sure you saved all the changes in the application.",
                            ButtonType.YES,
                            ButtonType.NO,
                            ButtonType.CANCEL);
                    Button ignoreBtn = (Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL);
                    ignoreBtn.setText("Ignore");
                    alert.setHeaderText("A new update is available");
                    Optional<ButtonType> response = alert.showAndWait();
                    response.ifPresent(e -> {
                        if(e.equals(ButtonType.YES)){
                            try {
                                Runtime.getRuntime().exec(new String[]{"java", "-jar", "Headstrong launcher.jar"});
                                Platform.exit();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                                getContentView().footer.show(e1.getMessage(), Footer.NotificationType.ERROR);
                            }
                        } else if(e.equals(ButtonType.NO)){
                            updateDisplayedBefore = false;
                        } else if(e.equals(ButtonType.CANCEL)){
                            updateDisplayedBefore = true;
                        }
                    });
                    alert.setHeaderText("A new update is available");
                } else System.out.println("No updates found so far.");
            }
        }));
        checkForUpdates.setPeriod(Duration.minutes(15.0));
        checkForUpdates.start();

        try {
            FXMLLoader navigationBarLoader = new FXMLLoader();
            JFXtrasFontRoboto.loadAll();
            navigationBarLoader.setLocation(getClass().getResource("/layout/navigationPane.fxml"));
            navigationBar = navigationBarLoader.load();
            navigationBar.setStyle("-fx-font-family: '" + JFXtrasFontRoboto.AvailableFonts.RobotoRegular.getFamilyName() + "';");
            NavigationView navigationView = navigationBarLoader.getController();
            navigationView.setMainWindowView(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        layout.getItems().set(0, navigationBar);

        changeContent(ContentSource.DASHBOARD);

        layout.setDividerPosition(0, 0.20);
        layout.setDividerPosition(1, 0.75);
    }

    /**
     * Changes content of the main wrapper consisting of ContentView and ContextView
     *
     * @param contentSource Enum type of the content source
     */
    public void changeContent(ContentSource contentSource) {
        // Check whether you are not loading already loaded content
        if (currentContentSource != contentSource) {
            // Update current source type
            currentContentSource = contentSource;

            // Prepare FXML Loader and set location of content view
            FXMLLoader contentBarLoader = new FXMLLoader();
            contentBarLoader.setLocation(getClass().getResource(contentSource.getContentViewPath()));

            // Try to load content and context views from FXML resources
            try {
                contentBar = contentBarLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Give the content view controller access to the MainWindowView
            contentView = contentBarLoader.getController();
            contentView.setMainWindowView(this);

            // Add the bar to the root layout
            layout.getItems().set(1, contentBar);

            // Set default font of content view
            contentBar.setStyle("-fx-font-family: '" + JFXtrasFontRoboto.AvailableFonts.RobotoRegular.getFamilyName() + "';");

            // Prepare FXML Loader and set location of context view, if required
            if (contentSource.getContextViewPath() != null) {
                changeContext(contentSource);
            } else {
                fullContentMode = true;
                layout.getItems().remove(contextBar);
            }

            layout.setDividerPosition(0, 0.20);
        }
    }

    public void changeContext(ContentSource contentSource) {
        if (fullContentMode) {
            layout.getItems().add(contextBar);
            fullContentMode = false;
        }
        // Prepare FXML Loader and set location of context view
        FXMLLoader contextBarLoader = new FXMLLoader();
        contextBarLoader.setLocation(getClass().getResource(contentSource.getContextViewPath()));

        // Try to load context view from FXML resource
        try {
            contextBar = contextBarLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Give the context view controller access to the MainWindowView
        contextView = contextBarLoader.getController();
        contextView.setMainWindowView(this);

        // Set default font of context view
        contextBar.setStyle("-fx-font-family: '" + JFXtrasFontRoboto.AvailableFonts.RobotoRegular.getFamilyName() + "';");

        // Add the bar to the root layout
        layout.getItems().set(2, contextBar);
        layout.setDividerPosition(1, 0.75);
    }

    @SuppressWarnings("unchecked")
    public void changeContext(ContentSource contentSource, Object contextItem) {
        changeContext(contentSource);
        getContextView().changeContextItem(contextItem);
    }

    /**
     * @return Returns the content view of currently loaded source
     */
    public ContentView getContentView() {
        return contentView;
    }

    /**
     * @return Returns the context view of currently loaded source
     */
    public ContextView getContextView() {
        return contextView;
    }

    /***
     * Component that will check for app updates in a given time interval
     */
    private ScheduledService<Boolean> checkForUpdates = new ScheduledService<Boolean>() {
        @Override
        protected Task<Boolean> createTask() {
            return new Task<Boolean>() {
                @Override
                protected Boolean call() throws Exception {
                    System.out.println("Checking for updates...");
                    String path = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
                    path = path.substring(1, path.lastIndexOf('/')) + "/cfg";
                    path = path.replaceAll("%20", " ");
                    String remote = "";
                    try {
                        JSONObject json = (JSONObject) new JSONParser().parse(new InputStreamReader(new FileInputStream(path + "/launcher.json")));
                        json = (JSONObject)json.get("remote_path");
                        String remotePath = (String)json.get("update");
                        json = (JSONObject) new JSONParser().parse(new BufferedReader(new InputStreamReader(new URL(remotePath).openStream())));
                        json = (JSONObject)json.get("desktop");
                        remote = (String)json.get("version");
                    } catch (IOException | ParseException e) {
                        e.printStackTrace();
                    }
                    String local = Main.getAppVersion();

                    System.out.println("Local version: " + local);
                    System.out.println("Remote version: " + remote);

                    String[] verSplit = remote.split("\\.");
                    String[] localSplit = local.split("\\.");

                    for (int i = 0; i < localSplit.length; i++){
                        int a = Integer.parseInt(localSplit[i]);
                        int b = Integer.parseInt(verSplit[i]);
                        if(a < b) return true;
                    }
                    return false;
                }
            };
        }
    };
}
