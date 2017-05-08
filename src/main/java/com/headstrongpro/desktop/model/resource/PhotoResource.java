package com.headstrongpro.desktop.model.resource;

import javafx.scene.image.Image;

/**
 * Created by rajmu on 17.05.08.
 */
//TODO: Custom functionality and behaviour to be added!
class PhotoResource implements Resource {

    private static final int TYPE = 2;

    private int id;
    private String name, description, url;
    private boolean isForAchievement;

    public PhotoResource(String name, String description, String url, boolean isForAchievement) {
        this.name = name;
        this.description = description;
        this.url = url;
        this.isForAchievement = isForAchievement;
    }

    public PhotoResource(int id, String name, String description, String url, boolean isForAchievement) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.url = url;
        this.isForAchievement = isForAchievement;
    }

    @Override
    public String getURL() {
        return url;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isForAchievement() {
        return isForAchievement;
    }

    @Override
    public int getType() {
        return TYPE;
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public void setID(int id) {
        this.id = id;
    }

    public Image getImage(){
        return new Image(url);
    }
}
