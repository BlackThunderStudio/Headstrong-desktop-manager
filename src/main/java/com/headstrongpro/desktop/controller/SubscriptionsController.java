package com.headstrongpro.desktop.controller;

import com.headstrongpro.desktop.DbLayer.DBSubscriptions;
import com.headstrongpro.desktop.core.exception.ConnectionException;
import com.headstrongpro.desktop.core.exception.DatabaseOutOfSyncException;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.model.PaymentRate;
import com.headstrongpro.desktop.model.Subscription;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.bouncycastle.math.raw.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Subscriptions Controller
 */
public class SubscriptionsController implements Refreshable, IContentController<Subscription> {

    private List<Subscription> subscriptions;
    private DBSubscriptions subscriptionDAO;

    public SubscriptionsController(){
        subscriptions = new ArrayList<>();
        subscriptionDAO = new DBSubscriptions();
    }

    public ObservableList<Subscription> getAll() throws ModelSyncException{
        refresh();
        return FXCollections.observableArrayList(subscriptions);
    }

    @Override
    public void refresh() throws ModelSyncException{
        subscriptions.clear();
        subscriptions.addAll(subscriptionDAO.getAll());
    }

    public List<PaymentRate> getRates() throws ConnectionException {
        return subscriptionDAO.getRates();
    }


    @Override
    public ObservableList<Subscription> searchByPhrase(String input) {
        if(input == null) throw new IllegalArgumentException("Input cannot be null!");
        return FXCollections.observableArrayList(subscriptions
                .stream()
                .filter(e -> e.getCompany().getName().toLowerCase().contains(input) ||
                        e.getRate().getName().toLowerCase().contains(input) ||
                        String.valueOf(e.getNoOfUsers()).contains(input))
                .collect(Collectors.toList()));
    }

    @Override
    public void delete(Subscription subscription) throws DatabaseOutOfSyncException, ModelSyncException {
        subscriptionDAO.delete(subscription);
        refresh();
    }

    @Override
    public Subscription createNew(Subscription subscription) throws ModelSyncException {
        return null;
    }

    @Override
    public void edit(Subscription subscription) throws DatabaseOutOfSyncException, ModelSyncException {

    }

    @Override
    public Subscription getByID(int id) throws ModelSyncException {
        return null;
    }
}
