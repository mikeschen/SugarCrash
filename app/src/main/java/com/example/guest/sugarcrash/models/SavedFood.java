package com.example.guest.sugarcrash.models;

/**
 * Created by Guest on 5/18/16.
 */
public class SavedFood {

    String itemName;
    String brandName;
    double sugars;
    int date;
    String pushId;

    public SavedFood() {}

    public SavedFood(String itemName, String brandName, double sugars, int date) {
        this.itemName = itemName;
        this.brandName = brandName;
        this.sugars = sugars;
        this.date = date;
    }



    public String getItemName() {
        return itemName;
    }

    public String getBrandName() {
        return brandName;
    }

    public double getSugars() {
        return sugars;
    }

    public int getDate() {
        return date;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }
}

