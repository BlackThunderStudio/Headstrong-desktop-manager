package com.headstrongpro.desktop.model;

/**
 * Payment Rate Entity
 */
public class PaymentRate implements IModel {
    private int id, numberOfMonths;
    private String name;

    public PaymentRate(int id, int numberOfMonths, String name) {
        this.id = id;
        this.numberOfMonths = numberOfMonths;
        this.name = name;
    }

    public PaymentRate(int numberOfMonths, String name) {
        this.numberOfMonths = numberOfMonths;
        this.name = name;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public int getNumberOfMonths() {
        return numberOfMonths;
    }

    public String getName() {
        return name;
    }
}
