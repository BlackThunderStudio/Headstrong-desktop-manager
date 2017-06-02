package com.headstrongpro.desktop.model;

import com.headstrongpro.desktop.model.resource.Resource;

import java.util.ArrayList;
import java.util.List;

/**
 * Course model class
 */
public class Course {
    private int id;
    private String name, description;
    private CourseCategory courseCategory;
    private List<Resource> resources;

    public Course(int id, String name, String description, CourseCategory courseCategory) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.courseCategory = courseCategory;
    }

    public Course(String name, String description, CourseCategory courseCategory) {
        this.name = name;
        this.description = description;
        this.courseCategory = courseCategory;
    }

    public Course(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Course(){};

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

    public CourseCategory getCourseCategory() {
        return courseCategory;
    }

    public void setCourseCategory(CourseCategory courseCategory) { this.courseCategory = courseCategory; }

    public List<Resource> getResources() { return resources; }

    public void setResources(List<Resource> resources) { this.resources = resources; }
}
