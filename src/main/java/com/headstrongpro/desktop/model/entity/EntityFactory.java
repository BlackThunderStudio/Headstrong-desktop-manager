package com.headstrongpro.desktop.model.entity;

/**
 * Created by Rajmund Staniek on 09-May-17.
 */
public class EntityFactory {

    public static Person getUser(int id, String name, String email, String phone, String gender, String cpr, String street, String postal, String city, String country, String accountNo, String baseSalary) {
        return new User(id, name, email, phone, gender, cpr, street, postal, city, country, accountNo, baseSalary);
    }

    public static Person getUser(String name, String email, String phone, String gender, String cpr, String street, String postal, String city, String country, String accountNo, String baseSalary) {
        return new User(name, email, phone, gender, cpr, street, postal, city, country, accountNo, baseSalary);
    }

    public static Person getClient(int id, String name, String email, String phone, String gender, String login, String password, String registrationDate, String currencyAmount) {
        return new Client(id, name, email, phone, gender, login, password, registrationDate, currencyAmount);
    }

    public static Person getClient(String name, String email, String phone, String gender, String login, String password, String registrationDate, String currencyAmount) {
        return new Client(name, email, phone, gender, login, password, registrationDate, currencyAmount);
    }
}