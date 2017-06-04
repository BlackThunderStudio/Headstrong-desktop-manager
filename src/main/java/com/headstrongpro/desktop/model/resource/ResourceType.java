package com.headstrongpro.desktop.model.resource;

/**
 * Created by rajmu on 17.05.13.
 */
public enum ResourceType {
    TEXT(1),
    IMAGE(2),
    AUDIO(3),
    VIDEO(4);

    private int val;

    ResourceType(int val) {
        this.val = val;
    }

    public int get() {
        return val;
    }
}
