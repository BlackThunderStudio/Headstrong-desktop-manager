package com.headstrongpro.desktop.model;

/**
 * Achievement mdel
 */
public class Achievement {
    private int id;
    private String name, description;
    private AchievementCategory achievementCategory;

    public Achievement(int id, String name, String description, AchievementCategory achievementCategory) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.achievementCategory = achievementCategory;
    }

    public Achievement(String name, String description, AchievementCategory achievementCategory) {
        this.name = name;
        this.description = description;
        this.achievementCategory = achievementCategory;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AchievementCategory getAchievementCategory() {
        return achievementCategory;
    }

    public void setAchievementCategory(AchievementCategory achievementCategory) {
        this.achievementCategory = achievementCategory;
    }
}
