package com.headstrongpro.desktop.model.entity;

import com.headstrongpro.desktop.model.Department;
import com.headstrongpro.desktop.model.Subscription;

import java.util.ArrayList;

/**
 * Company Entity
 */
public class Company {
    private int id;
    private String name, cvr, street, postal, city, country;
    private Subscription subscription;
    private ArrayList<Department> departments;

    public Company(int id, String name, String cvr, String street, String postal, String city, String country, Subscription subscription, ArrayList<Department> departments) {
        this.id = id;
        this.name = name;
        this.cvr = cvr;
        this.street = street;
        this.postal = postal;
        this.city = city;
        this.country = country;
        this.subscription = subscription;
        this.departments = departments;
    }

    public Company(String name, String cvr, String street, String postal, String city, String country, Subscription subscription, ArrayList<Department> departments) {
        this.name = name;
        this.cvr = cvr;
        this.street = street;
        this.postal = postal;
        this.city = city;
        this.country = country;
        this.subscription = subscription;
        this.departments = departments;
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

    public Subscription getSubscription() { return subscription; }

    public void setSubscription(Subscription subscription) { this.subscription = subscription; }

    public ArrayList<Department> getDepartments() { return departments;}

    public void setDepartments(ArrayList<Department> departments) { this.departments = departments;}
}
