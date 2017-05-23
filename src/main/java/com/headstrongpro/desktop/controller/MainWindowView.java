package com.headstrongpro.desktop.controller;

import javafx.fxml.Initializable;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by rajmu on 17.04.06.
 */
public class MainWindowView implements Initializable {
    public WebView webViw;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        WebEngine engine = webViw.getEngine();
        engine.load("http://headstrongpro.com/alt/maps/");

    }
}
