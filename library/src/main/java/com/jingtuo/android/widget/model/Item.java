package com.jingtuo.android.widget.model;

import java.util.ArrayList;

/**
 * Created by 28173_000 on 2016/8/28.
 */
public class Item {

    private String key;

    private String value;

    private boolean hasSubItems;

    private ArrayList<Item> subItems;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ArrayList<Item> getSubItems() {
        return subItems;
    }

    public void setSubItems(ArrayList<Item> subItems) {
        this.subItems = subItems;
    }

    public boolean isHasSubItems() {
        return hasSubItems;
    }

    public void setHasSubItems(boolean hasSubItems) {
        this.hasSubItems = hasSubItems;
    }
}
