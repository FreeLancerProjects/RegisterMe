package com.creativeshare.registerme.activities_fragments.activities.home_activity.fragments.fragment_home;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.creativeshare.registerme.R;


public class Fragment_MyOrders extends Fragment {



    public Fragment_MyOrders() {
        // Required empty public constructor
    }

    public static Fragment_MyOrders newInstance() {

        return new Fragment_MyOrders();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_myorders, container, false);
        return view;
    }


}
