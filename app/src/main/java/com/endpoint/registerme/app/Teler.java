package com.endpoint.registerme.app;

import android.content.Context;
import android.util.Log;

import com.telr.mobile.sdk.TelrApplication;

public  class  Teler extends TelrApplication  {


    private static Context context;

    public void onCreate(){
        super.onCreate();
        Log.d("Demo","Context Started....");
        Teler.context = getApplicationContext();
//         teler=new Teler();
//         teler.getContext();




    }

    public  static Context getContext(){


        return Teler.context;
    }}
