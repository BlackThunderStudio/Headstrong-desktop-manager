package com.headstrongpro.desktop.model;

import java.sql.Date;

/**
 * Payment model class
 */
public class Payment {
    private int id;
    private double value;
    private Date timestamp, dueDate;
    boolean paid;
    private Subscription subscription;
    private String companyName, companyCvr;

    public Payment(int id, double value, Date timestamp, Date dueDate, boolean paid, Subscription subscription) {
        this.id = id;
        this.value = value;
        this.timestamp = timestamp;
        this.dueDate = dueDate;
        this.paid = paid;
        this.subscription = subscription;
        companyName = subscription.getCompany().getName();
        companyCvr = subscription.getCompany().getCvr();
    }

    public Payment(double value, Date timestamp, Date dueDate) {
        this.value = value;
        this.timestamp = timestamp;
        this.dueDate = dueDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isPaid() { return paid; }

    public void setPaid() { this.paid = paid; }

    public Subscription getSubscription() { return subscription; }

    public void setSubscription(Subscription subscription) { this.subscription = subscription; }

    public String getCompanyName() {
        return companyName;
    }

    public String getCompanyCvr() {
        return companyCvr;
    }
}
