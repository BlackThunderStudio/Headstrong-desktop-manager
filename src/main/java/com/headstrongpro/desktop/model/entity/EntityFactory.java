package com.headstrongpro.desktop.model.entity;

import java.sql.Date;

/**
 * Entity Factory
 */
public class EntityFactory {

    //TODO: Implement to all entities

    public static Person getUser(int id, String name, String email, String phone, String gender, String cpr, String street, String postal, String city, String country, String accountNo, String baseSalary) {
        return new User(id, name, email, phone, gender, cpr, street, postal, city, country, accountNo, baseSalary);
    }

    public static Person getUser(String name, String email, String phone, String gender, String cpr, String street, String postal, String city, String country, String accountNo, String baseSalary) {
        return new User(name, email, phone, gender, cpr, street, postal, city, country, accountNo, baseSalary);
    }

    public static Client getClient(int id, String name, String email, String phone, String gender, String login, String password, Date registrationDate, int companyId) {
        return new Client(id, name, email, phone, gender, login, password, registrationDate, companyId);
    }

    public static Person getClient(String name, String email, String phone, String gender, String login, String password, Date registrationDate, int companyId) {
        return new Client(name, email, phone, gender, login, password, registrationDate, companyId);
    }
}