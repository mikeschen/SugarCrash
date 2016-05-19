package com.example.guest.sugarcrash.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import com.example.guest.sugarcrash.R;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.guest.sugarcrash.Constants;
import com.example.guest.sugarcrash.adapters.FoodListAdapter;
import com.example.guest.sugarcrash.models.Food;
import com.example.guest.sugarcrash.services.NutritionixService;
import com.example.guest.sugarcrash.util.EndlessRecyclerViewScrollListener;

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
    private ProgressDialog mAuthProgressDialog;

    public ArrayList<Food> mFoods = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        ButterKnife.bind(this);
        mAuthProgressDialog = new ProgressDialog(this);
        mAuthProgressDialog.setTitle("Loading...");
        mAuthProgressDialog.setMessage("Searching for food items...");
        mAuthProgressDialog.setCancelable(false);
    Intent intent = getIntent();
        mSearchString = intent.getStringExtra("inputText");
        mSearchType = mSharedPreferences.getString(Constants.PREFERENCES_SEARCH_TYPE_KEY, null);
        if(mSearchType != null && mSearchType.equals("string")){
            searchDatabaseByTerm();
        } else if(mSearchType != null && mSearchType.equals("upc") && mSearchString != null){
            searchDatabaseByUpc();
        }
        mAuthProgressDialog.show();
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
                        LinearLayoutManager layoutManager = new LinearLayoutManager(SearchResultsActivity.this);
                        mSearchResultsRecyclerView.setLayoutManager(layoutManager);
                        mSearchResultsRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
                            @Override
                            public void onLoadMore(int page, int totalItemsCount) {
                                loadMoreFromNutritionix(totalItemsCount);
                            }
                        });
                        mAuthProgressDialog.dismiss();
                    }
                });
            }
        });
    }

    public void loadMoreFromNutritionix(int offset){
        final NutritionixService nutritionixService = new NutritionixService();
        nutritionixService.moreSearchFoods(mSearchString, offset, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                mFoods.addAll(nutritionixService.processResults(response));

                SearchResultsActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int currentSize = mAdapter.getItemCount();
                        mAdapter.notifyItemRangeInserted(currentSize, mFoods.size() - 1);
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
                        if (mFoods == null) {
                            mAuthProgressDialog.dismiss();
                            Toast.makeText(SearchResultsActivity.this, "Food Item Not Found", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(SearchResultsActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                        else {
                            mAdapter = new FoodListAdapter(getApplicationContext(), mFoods);
                            mSearchResultsRecyclerView.setAdapter(mAdapter);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(SearchResultsActivity.this);
                            mSearchResultsRecyclerView.setLayoutManager(layoutManager);
                            mSearchResultsRecyclerView.setHasFixedSize(true);
                        }
                    }
                });
            }
        });
    }
}
