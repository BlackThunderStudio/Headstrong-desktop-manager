package com.headstrongpro.desktop.model.entity;

/**
 * User Entity
 */
public class User extends Person {
    private int uid;
    private String cpr, street, postal, city, country, accountNo, baseSalary;

    public User(int id, String name, String email, String phone, String gender, int uid, String cpr, String street, String postal, String city, String country, String accountNo, String baseSalary) {
        super(id, name, email, phone, gender);
        this.uid = uid;
        this.cpr = cpr;
        this.street = street;
        this.postal = postal;
        this.city = city;
        this.country = country;
        this.accountNo = accountNo;
        this.baseSalary = baseSalary;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getCpr() {
        return cpr;
    }

    public void setCpr(String cpr) {
        this.cpr = cpr;
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

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getBaseSalary() {
        return baseSalary;
    }

    public void setBaseSalary(String baseSalary) {
        this.baseSalary = baseSalary;
    }
}
