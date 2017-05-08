package com.headstrongpro.desktop.model.resource;

/**
 * Created by rajmu on 17.05.08.
 */
//TODO: Custom functionality and behaviour to be added!
class VideoResource implements Resource {

    private static final int TYPE = 4;

    private int id;
    private String name, description, url;
    private boolean isForAchievement;

    public VideoResource(String name, String description, String url, boolean isForAchievement) {
        this.name = name;
        this.description = description;
        this.url = url;
        this.isForAchievement = isForAchievement;
    }

    public VideoResource(int id, String name, String description, String url, boolean isForAchievement) {
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
}
