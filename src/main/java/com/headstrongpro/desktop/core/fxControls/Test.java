package com.headstrongpro.desktop.core.fxControls;

import javafx.fxml.Initializable;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Rajmund Staniek on 25-May-17.
 */
public class Test implements Initializable {
    public DashboardTile tile1;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tile1.title.setText("Sexy title");
        tile1.value.setText("20");
        tile1.subtitle.setText("fucks given today");
        tile1.setTextFill(Color.web("#eeeeee"));
        tile1.pane.setStyle("-fx-background-color: #323232");
    }
}
