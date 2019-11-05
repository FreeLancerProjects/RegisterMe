package com.creativeshare.registerme.activities_fragments.activities.home_activity.fragments.fragment_home;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.creativeshare.registerme.R;



public class Fragment_Notification extends Fragment {

    public static Fragment_Notification newInstance() {

        return new Fragment_Notification();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_notification, container, false);
        return view;
    }


}
