package com.headstrongpro.desktop.view.dashboard;

import com.headstrongpro.desktop.core.fxControls.DashboardTile;
import com.headstrongpro.desktop.view.ContentView;
import com.headstrongpro.desktop.view.MainWindowView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Ondřej Soukup on 19.05.2017.
 */
public class DashboardView extends ContentView implements Initializable {

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
    @FXML
    public DashboardTile dashEmployeesTile;



    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
