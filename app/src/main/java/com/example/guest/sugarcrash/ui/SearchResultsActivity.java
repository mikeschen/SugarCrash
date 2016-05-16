package com.example.guest.sugarcrash.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.guest.sugarcrash.Constants;
import com.example.guest.sugarcrash.R;
import com.example.guest.sugarcrash.services.NutritionixService;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SearchResultsActivity extends BaseActivity {
    private static final String TAG = SearchResultsActivity.class.getSimpleName();
    private String mSearchString;
    private String mSearchType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        Intent intent = getIntent();
        mSearchString = intent.getStringExtra("inputText");
        mSearchType = mSharedPreferences.getString(Constants.PREFERENCES_SEARCH_TYPE_KEY, null);
        if(mSearchType != null && mSearchType.equals("string")){
            searchDatabaseByTerm();
        } else if(mSearchType != null && mSearchType.equals("upc")){
            Log.v(TAG, mSearchString + " " + mSearchType);
        }
    }

    private void searchDatabaseByTerm(){
        final NutritionixService nutritionixService = new NutritionixService();
        nutritionixService.searchFoods(mSearchString, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try{
                    String jsonData = response.body().string();
                    if(response.isSuccessful()){
                        Log.v(TAG, jsonData);
                    }
                } catch(IOException e){
                    e.printStackTrace();
                }
            }
        });
    }
}
