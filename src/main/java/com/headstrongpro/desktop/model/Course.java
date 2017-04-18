package com.headstrongpro.desktop.model;

import java.util.ArrayList;

/**
 * Course model class
 */
public class Course {
    private int id;
    private String name, description;
    private CourseCategory courseCategory;
    private ArrayList<Session> sessions;

    public Course(int id, String name, String description, CourseCategory courseCategory, ArrayList<Session> sessions) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.courseCategory = courseCategory;
        this.sessions = sessions;
    }

    public Course(String name, String description, CourseCategory courseCategory, ArrayList<Session> sessions) {
        this.name = name;
        this.description = description;
        this.courseCategory = courseCategory;
        this.sessions = sessions;
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

    public CourseCategory getCourseCategory() {
        return courseCategory;
    }

    public void setCourseCategory(CourseCategory courseCategory) {
        this.courseCategory = courseCategory;
    }

    public ArrayList<Session> getSessions() {
        return sessions;
    }

    public void setSessions(ArrayList<Session> sessions) {
        this.sessions = sessions;
    }
}
