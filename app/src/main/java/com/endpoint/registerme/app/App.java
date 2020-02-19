package com.endpoint.registerme.app;

import android.content.Context;
import android.content.Intent;

import androidx.multidex.MultiDexApplication;

import com.endpoint.registerme.language.Language_Helper;
import com.endpoint.registerme.preferences.Preferences;
import com.google.firebase.FirebaseApp;


public class App extends  MultiDexApplication{

//    private Teler teler;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(Language_Helper.updateResources(base, Preferences.getInstance().getLanguage(base)));
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        Intent intent=new Intent(getApplicationContext(),Teler.class);
        startService(intent);
  //startActivity(intent);

    }

   }
