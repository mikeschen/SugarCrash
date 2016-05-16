package com.example.guest.sugarcrash;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by Guest on 5/16/16.
 */
public class SugarCrashApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
