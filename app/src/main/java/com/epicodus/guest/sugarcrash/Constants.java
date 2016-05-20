package com.epicodus.guest.sugarcrash;

/**
 * Created by Guest on 5/16/16.
 */
public class Constants {
    public static final String NUTRITIONIX_API_KEY = BuildConfig.NUTRITIONIX_API_KEY;
    public static final String NUTRITIONIX_APP_ID = BuildConfig.NUTRITIONIX_APP_ID;

    public static final String NUTRITIONIX_BASE_URL = "https://api.nutritionix.com/v1_1/";
    public static final String SEARCH_PATH_SEGMENT = "search";
    public static final String BRAND_PATH_SEGMENT = "brand";
    public static final String ITEM_PATH_SEGMENT = "item";
    public static final String SUGARS_FIELD_SEGMENT = "nf_sugars";
    public static final String CALORIES_FIELD_SEGMENT = "nf_calories";

    public static final String APPID_QUERY = "appId";
    public static final String APPKEY_QUERY = "appKey";
    public static final String UPC_QUERY = "upc";
    public static final String FIELDS_QUERY = "fields";
    public static final String RESULTS_QUERY = "results";

    public static final String RESULT_QUANTITY_DEFAULT = "0:20";
    public static final String QUERY_FIELD_DEFAULTS = "item_name,brand_name,item_id";


    public static final String FIREBASE_URL = BuildConfig.FIREBASE_ROOT_URL;
    public static final String FIREBASE_LOCATION_USERS = "users";
    public static final String FIREBASE_PROPERTY_EMAIL = "email";
    public static final String KEY_UID = "UID";
    public static final String FIREBASE_URL_USERS = FIREBASE_URL + "/" + FIREBASE_LOCATION_USERS;
    public static final String FIREBASE_LOCATION_SAVEDFOOD = "savedFood";
    public static final String FIREBASE_URL_SAVEDFOOD = FIREBASE_URL + "/" + FIREBASE_LOCATION_SAVEDFOOD;

    public static final String PREFERENCES_SEARCH_TYPE_KEY = "searchType";
    public static final String KEY_USER_EMAIL = "email";
}
