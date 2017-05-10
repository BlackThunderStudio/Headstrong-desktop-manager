package com.headstrongpro.desktop.model.resource;

import java.sql.Time;

/**
 * Created by rajmu on 17.05.08.
 */

//TODO: Custom functionality and behaviour to be added!
public class AudioResource implements Resource {

    private static final int TYPE = 3;

    private int id;
    private String name, description, url;
    private boolean isForAchievement;
    private Time duration;

    AudioResource(String name, String description, boolean isForAchievement) {
        this.name = name;
        this.description = description;
        this.url = url;
        this.isForAchievement = isForAchievement;
    }

    AudioResource(int id, String name, String description, boolean isForAchievement) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.url = url;
        this.isForAchievement = isForAchievement;
    }

    private AudioResource(){}

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Time getDuration() {
        return duration;
    }

    public void setDuration(Time duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "AudioFile";
    }
}
