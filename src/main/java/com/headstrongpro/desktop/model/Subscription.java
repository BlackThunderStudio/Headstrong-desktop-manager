package com.headstrongpro.desktop.model;

import com.headstrongpro.desktop.model.entity.Company;

import java.sql.Date;

/**
 * Subscription Entity
 */
public class Subscription implements IModel {
    private int id, noOfUsers;
    private boolean isActive;
    private Date startDate, endDate;
    private PaymentRate rate;
    private Company company;

    public Subscription() {
    }

    public Subscription(int id, int noOfUsers, boolean isActive, Date startDate, Date endDate, PaymentRate rate, Company company) {
        this.id = id;
        this.noOfUsers = noOfUsers;
        this.isActive = isActive;
        this.startDate = startDate;
        this.endDate = endDate;
        this.rate = rate;
        this.company = company;
    }

    public Subscription(int noOfUsers, boolean isActive, Date startDate, Date endDate, PaymentRate rate, Company company) {
        this.noOfUsers = noOfUsers;
        this.isActive = isActive;
        this.startDate = startDate;
        this.endDate = endDate;
        this.rate = rate;
        this.company = company;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public int getNoOfUsers() {
        return noOfUsers;
    }

    public void setNoOfUsers(int noOfUsers) {
        this.noOfUsers = noOfUsers;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public PaymentRate getRate() {
        return rate;
    }

    public void setRate(PaymentRate rate) {
        this.rate = rate;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    @Override
    public String toString() {
        return "the subscription for " +
                company +
                " (from " + startDate +
                " to " + endDate + " " +
                "for " + noOfUsers + " users)";
    }
}
