package com.headstrongpro.desktop.model;

/**
 * Department model class
 */
public class Department {
    private int id, companyID;
    private String name, description;

    public Department(int id, String name, String description, int companyID) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.companyID = companyID;
    }

    public Department(String name, String description, int companyID) {
        this.name = name;
        this.description = description;
        this.companyID = companyID;
    }

    public Department(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Department(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCompanyID() {
        return companyID;
    }

    public void setCompanyID(int companyID) {
        this.companyID = companyID;
    }
}
