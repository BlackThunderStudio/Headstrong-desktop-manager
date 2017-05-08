package com.headstrongpro.desktop.model.resource;

/**
 * Created by rajmu on 17.05.08.
 */
public class ResourceFactory {

    public Resource getResource(ResourceType resourceType, int id, int type, String name, String description, String url, boolean isForAchievement){
        if (resourceType == null){
            return null;
        } else if (resourceType.equals(ResourceType.TEXT)){
            return new TextResource(id, type, name, description, url, isForAchievement);
        } else if (resourceType.equals(ResourceType.PHOTO)){
            return new PhotoResource(id, type, name, description, url, isForAchievement);
        } else if (resourceType.equals(ResourceType.AUDIO)){
            return new AudioResource(id, type, name, description, url, isForAchievement);
        } else if (resourceType.equals(ResourceType.VIDEO)){
            return new VideoResource(id, type, name, description, url, isForAchievement);
        }
        return null;
    }
}

enum ResourceType{
    TEXT(1),
    PHOTO(2),
    AUDIO(3),
    VIDEO(4);

    private int id;
    ResourceType(int id){
        this.id = id;
    }

    int getId(){
        return id;
    }
}
