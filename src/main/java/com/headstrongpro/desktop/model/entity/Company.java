package com.headstrongpro.desktop.model.entity;

/**
 * Created by 1062085 on 11-Apr-17.
 */
public class Company {
    private int id;
    private String name, cvr, street, postal, city, country;

    public Company(int id, String name, String cvr, String street, String postal, String city, String country) {
        this.id = id;
        this.name = name;
        this.cvr = cvr;
        this.street = street;
        this.postal = postal;
        this.city = city;
        this.country = country;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCvr() {
        return cvr;
    }

    public void setCvr(String cvr) {
        this.cvr = cvr;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPostal() {
        return postal;
    }

    public void setPostal(String postal) {
        this.postal = postal;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
