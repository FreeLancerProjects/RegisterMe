package com.creativeshare.registerme.activities_fragments.activities.home_activity.fragments.fragment_home.fragment_create_cv;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.creativeshare.registerme.R;


public class Fragment_Edit_Cv extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_cv, container, false);
        initView(view);
        return view;

    }

    private void initView(View view) {



    }
    public static Fragment_Edit_Cv newInstance() {
        return new Fragment_Edit_Cv();
    }

}
