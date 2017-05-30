package com.headstrongpro.desktop.core.fxControls;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;

/**
 * desktop-manager
 * <p>
 * <p>
 * Created by rajmu on 17.05.30.
 */
public class DashboardTile extends AnchorPane {

    @FXML
    public GridPane background;

    @FXML
    public Label title;

    @FXML
    public Label value;

    @FXML
    public Label subtitle;

    public DashboardTile(){
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/layout/customControls/dashboardTile.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try{
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
