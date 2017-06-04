package com.headstrongpro.desktop.model.resource;

import com.headstrongpro.desktop.model.IModel;

import java.io.File;

/**
 * Text Resource
 */
//TODO: Custom functionality and behaviour to be added!
public class TextResource implements Resource, IModel {

    private static final ResourceType TYPE = ResourceType.TEXT;

    private int id;
    private String name, description, content;
    private boolean isForAchievement;
    private File file;
    private String path;

    TextResource(String name, String description, boolean isForAchievement) {
        this.name = name;
        this.description = description;
        this.isForAchievement = isForAchievement;
    }

    TextResource(int id, String name, String description, boolean isForAchievement) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.isForAchievement = isForAchievement;
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
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
