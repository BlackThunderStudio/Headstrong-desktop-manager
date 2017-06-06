package com.headstrongpro.desktop.model;

import com.headstrongpro.desktop.model.resource.Resource;

import java.util.List;

/**
 * Session model class
 */
public class Session implements IModel {
    private int id;
    private String name, description;
    private List<Resource> resources;

    public Session(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Session() {
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

    public List<Resource> getResources() {
        return resources;
    }

    public void setResources(List<Resource> resources) {
        this.resources = resources;
    }
}
