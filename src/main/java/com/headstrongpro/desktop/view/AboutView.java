package com.headstrongpro.desktop.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * desktop-manager
 * <p>
 * <p>
 * Created by rajmu on 17.05.26.
 */
public class AboutView implements Initializable {

    @FXML
    public WebView webView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        WebEngine engine = webView.getEngine();
        engine.load("http://remix1436.ct8.pl/resources/about/");
    }
}
