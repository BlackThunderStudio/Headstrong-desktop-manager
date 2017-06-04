package com.headstrongpro.desktop.controller;

import com.headstrongpro.desktop.DbLayer.DBPayment;
import com.headstrongpro.desktop.core.exception.DatabaseOutOfSyncException;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.model.Payment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * desktop-manager
 * <p>
 * because of the lack of time this controller lacks a lot of fool proofing
 * and functionality. For more sophisticated implementation please see ResourcesController
 * <p>
 * <p>
 * <p>
 * Created by rajmu on 17.06.01.
 */
public class PaymentsController implements Refreshable, IContentController<Payment> {

    private DBPayment dbPayment;
    private List<Payment> paymentList;

    public PaymentsController() {
        dbPayment = new DBPayment();
        paymentList = new ArrayList<>();
    }

    @Concurrent
    @Override
    public void refresh() throws ModelSyncException {
        paymentList = dbPayment.getAll();
    }

    @Override
    public ObservableList<Payment> getAll() throws ModelSyncException {
        refresh();
        return FXCollections.observableArrayList(paymentList);
    }

    @Override
    public Payment getById(int id) throws ModelSyncException {
        return dbPayment.getById(id);
    }

    @Override
    public ObservableList<Payment> searchByPhrase(String input) {
        return FXCollections.observableArrayList(paymentList
                .stream()
                .filter(e -> String.valueOf(e.getSubscription().getId()).contains(input) ||
                        String.valueOf(e.getValue()).contains(input) ||
                        e.getSubscription()
                                .getCompany()
                                .getName()
                                .toLowerCase()
                                .contains(input) ||
                        e.getSubscription()
                                .getCompany()
                                .getCvr()
                                .toLowerCase()
                                .contains(input))
                .collect(Collectors.toList()));
    }

    @Override
    public void delete(Payment payment) throws DatabaseOutOfSyncException, ModelSyncException {
        if (payment == null) throw new IllegalArgumentException("Cannot be null");
        dbPayment.delete(payment);
    }

    @Override
    public Payment createNew(Payment payment) throws ModelSyncException {
        if (payment != null) throw new IllegalArgumentException("Cannot be null");
        return dbPayment.persist(payment);
    }

    @Override
    public void edit(Payment payment) throws DatabaseOutOfSyncException, ModelSyncException {
        dbPayment.update(payment);
    }
}
