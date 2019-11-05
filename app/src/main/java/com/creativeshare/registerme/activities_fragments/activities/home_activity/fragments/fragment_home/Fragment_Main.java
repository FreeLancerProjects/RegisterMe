package com.creativeshare.registerme.activities_fragments.activities.home_activity.fragments.fragment_home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.creativeshare.registerme.R;


public class Fragment_Main extends Fragment {

    public static Fragment_Main newInstance() {

        return new Fragment_Main();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_main, container, false);
        return view;
    }


}
