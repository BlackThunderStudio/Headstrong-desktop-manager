package com.headstrongpro.desktop.model;

import java.sql.Date;

/**
 * Payment model class
 */
public class Payment {
    private int id;
    private double value;
    private Date timestamp, dueDate;

    public Payment(int id, double value, Date timestamp, Date dueDate) {
        this.id = id;
        this.value = value;
        this.timestamp = timestamp;
        this.dueDate = dueDate;
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
}
