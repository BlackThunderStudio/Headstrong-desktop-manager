package com.headstrongpro.desktop.model;

/**
 * Department model class
 */
public class Department implements IModel {
    private int id, companyId;
    private String name, description;

    public Department(int id, String name, String description, int companyId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.companyId = companyId;
    }

    public Department(String name, String description, int companyId) {
        this.name = name;
        this.description = description;
        this.companyId = companyId;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
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

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }
}
