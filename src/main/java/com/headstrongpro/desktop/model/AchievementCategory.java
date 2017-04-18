package com.headstrongpro.desktop.model;

/**
 * achievement category model
 */
public class AchievementCategory {
    private int id;
    private String name;
    private AchievementCategory parentCategory;

    public AchievementCategory(int id, String name, AchievementCategory parentCategory) {
        this.id = id;
        this.name = name;
        this.parentCategory = parentCategory;
    }

    public AchievementCategory(String name, AchievementCategory parentCategory) {
        this.name = name;
        this.parentCategory = parentCategory;
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

    public AchievementCategory getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(AchievementCategory parentCategory) {
        this.parentCategory = parentCategory;
    }
}
