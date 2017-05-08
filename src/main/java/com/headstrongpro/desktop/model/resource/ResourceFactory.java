package com.headstrongpro.desktop.model.resource;

/**
 * Created by rajmu on 17.05.08.
 */
public class ResourceFactory {

    public Resource getResource(int id, String name, String description, String url, boolean isForAchievement, int type){
        if (type < 1 || type > 4){
            return null;
        } else if (type == 1){
            return new TextResource(id, type, name, description, url, isForAchievement);
        } else if (type == 2){
            return new PhotoResource(id, type, name, description, url, isForAchievement);
        } else if (type == 3){
            return new AudioResource(id, type, name, description, url, isForAchievement);
        } else if (type == 4){
            return new VideoResource(id, type, name, description, url, isForAchievement);
        }
        return null;
    }

    public Resource getResource(String name, String description, String url, boolean isForAchievement, int type){
        if (type < 1 || type > 4){
            return null;
        } else if (type == 1){
            return new TextResource(type, name, description, url, isForAchievement);
        } else if (type == 2){
            return new PhotoResource(type, name, description, url, isForAchievement);
        } else if (type == 3){
            return new AudioResource(type, name, description, url, isForAchievement);
        } else if (type == 4){
            return new VideoResource(type, name, description, url, isForAchievement);
        }
        return null;
    }
}
