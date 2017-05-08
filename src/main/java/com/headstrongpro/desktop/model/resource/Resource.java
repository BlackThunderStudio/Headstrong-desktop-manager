package com.headstrongpro.desktop.model.resource;

/**
 * Created by rajmu on 17.05.08.
 */
public interface Resource {
    String getURL();
    String getDescription();
    String getName();
    boolean isForAchievement();
    int getType();
    int getID();
}
