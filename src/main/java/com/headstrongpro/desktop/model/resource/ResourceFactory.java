package com.headstrongpro.desktop.model.resource;

/**
 * Resource Factory
 */
public class ResourceFactory {

    public static Resource getResource(int id, String name, String description, boolean isForAchievement, int type) {
        if (type == 1) {
            return new TextResource(id, name, description, isForAchievement);
        } else if (type == 2) {
            return new ImageResource(id, name, description, isForAchievement);
        } else if (type == 3) {
            return new AudioResource(id, name, description, isForAchievement);
        } else if (type == 4) {
            return new VideoResource(id, name, description, isForAchievement);
        }
        return null;
    }

    public static Resource getResource(String name, String description, boolean isForAchievement, int type) {
        if (type < 1 || type > 4) {
            return null;
        } else if (type == 1) {
            return new TextResource(name, description, isForAchievement);
        } else if (type == 2) {
            return new ImageResource(name, description, isForAchievement);
        } else if (type == 3) {
            return new AudioResource(name, description, isForAchievement);
        } else if (type == 4) {
            return new VideoResource(name, description, isForAchievement);
        }
        return null;
    }
}
