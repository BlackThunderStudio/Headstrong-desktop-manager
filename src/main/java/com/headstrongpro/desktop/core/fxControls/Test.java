package com.headstrongpro.desktop.core.fxControls;

import javafx.fxml.Initializable;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Rajmund Staniek on 25-May-17.
 */
public class Test implements Initializable {


    public DashboardTile t1;
    public DashboardTile t2;
    public DashboardTile t3;
    public DashboardTile t4;
    public DashboardTile t5;
    public DashboardTile t6;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        t1.setTextFill(Color.web("#eeeeee"));
        t2.setTextFill(Color.web("#eeeeee"));
        t3.setTextFill(Color.web("#eeeeee"));
        t4.setTextFill(Color.web("#eeeeee"));
        t5.setTextFill(Color.web("#eeeeee"));
        t6.setTextFill(Color.web("#eeeeee"));

        t1.setBackgroundColor("#323232");
        t2.setBackgroundColor("#323232");
        t3.setBackgroundColor("#323232");
        t4.setBackgroundColor("#323232");
        t5.setBackgroundColor("#323232");
        t6.setBackgroundColor("#323232");

        t1.setTitle("Companies");
        t1.setSubtitle("Clients this month");
        t1.setValue("+5");
    }
}
