package com.creativeshare.registerme.app;

import android.content.Context;

import androidx.multidex.MultiDexApplication;

import com.creativeshare.registerme.language.Language_Helper;
import com.creativeshare.registerme.preferences.Preferences;
import com.google.firebase.FirebaseApp;
import com.telr.mobile.sdk.TelrApplication;


public class App extends  MultiDexApplication{

    private Teler teler;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(Language_Helper.updateResources(base, Preferences.getInstance().getLanguage(base)));
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
         teler=new Teler();
         teler.getContext();
    }




}
