package com.headstrongpro.desktop.model;

/**
 * shop item classss
 */
public class ShopItem {
    private int id;
    private String name, description;
    private double price;
    private ItemCategory itemCategory;

    public ShopItem(int id, String name, String description, double price, ItemCategory itemCategory) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.itemCategory = itemCategory;
    }

    public ShopItem(String name, String description, double price, ItemCategory itemCategory) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.itemCategory = itemCategory;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public ItemCategory getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(ItemCategory itemCategory) {
        this.itemCategory = itemCategory;
    }
}
