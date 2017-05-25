package com.headstrongpro.desktop.core;

/**
 * Created by rajmu on 17.04.06.
 */
public enum Drawables {
    SPLASH_SMALL("/img/splash_360p.png"),
    ICON_REFRESH("/img/icons/cloud-sync-icon.png");

    private String path;

    Drawables(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
