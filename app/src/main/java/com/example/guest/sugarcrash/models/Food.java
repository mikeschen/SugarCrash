package com.example.guest.sugarcrash.models;

/**
 * Created by Guest on 5/17/16.
 */
public class Food {
    String itemId;
    String itemName;
    String brandName;
    String itemDescription;
    double calories;
    double totalFat;
    double saturatedFat;
    double polyunsaturatedFat;
    double monounsaturatedFat;
    double cholesterol;
    double sodium;
    double sugars;
    double servingsPerContainer;
    double servingSizeQuantity;
    String servingSizeUnit;
    double servingWeightGrams;
    double protein;
    String pushId;

    public Food() {}

    public Food(String itemId, String itemName, String brandName, String itemDescription, double calories, double totalFat, double saturatedFat, double polyunsaturatedFat, double monounsaturatedFat, double cholesterol, double sodium, double sugars, double servingsPerContainer, double servingSizeQuantity, String servingSizeUnit, double servingWeightGrams, double protein) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.brandName = brandName;
        this.itemDescription = itemDescription;
        this.calories = calories;
        this.totalFat = totalFat;
        this.saturatedFat = saturatedFat;
        this.polyunsaturatedFat = polyunsaturatedFat;
        this.monounsaturatedFat = monounsaturatedFat;
        this.cholesterol = cholesterol;
        this.sodium = sodium;
        this.sugars = sugars;
        this.servingsPerContainer = servingsPerContainer;
        this.servingSizeQuantity = servingSizeQuantity;
        this.servingSizeUnit = servingSizeUnit;
        this.servingWeightGrams = servingWeightGrams;
        this.protein = protein;
    }

    public String getItemId() {
        return itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public String getBrandName() {
        return brandName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public double getCalories() {
        return calories;
    }

    public double getTotalFat() {
        return totalFat;
    }

    public double getSaturatedFat() {
        return saturatedFat;
    }

    public double getPolyunsaturatedFat() {
        return polyunsaturatedFat;
    }

    public double getMonoUnsaturatedFat() {
        return monounsaturatedFat;
    }

    public double getCholesterol() {
        return cholesterol;
    }

    public double getSodium() {
        return sodium;
    }

    public double getSugars() {
        return sugars;
    }

    public double getServingsPerContainer() {
        return servingsPerContainer;
    }

    public double getServingSizeQuantity() {
        return servingSizeQuantity;
    }

    public String getServingSizeUnit() {
        return servingSizeUnit;
    }

    public double getServingWeightGrams() {
        return servingWeightGrams;
    }

    public double getProtein() {
        return protein;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }
}
