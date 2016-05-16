package com.example.guest.sugarcrash.services;

import android.util.Log;

import com.example.guest.sugarcrash.Constants;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by Guest on 5/16/16.
 */
public class NutritionixService {
    public static String TAG = NutritionixService.class.getSimpleName();
    private String API_KEY = Constants.NUTRITIONIX_API_KEY;
    private String APP_ID = Constants.NUTRITIONIX_APP_ID;
    private String BASE_URL = Constants.NUTRITIONIX_BASE_URL;
    private String SEARCH_PATH_SEGMENT = Constants.SEARCH_PATH_SEGMENT;
    private String FIELDS_QUERY = Constants.FIELDS_QUERY;
    private String RESULTS_QUERY = Constants.RESULTS_QUERY;
    private String QUERY_FIELD_DEFAULTS = Constants.QUERY_FIELD_DEFAULTS;
    private String APPID_QUERY = Constants.APPID_QUERY;
    private String APPKEY_QUERY = Constants.APPKEY_QUERY;
    private String RESULT_QUANTITY_DEFAULT = Constants.RESULT_QUANTITY_DEFAULT;
    private String ITEM_PATH_SEGMENT = Constants.ITEM_PATH_SEGMENT;
    private String UPC_QUERY = Constants.UPC_QUERY;

    public void searchFoods(String userSearch, Callback callback){

        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL).newBuilder();
        urlBuilder.addPathSegment(SEARCH_PATH_SEGMENT);
        urlBuilder.addPathSegment(userSearch);
        urlBuilder.addQueryParameter(FIELDS_QUERY, "*");
        urlBuilder.addQueryParameter(RESULTS_QUERY, RESULT_QUANTITY_DEFAULT);
        urlBuilder.addQueryParameter(APPID_QUERY, APP_ID);
        urlBuilder.addQueryParameter(APPKEY_QUERY, API_KEY);

        String url = urlBuilder.build().toString();
        Request request = new Request.Builder().url(url).build();
        Log.v(TAG, "search url: " + request);

        Call call = client.newCall(request);
        call.enqueue(callback);

    }

    public void searchUPC(String upc, Callback callback){
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL).newBuilder();
        urlBuilder.addPathSegment(ITEM_PATH_SEGMENT);
        urlBuilder.addQueryParameter(UPC_QUERY, upc);
        urlBuilder.addQueryParameter(APPID_QUERY, APP_ID);
        urlBuilder.addQueryParameter(APPKEY_QUERY, API_KEY);

        String url = urlBuilder.build().toString();
        Request request = new Request.Builder().url(url).build();
        Log.v(TAG, "search url: " + request);

        Call call = client.newCall(request);
        call.enqueue(callback);
    }
}
