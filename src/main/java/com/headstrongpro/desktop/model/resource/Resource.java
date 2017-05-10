package com.headstrongpro.desktop.model.resource;

/**
 * Created by rajmu on 17.05.08.
 */
public interface Resource {
    String getDescription();
    String getName();
    boolean isForAchievement();
    int getType();
    int getID();

    void setID(int id);

    static <T extends Resource> T as(Resource resource, Class<T> type){
        return type.cast(resource);
    }
}
