package com.headstrongpro.desktop.view.dashboard;

import com.headstrongpro.desktop.controller.Analytics;
import com.headstrongpro.desktop.core.fxControls.DashboardTile;
import com.headstrongpro.desktop.view.ContentView;
import com.headstrongpro.desktop.view.MainWindowView;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
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

    private static final String TOTAL = "in\ntotal";

    private Task<Void> loadCompanies;
    private Task<Void> loadClients;
    private Task<Void> loadSubs;
    private Task<Void> loadPayments;
    private Task<Void> loadCourses;
    private Task<Void> loadRes;

    private int valCompanies, valClients, valSubs, valPayments, valCourses, valRes;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadData();
    }

    private void loadData(){
        //tasks init
        loadCompanies = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                valCompanies = Analytics.getCount(Analytics.Table.COMPANIES);
                return null;
            }
        };
        loadClients = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                valClients = Analytics.getCount(Analytics.Table.CLIENTS);
                return null;
            }
        };
        loadSubs = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                valSubs = Analytics.getActiveSubstriptions();
                return null;
            }
        };
        loadPayments = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                valPayments = Analytics.getOverduePayments();
                return null;
            }
        };
        loadCourses = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                valCourses = Analytics.getCount(Analytics.Table.COURSES);
                return null;
            }
        };
        loadRes = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                valRes = Analytics.getCount(Analytics.Table.RESOURCES);
                return null;
            }
        };

        //event listeners
        loadCompanies.stateProperty().addListener(((observable, oldValue, newValue) -> {
            if(newValue.equals(Worker.State.SUCCEEDED)){
                prepareTile(dashCompaniesTile, "Companies", TOTAL, valCompanies);
            }
        }));
        loadClients.stateProperty().addListener(((observable, oldValue, newValue) -> {
            if(newValue.equals(Worker.State.SUCCEEDED)){
                prepareTile(dashClientsTile, "Clients", TOTAL, valClients);
            }
        }));
        loadSubs.stateProperty().addListener(((observable, oldValue, newValue) -> {
            if(newValue.equals(Worker.State.SUCCEEDED)){
                prepareTile(dashSubscriptionsTile, "Subscriptions", "currently\nactive", valSubs);
            }
        }));
        loadPayments.stateProperty().addListener(((observable, oldValue, newValue) -> {
            if(newValue.equals(Worker.State.SUCCEEDED)){
                prepareTile(dashPaymentsTile, "Payments", "overdue\npayments", valPayments);
            }
        }));
        loadCourses.stateProperty().addListener(((observable, oldValue, newValue) -> {
            if(newValue.equals(Worker.State.SUCCEEDED)){
                prepareTile(dashCoursesTile, "Courses", TOTAL, valCourses);
            }
        }));
        loadRes.stateProperty().addListener(((observable, oldValue, newValue) -> {
            if(newValue.equals(Worker.State.SUCCEEDED)){
                prepareTile(dashResourcesTile, "Resources", TOTAL, valRes);
            }
        }));

        //thread setup
        Thread[] threads = new Thread[6];
        threads[0] = new Thread(loadCompanies);
        threads[1] = new Thread(loadClients);
        threads[2] = new Thread(loadSubs);
        threads[3] = new Thread(loadPayments);
        threads[4] = new Thread(loadCourses);
        threads[5] = new Thread(loadRes);

        for(Thread th : threads){
            th.setDaemon(true);
            th.start();
        }
    }

    private void prepareTile(DashboardTile tile, String title, String subtitle, int value){
        tile.setTitle(title);
        tile.setSubtitle(subtitle);
        tile.setValue(String.valueOf(value));
    }
}
