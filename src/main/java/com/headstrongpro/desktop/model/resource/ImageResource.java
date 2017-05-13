package com.headstrongpro.desktop.model.resource;

/**
 * Created by rajmu on 17.05.08.
 */
//TODO: Custom functionality and behaviour to be added!
public class ImageResource implements Resource {

    private static final ResourceType TYPE = ResourceType.IMAGE;

    private int id;
    private String name, description, url;
    private boolean isForAchievement;

    ImageResource(String name, String description, boolean isForAchievement) {
        this.name = name;
        this.description = description;
        this.isForAchievement = isForAchievement;
    }

    ImageResource(int id, String name, String description, boolean isForAchievement) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.isForAchievement = isForAchievement;
    }

    private ImageResource(){}

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
    public ResourceType getType() {
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

    public void setURL(String url) {
        this.url = url;
    }
}
