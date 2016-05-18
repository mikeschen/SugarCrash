package com.example.guest.sugarcrash.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import com.example.guest.sugarcrash.R;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.guest.sugarcrash.Constants;
import com.example.guest.sugarcrash.adapters.FoodListAdapter;
import com.example.guest.sugarcrash.models.Food;
import com.example.guest.sugarcrash.services.NutritionixService;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SearchResultsActivity extends BaseActivity {
    private static final String TAG = SearchResultsActivity.class.getSimpleName();
    @Bind(R.id.searchResultsRecyclerView) RecyclerView mSearchResultsRecyclerView;
    private FoodListAdapter mAdapter;
    private String mSearchString;
    private String mSearchType;

    public ArrayList<Food> mFoods = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        mSearchString = intent.getStringExtra("inputText");
        mSearchType = mSharedPreferences.getString(Constants.PREFERENCES_SEARCH_TYPE_KEY, null);
        if(mSearchType != null && mSearchType.equals("string")){
            searchDatabaseByTerm();
        } else if(mSearchType != null && mSearchType.equals("upc") && mSearchString != null){
            Log.v(TAG, mSearchString + " " + mSearchType);
            searchDatabaseByUpc();
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
                mFoods = nutritionixService.processResults(response);

                SearchResultsActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter = new FoodListAdapter(getApplicationContext(), mFoods);
                        mSearchResultsRecyclerView.setAdapter(mAdapter);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(SearchResultsActivity.this);
                        mSearchResultsRecyclerView.setLayoutManager(layoutManager);
                        mSearchResultsRecyclerView.setHasFixedSize(true);
                    }
                });
            }
        });
    }

    private void searchDatabaseByUpc(){
        final NutritionixService nutritionixService = new NutritionixService();
        nutritionixService.searchUPC(mSearchString, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                mFoods = nutritionixService.processResultsUpc(response);

                SearchResultsActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter = new FoodListAdapter(getApplicationContext(), mFoods);
                        mSearchResultsRecyclerView.setAdapter(mAdapter);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(SearchResultsActivity.this);
                        mSearchResultsRecyclerView.setLayoutManager(layoutManager);
                        mSearchResultsRecyclerView.setHasFixedSize(true);
                    }
                });
            }
        });
    }
}
