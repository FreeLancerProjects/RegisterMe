package com.creativeshare.registerme.app;

import android.app.Application;
import android.content.Context;

import com.creativeshare.registerme.language.Language_Helper;
import com.creativeshare.registerme.preferences.Preferences;
import com.google.firebase.FirebaseApp;


public class App extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(Language_Helper.updateResources(base, Preferences.getInstance().getLanguage(base)));
    }

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseApp.initializeApp(this);
    }
}
