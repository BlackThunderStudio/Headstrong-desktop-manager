package com.headstrongpro.desktop.core.fxControls;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.io.IOException;

/**
 * DashboardTile
 */
public class DashboardTile extends GridPane {

    @FXML
    public GridPane pane;

    @FXML
    public ImageView background;

    @FXML
    public Label title;

    @FXML
    public Label value;

    @FXML
    public Label subtitle;

    public DashboardTile() {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/layout/customControls/dashboardTile.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
            subtitle.setWrapText(true);
            title.setWrapText(true);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void setTextFill(Color color) {
        title.setTextFill(color);
        subtitle.setTextFill(color);
        value.setTextFill(color);
    }

    public void setTitle(String string) {
        title.setText(string);
    }

    public void setSubtitle(String string) {
        subtitle.setText(string);
    }

    public void setValue(String string) {
        value.setText(string);
    }

    public void clear() {
        title.setText("");
        subtitle.setText("");
        value.setText("");
        background.setImage(null);
    }

    public void setBackgroundColor(String colorCode) {
        pane.setStyle("-fx-background-color: " + colorCode);
    }
}
