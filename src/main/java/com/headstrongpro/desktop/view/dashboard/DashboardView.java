package com.headstrongpro.desktop.view.dashboard;

import com.headstrongpro.desktop.controller.Analytics;
import com.headstrongpro.desktop.core.fxControls.DashboardTile;
import com.headstrongpro.desktop.view.ContentSource;
import com.headstrongpro.desktop.view.ContentView;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Dashboard View
 */
public class DashboardView extends ContentView implements Initializable {

    private static final String TOTAL = "in\ntotal";
    private static Timer timer;

    // Dashboard tiles
    @FXML
    public DashboardTile companiesTile, clientsTile, subscriptionsTile, paymentsTile, coursesTile, resourcesTile;
    @FXML
    public StackPane settingsTile, aboutTile;

    private int companiesValue, clientsValue, subscriptionsValue, paymentsValue, coursesValue, resourcesValue;

    public static void endReloadingDashboard() {
        timer.cancel();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadData();
        TimerTask reload = new TimerTask() {
            @Override
            public void run() {
                loadData();
            }
        };
        timer = new Timer();
        long delay = 60 * 1000;
        long interval = 2 * 60 * 1000;
        timer.scheduleAtFixedRate(reload, delay, interval);
    }

    @Override
    protected void loadData() {
        //tasks init
        Task<Void> loadCompanies = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                companiesValue = Analytics.getCount(Analytics.Table.COMPANIES);
                return null;
            }
        };
        Task<Void> loadClients = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                clientsValue = Analytics.getCount(Analytics.Table.CLIENTS);
                return null;
            }
        };
        Task<Void> loadSubscriptions = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                subscriptionsValue = Analytics.getActiveSubscriptions();
                return null;
            }
        };
        Task<Void> loadPayments = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                paymentsValue = Analytics.getOverduePayments();
                return null;
            }
        };
        Task<Void> loadCourses = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                coursesValue = Analytics.getCount(Analytics.Table.COURSES);
                return null;
            }
        };
        Task<Void> loadResources = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                resourcesValue = Analytics.getCount(Analytics.Table.RESOURCES);
                return null;
            }
        };

        //event listeners
        loadCompanies.stateProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue.equals(Worker.State.SUCCEEDED)) {
                prepareTile(companiesTile, "Companies", TOTAL, companiesValue);
            }
        }));
        loadClients.stateProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue.equals(Worker.State.SUCCEEDED)) {
                prepareTile(clientsTile, "Clients", TOTAL, clientsValue);
            }
        }));
        loadSubscriptions.stateProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue.equals(Worker.State.SUCCEEDED)) {
                prepareTile(subscriptionsTile, "Subscriptions", "currently\nactive", subscriptionsValue);
            }
        }));
        loadPayments.stateProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue.equals(Worker.State.SUCCEEDED)) {
                prepareTile(paymentsTile, "Payments", "overdue\npayments", paymentsValue);
            }
        }));
        loadCourses.stateProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue.equals(Worker.State.SUCCEEDED)) {
                prepareTile(coursesTile, "Courses", TOTAL, coursesValue);
            }
        }));
        loadResources.stateProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue.equals(Worker.State.SUCCEEDED)) {
                prepareTile(resourcesTile, "Resources", TOTAL, resourcesValue);
            }
        }));

        //thread setup
        Thread[] threads = new Thread[6];
        threads[0] = new Thread(loadCompanies);
        threads[1] = new Thread(loadClients);
        threads[2] = new Thread(loadSubscriptions);
        threads[3] = new Thread(loadPayments);
        threads[4] = new Thread(loadCourses);
        threads[5] = new Thread(loadResources);

        for (Thread th : threads) {
            th.setDaemon(true);
            th.start();
        }
    }

    private void prepareTile(DashboardTile tile, String title, String subtitle, int value) {
        tile.setTitle(title);
        tile.setSubtitle(subtitle);
        tile.setValue(String.valueOf(value));
    }

    @FXML
    public void coursesTileOnClick() {
        mainWindowView.changeContent(ContentSource.COURSES);
    }

    @FXML
    public void resourcesTileOnClick() {
        mainWindowView.changeContent(ContentSource.RESOURCES);
    }

    @FXML
    public void companiesTileOnClick() {
        mainWindowView.changeContent(ContentSource.COMPANIES);
    }

    @FXML
    public void clientsTileOnClick() {
        mainWindowView.changeContent(ContentSource.CLIENTS);
    }

    @FXML
    public void subscriptionsTileOnClick() {
        mainWindowView.changeContent(ContentSource.SUBSCRIPTIONS);
    }

    @FXML
    public void paymentsTileOnClick() {
        mainWindowView.changeContent(ContentSource.PAYMENTS);
    }

    @FXML
    public void settingTileOnClick() {
        mainWindowView.changeContent(ContentSource.SETTINGS);
    }

    @FXML
    public void aboutTileOnClick() {
        mainWindowView.changeContent(ContentSource.ABOUT);
    }

}
