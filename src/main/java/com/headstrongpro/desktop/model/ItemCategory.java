package com.headstrongpro.desktop.model;

/**
 * Item category model
 */
public class ItemCategory {
    private int id;
    private String name;
    private ItemCategory parentCategory;

    public ItemCategory(int id, String name, ItemCategory parentCategory) {
        this.id = id;
        this.name = name;
        this.parentCategory = parentCategory;
    }

    public ItemCategory(String name, ItemCategory parentCategory) {
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

    public ItemCategory getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(ItemCategory parentCategory) {
        this.parentCategory = parentCategory;
    }
}
