package com.headstrongpro.desktop.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Pane;
import jfxtras.resources.JFXtrasFontRoboto;

import java.io.IOException;
import java.net.URL;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            FXMLLoader navigationBarLoader = new FXMLLoader();
            JFXtrasFontRoboto.loadAll();
            navigationBarLoader.setLocation(getClass().getResource("/layout/navigationPane.fxml"));
            navigationBar = navigationBarLoader.load();
            navigationBar.setStyle("-fx-font-family: '" + JFXtrasFontRoboto.AvailableFonts.RobotoLight.getFamilyName() + "';");
            NavigationView navigationView = navigationBarLoader.getController();
            navigationView.setMainWindowView(this);

        } catch (Exception e) {
            e.printStackTrace();
        }
        layout.getItems().set(0, navigationBar);
        layout.getItems().set(1, contentBar);
        layout.getItems().set(2, contextBar);
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

            // Prepare FXML Loaders and set locations of content and context views
            FXMLLoader contentBarLoader = new FXMLLoader();
            contentBarLoader.setLocation(getClass().getResource(contentSource.getContentView()));
            FXMLLoader contextBarLoader = new FXMLLoader();
            contextBarLoader.setLocation(getClass().getResource(contentSource.getContextView()));

            // Try to load content and context views from FXML resources
            try {
                contentBar = contentBarLoader.load();
                contextBar = contextBarLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Give both content and context view controllers access to the MainWindowView
            contentView = contentBarLoader.getController();
            contentView.setMainWindowView(this);
            contextView = contextBarLoader.getController();
            contextView.setMainWindowView(this);

            contentBar.setStyle("-fx-font-family: '" + JFXtrasFontRoboto.AvailableFonts.RobotoLight.getFamilyName() + "';");
            contextBar.setStyle("-fx-font-family: '" + JFXtrasFontRoboto.AvailableFonts.RobotoLight.getFamilyName() + "';");
            layout.getItems().set(1, contentBar);
            layout.getItems().set(2, contextBar);
            layout.setDividerPosition(0, 0.20);
            layout.setDividerPosition(1, 0.75);
        }
    }

    public void changeContext(ContentSource contentSource) {
        FXMLLoader contextBarLoader = new FXMLLoader();
        contextBarLoader.setLocation(getClass().getResource(contentSource.getContextView()));
        try {
            contextBar = contextBarLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        contextView = contextBarLoader.getController();
        contextView.setMainWindowView(this);

        contextBar.setStyle("-fx-font-family: '" + JFXtrasFontRoboto.AvailableFonts.RobotoLight.getFamilyName() + "';");

        layout.getItems().set(2, contextBar);
        layout.setDividerPosition(1, 0.75);
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
}
