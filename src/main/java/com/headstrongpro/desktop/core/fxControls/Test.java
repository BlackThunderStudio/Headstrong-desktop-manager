package com.headstrongpro.desktop.core.fxControls;

import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Rajmund Staniek on 25-May-17.
 */
public class Test implements Initializable {
    public LoadingBar loading;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loading.show("Testing some stuff...");
    }
}
