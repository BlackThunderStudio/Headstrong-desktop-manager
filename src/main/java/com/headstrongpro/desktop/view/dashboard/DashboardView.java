package com.headstrongpro.desktop.view.dashboard;

import com.headstrongpro.desktop.core.fxControls.DashboardTile;
import eu.hansolo.tilesfx.Tile;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Ondřej Soukup on 19.05.2017.
 */
public class DashboardView implements Initializable {

    @FXML
    public DashboardTile dashCompaniesTile;
    @FXML
    public DashboardTile dashClientsTile;
    @FXML
    public DashboardTile dashSubscriptionsTile;
    @FXML
    public DashboardTile dashPaymentsTile;
    @FXML
    public DashboardTile dashCoursesTile;
    @FXML
    public DashboardTile dashResourcesTile;
    @FXML
    public DashboardTile dashSettingsTile;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
