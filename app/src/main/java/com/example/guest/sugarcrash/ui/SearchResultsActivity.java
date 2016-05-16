package com.example.guest.sugarcrash.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.guest.sugarcrash.Constants;
import com.example.guest.sugarcrash.R;

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
        Log.v(TAG, mSearchString + " " + mSearchType);
    }
}
