package com.headstrongpro.desktop.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * AboutView
 */
public class AboutView extends ContentView implements Initializable {

    @FXML
    public WebView webView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        WebEngine engine = webView.getEngine();
        engine.load("http://remix1436.ct8.pl/resources/about/");
    }
}
