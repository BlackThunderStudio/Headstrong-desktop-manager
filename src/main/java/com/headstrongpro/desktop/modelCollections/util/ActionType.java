package com.headstrongpro.desktop.modelCollections.util;

public enum ActionType {
    CREATE("CREATE"),
    UPDATE("UPDATE"),
    DELETE("DELETE");

    private String type;

    ActionType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
