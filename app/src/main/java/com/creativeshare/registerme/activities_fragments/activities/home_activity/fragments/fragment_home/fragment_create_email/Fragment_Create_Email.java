package com.creativeshare.registerme.activities_fragments.activities.home_activity.fragments.fragment_home.fragment_create_email;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.creativeshare.registerme.R;


public class Fragment_Create_Email extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_email, container, false);
        initView(view);
        return view;

    }

    private void initView(View view) {



    }
    public static Fragment_Create_Email newInstance() {
        return new Fragment_Create_Email();
    }


}
