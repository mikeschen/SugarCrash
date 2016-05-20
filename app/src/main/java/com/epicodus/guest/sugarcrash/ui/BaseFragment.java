package com.epicodus.guest.sugarcrash.ui;


import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;

import com.epicodus.guest.sugarcrash.Constants;
import com.firebase.client.Firebase;

/**
 * A simple {@link Fragment} subclass.
 */
public class BaseFragment extends Fragment {

    public SharedPreferences mSharedPreferences;
    public SharedPreferences.Editor mSharedPreferencesEditor;
    public Firebase mFirebaseRef;
    public String mUId;
    public Typeface myCustomFont;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mSharedPreferencesEditor = mSharedPreferences.edit();
        mFirebaseRef = new Firebase(Constants.FIREBASE_URL);
        mUId = mSharedPreferences.getString(Constants.KEY_UID, null);
        myCustomFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SpicyRice-Regular.ttf");
    }
}
