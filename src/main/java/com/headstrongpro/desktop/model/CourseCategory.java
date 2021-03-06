package com.headstrongpro.desktop.model;

/**
 * Course Category model
 */
public class CourseCategory implements IModel {
    private int id;
    private String name;

    public CourseCategory(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public CourseCategory(String name) {
        this.name = name;
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
}
