package com.headstrongpro.desktop.model;

/**
 * Created by jakub on 09/05/2017.
 */
public class PaymentRate {
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

    public int getId() {
        return id;
    }

    public int getNumberOfMonths() {
        return numberOfMonths;
    }

    public String getName() {
        return name;
    }
}
