package com.headstrongpro.desktop.model.entity;

import java.sql.Date;

/**
 * Client Entity
 */
public class Client extends Person {
    private String login, password;
    private Date  registrationDate;
    private int companyId;

    public Client(int id, String name, String email, String phone, String gender, String login, String password, Date registrationDate, int companyId) {
        super(id, name, email, phone, gender);
        this.login = login;
        this.password = password;
        this.registrationDate = registrationDate;
        this.companyId = companyId;
    }

    public Client(String name, String email, String phone, String gender, String login, String password, Date registrationDate, int companyId) {
        super(name, email, phone, gender);
        this.login = login;
        this.password = password;
        this.registrationDate = registrationDate;
        this.companyId = companyId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public int getcompanyId() {
        return companyId;
    }

    public void setcompanyId(int companyId) {
        this.companyId = companyId;
    }
}
