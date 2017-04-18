package com.headstrongpro.desktop.model;

import java.sql.Date;

/**
 * Subscription model class
 */
public class Subscription {
    private int id, noOfUsers, rateId;
    private boolean isActive;
    private Date startDate, endDate;

    public Subscription(int id, int noOfUsers, int rateId, boolean isActive, Date startDate, Date endDate) {
        this.id = id;
        this.noOfUsers = noOfUsers;
        this.rateId = rateId;
        this.isActive = isActive;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Subscription(int noOfUsers, int rateId, boolean isActive, Date startDate, Date endDate) {
        this.noOfUsers = noOfUsers;
        this.rateId = rateId;
        this.isActive = isActive;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNoOfUsers() {
        return noOfUsers;
    }

    public void setNoOfUsers(int noOfUsers) {
        this.noOfUsers = noOfUsers;
    }

    public int getRateId() {
        return rateId;
    }

    public void setRateId(int rateId) {
        this.rateId = rateId;
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
}
