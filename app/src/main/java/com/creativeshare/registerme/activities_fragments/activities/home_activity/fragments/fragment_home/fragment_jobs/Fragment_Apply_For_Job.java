package com.creativeshare.registerme.activities_fragments.activities.home_activity.fragments.fragment_home.fragment_jobs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.creativeshare.registerme.R;


public class Fragment_Apply_For_Job extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_apply_for_job, container, false);
        initView(view);
        return view;

    }

    private void initView(View view) {



    }
    public static Fragment_Apply_For_Job newInstance() {
        return new Fragment_Apply_For_Job();
    }

}
