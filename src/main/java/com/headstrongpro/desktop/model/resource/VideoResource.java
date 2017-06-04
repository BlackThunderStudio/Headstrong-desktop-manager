package com.headstrongpro.desktop.model.resource;

import com.headstrongpro.desktop.model.IModel;

import java.io.File;
import java.sql.Time;

/**
 * Video Resource
 */
//TODO: Custom functionality and behaviour to be added!
public class VideoResource implements Resource, IModel {

    private static final ResourceType TYPE = ResourceType.VIDEO;

    private int id;
    private String name, description, url;
    private boolean isForAchievement;
    private Time duration;
    private File file;
    private String path;

    VideoResource(String name, String description, boolean isForAchievement) {
        this.name = name;
        this.description = description;
        this.isForAchievement = isForAchievement;
    }

    VideoResource(int id, String name, String description, boolean isForAchievement) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.isForAchievement = isForAchievement;
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
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
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
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }
}
