package com.headstrongpro.desktop.model;

/**
 * Group model class
 */
public class Group {
    private int id, companyId, parentId;
    private String name;

    public Group(int id, String name, int companyId, int parentId) {
        this.id = id;
        this.name = name;
        this.companyId = companyId;
        this.parentId = parentId;
    }

    public Group(int id, String name, int companyId) {
        this.id = id;
        this.name = name;
        this.companyId = companyId;
        this.parentId = -1;
    }

    public Group(String name, int companyId, int parentId) {
        this.name = name;
        this.companyId = companyId;
        this.parentId = parentId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
