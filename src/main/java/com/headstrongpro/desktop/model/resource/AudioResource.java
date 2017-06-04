package com.headstrongpro.desktop.model.resource;

import java.io.File;
import java.sql.Time;

/**
 * Created by rajmu on 17.05.08.
 */

//TODO: Custom functionality and behaviour to be added!
public class AudioResource implements Resource {

    private static final ResourceType TYPE = ResourceType.AUDIO;

    private int id;
    private String name, description, url;
    private boolean isForAchievement;
    private Time duration;
    private File file;
    private String path;

    AudioResource(String name, String description, boolean isForAchievement) {
        this.name = name;
        this.description = description;
        this.isForAchievement = isForAchievement;
    }

    AudioResource(int id, String name, String description, boolean isForAchievement) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.isForAchievement = isForAchievement;
    }

    private AudioResource() {
    }

    @Override
    public File getFile() {
        return file;
    }

    @Override
    public void setFile(File file) {
        this.file = file;
    }

    @Override
    public String getRemoteFileName() {
        return path;
    }

    @Override
    public void setRemoteFileName(String path) {
        this.path = path;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        String s = String.format("[Name]: %s\n[Description]: %s\n[For Achievement]: %s\n", name, description, String.valueOf(isForAchievement));
        if (id != 0) s += (String.format("[ID]: %d\n", id));
        if (duration != null) s += String.format("[Duration]: %s\n", String.valueOf(duration));
        if (url != null && !url.isEmpty()) s += String.format("[URL]: %s\n", url);
        return s;
    }
}
