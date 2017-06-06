package com.headstrongpro.desktop.model.resource;

/**
 * Resource Type
 */
public enum ResourceType {
    TEXT(1),
    IMAGE(2),
    AUDIO(3),
    VIDEO(4),
    ANY(-1);

    private int val;

    ResourceType(int val) {
        this.val = val;
    }

    public int get() {
        return val;
    }
}
