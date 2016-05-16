package com.example.guest.sugarcrash.ui;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.guest.sugarcrash.Constants;
import com.example.guest.sugarcrash.R;
import com.firebase.client.Firebase;

public class BaseActivity extends AppCompatActivity {
    public SharedPreferences mSharedPreferences;
    public SharedPreferences.Editor mSharedPreferencesEditor;
    public Firebase mFirebaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mSharedPreferencesEditor = mSharedPreferences.edit();
        mFirebaseRef = new Firebase(Constants.FIREBASE_URL);
    }

    public void addSearchTypeToSharedPreferences(String searchType){
        mSharedPreferencesEditor.putString(Constants.PREFERENCES_SEARCH_TYPE_KEY, searchType).apply();
    }
}
