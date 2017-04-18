package com.headstrongpro.desktop.model;

import java.util.ArrayList;

/**
 * Resource model
 */
public abstract class Resource {
    private int id;
    private String name, description, url;
    private ArrayList<Achievement> achievements;
    private Session session;

    public Resource(int id, String name, String description, String url, ArrayList<Achievement> achievements, Session session) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.url = url;
        this.achievements = achievements;
        this.session = session;
    }

    public Resource(String name, String description, String url, ArrayList<Achievement> achievements, Session session) {
        this.name = name;
        this.description = description;
        this.url = url;
        this.achievements = achievements;
        this.session = session;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ArrayList<Achievement> getAchievements() {
        return achievements;
    }

    public void setAchievements(ArrayList<Achievement> achievements) {
        this.achievements = achievements;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }
}
