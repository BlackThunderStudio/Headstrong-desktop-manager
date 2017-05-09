package com.headstrongpro.desktop.model.resource;

/**
 * Created by Rajmund Staniek on 09-May-17.
 */
public interface IUrl {
    boolean check(String url);

    String transform(String url);
}
