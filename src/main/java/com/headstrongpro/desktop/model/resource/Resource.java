package com.headstrongpro.desktop.model.resource;

import java.io.File;

import static com.headstrongpro.desktop.model.resource.ResourceType.*;

/**
 * Resource Interface
 */
public interface Resource {
    @Deprecated
    static <T extends Resource> T ofType(Resource resource, Class<T> type) {
        return type.cast(resource);
    }

    @SuppressWarnings("unchecked")
    static <T extends Resource> T ofType(Resource resource) {
        if (resource.getType().equals(TEXT)) {
            return (T) TextResource.class.cast(resource);
        } else if (resource.getType().equals(IMAGE)) {
            return (T) ImageResource.class.cast(resource);
        } else if (resource.getType().equals(AUDIO)) {
            return (T) AudioResource.class.cast(resource);
        } else if (resource.getType().equals(VIDEO)) {
            return (T) VideoResource.class.cast(resource);
        }
        return null;
    }

    String getDescription();

    String getName();

    void setName(String name);

    boolean isForAchievement();

    ResourceType getType();

    int getId();

    void setId(int id);

    void setDescription(String description);

    File getFile();

    void setFile(File file);

    String getRemoteFileName();

    void setRemoteFileName(String path);
}
