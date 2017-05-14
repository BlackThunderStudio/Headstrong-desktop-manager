package com.headstrongpro.desktop.model.resource;

import static com.headstrongpro.desktop.model.resource.ResourceType.*;

/**
 * Created by rajmu on 17.05.08.
 */
public interface Resource {
    String getDescription();
    String getName();
    boolean isForAchievement();
    ResourceType getType();
    int getID();

    void setID(int id);

    @Deprecated
    static <T extends Resource> T ofType(Resource resource, Class<T> type){
        return type.cast(resource);
    }

    static <T extends Resource> T ofType(Resource resource){
        if(resource.getType().equals(TEXT)){
            return (T) TextResource.class.cast(resource);
        } else if(resource.getType().equals(IMAGE)){
            return (T) ImageResource.class.cast(resource);
        } else if(resource.getType().equals(AUDIO)){
            return (T) AudioResource.class.cast(resource);
        } else if (resource.getType().equals(VIDEO)){
            return (T) VideoResource.class.cast(resource);
        }
        return null;
    }
}
