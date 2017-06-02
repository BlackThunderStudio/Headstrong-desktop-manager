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

    private boolean fullContentMode;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
            contentBarLoader.setLocation(getClass().getResource(contentSource.getContentView()));

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
            if (contentSource.getContextView() != null) {
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
        contextBarLoader.setLocation(getClass().getResource(contentSource.getContextView()));

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
}
