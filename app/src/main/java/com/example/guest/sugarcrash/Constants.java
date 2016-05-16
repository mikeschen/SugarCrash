package com.example.guest.sugarcrash;

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

}
