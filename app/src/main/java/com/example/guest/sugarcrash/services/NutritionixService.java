package com.example.guest.sugarcrash.services;

import android.util.Log;

import com.example.guest.sugarcrash.Constants;
import com.example.guest.sugarcrash.models.Food;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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

    public ArrayList<Food> processResults(Response response) {
        ArrayList<Food> foods = new ArrayList<>();

        try {
            String jsonData = response.body().string();
            if(response.isSuccessful()) {
                JSONObject nutritionJSON  = new JSONObject(jsonData);
                JSONArray hitsArrayJSON = nutritionJSON.getJSONArray("hits");
                for (int i = 0; i < hitsArrayJSON.length(); i++  ){
                    JSONObject foodsJSON = hitsArrayJSON.getJSONObject(i).getJSONObject("fields");
                    String itemId = foodsJSON.getString("item_id");
                    String itemName = foodsJSON.getString("item_name");
                    String brandName = foodsJSON.getString("brand_name");
                    String itemDescription = foodsJSON.getString("item_description");
                    double calories = foodsJSON.getDouble("nf_calories");
                    double totalFat = foodsJSON.getDouble("nf_total_fat");
                    double saturatedFat = foodsJSON.optDouble("nf_saturated_fat", 0);
                    double polyunsaturatedFat = foodsJSON.optDouble("nf_polyunsaturated_fat", 0);
                    double monounsaturatedFat = foodsJSON.optDouble("nf_monounsaturated_fat", 0);
                    double cholesterol = foodsJSON.optDouble("nf_cholesterol", 0);
                    double sodium = foodsJSON.getDouble("nf_sodium");
                    double sugars = foodsJSON.optDouble("nf_sugars", 0);
                    double servingsPerContainer = foodsJSON.optDouble("nf_servings_per_container", 0);
                    double servingSizeQuantity = foodsJSON.optDouble("nf_serving_size_qty", 0);
                    String servingSizeUnit = foodsJSON.getString("nf_serving_size_unit");
                    double servingWeightGrams = foodsJSON.optDouble("nf_serving_weight_grams", 0);
                    double protein = foodsJSON.getDouble("nf_protein");
                    Food food = new Food(itemId, itemName, brandName, itemDescription, calories, totalFat, saturatedFat, polyunsaturatedFat, monounsaturatedFat, cholesterol, sodium, sugars, servingsPerContainer, servingSizeQuantity, servingSizeUnit, servingWeightGrams, protein);
                    foods.add(food);
                }


            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return foods;
    }
}
