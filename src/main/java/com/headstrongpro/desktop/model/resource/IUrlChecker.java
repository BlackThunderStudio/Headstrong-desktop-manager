package com.headstrongpro.desktop.model.resource;

/**
 * Created by Rajmund Staniek on 09-May-17.
 */
@FunctionalInterface
public interface IUrlChecker {
    boolean check(String url);
}
