package com.headstrongpro.desktop.controller;

import com.headstrongpro.desktop.DbLayer.DBSubscriptions;
import com.headstrongpro.desktop.core.exception.ConnectionException;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.model.PaymentRate;
import com.headstrongpro.desktop.model.Subscription;
import org.bouncycastle.math.raw.Mod;

import java.util.ArrayList;
import java.util.List;

/**
 * Subscriptions Controller
 */
public class SubscriptionsController implements Refreshable {

    private List<Subscription> subscriptions;
    private DBSubscriptions subscriptionDAO;

    public SubscriptionsController(){
        subscriptions = new ArrayList<>();
        subscriptionDAO = new DBSubscriptions();
    }

    public List<Subscription> getSubscriptions() throws ModelSyncException{
        refresh();
        return subscriptions;
    }

    @Override
    public void refresh() throws ModelSyncException{
        subscriptions.clear();
        subscriptions.addAll(subscriptionDAO.getAll());
    }

    public List<PaymentRate> getRates() throws ConnectionException {
        return subscriptionDAO.getRates();
    }
}
