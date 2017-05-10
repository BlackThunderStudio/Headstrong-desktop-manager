package com.headstrongpro.desktop.model.resource;

/**
 * Created by rajmu on 17.05.08.
 */
public class ResourceFactory {

    private static IUrlChecker urlCheckerhttps = (u) -> u.startsWith("https://");
    private static IUrlChecker urlCheckerWww = u -> u.startsWith("www");
    private static IUrlTransformer urlTransformer = (u) -> {
        if (u.length() <= 2000 && !u.contains("'")) {
            if (urlCheckerhttps.check(u)) {
                return u;
            } else if (urlCheckerWww.check(u)) {
                return "https://" + u;
            } else {
                return "ERROR";
            }
        } else return "ERROR";
    };

    public static Resource getResource(int id, String name, String description, boolean isForAchievement, int type){
        if (type < 1 || type > 4){
            return null;
        } else if (type == 1){
            return new TextResource(id, name, description, isForAchievement);
        } else if (type == 2){
            return new PhotoResource(id, name, description, isForAchievement);
        } else if (type == 3){
            return new AudioResource(id, name, description, isForAchievement);
        } else if (type == 4){
            return new VideoResource(id, name, description, isForAchievement);
        }
        return null;
    }

    public static Resource getResource(String name, String description, boolean isForAchievement, int type){
        if (type < 1 || type > 4){
            return null;
        } else if (type == 1){
            return new TextResource(name, description, isForAchievement);
        } else if (type == 2){
            return new PhotoResource(name, description, isForAchievement);
        } else if (type == 3){
            return new AudioResource(name, description, isForAchievement);
        } else if (type == 4){
            return new VideoResource(name, description, isForAchievement);
        }
        return null;
    }
}
