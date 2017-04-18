package com.headstrongpro.desktop.model;

import java.util.ArrayList;

/**
 * ShOp MoDeL
 */
public class Shop {
    public ArrayList<ShopItem> items;

    public Shop(ArrayList<ShopItem> items) {
        this.items = items;
    }

    public ArrayList<ShopItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<ShopItem> items) {
        this.items = items;
    }
}
