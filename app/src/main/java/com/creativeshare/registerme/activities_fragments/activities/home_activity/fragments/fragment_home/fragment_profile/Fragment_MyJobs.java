package com.creativeshare.registerme.activities_fragments.activities.home_activity.fragments.fragment_home.fragment_profile;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.creativeshare.registerme.R;
import com.creativeshare.registerme.activities_fragments.activities.home_activity.activity.Home_Activity;
import com.creativeshare.registerme.activities_fragments.activities.home_activity.fragments.fragment_home.fragment_jobs.Fragment_Apply_For_Job;
import com.creativeshare.registerme.models.UserModel;
import com.creativeshare.registerme.preferences.Preferences;


public class Fragment_MyJobs extends Fragment {

private Home_Activity home_activity;
private LinearLayout ll_no_store;
    private ProgressBar progBar;
    private int company_id=-1;
    private Preferences preferences;
    private UserModel userModel;
    public static Fragment_MyJobs newInstance() {
        return new Fragment_MyJobs();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_jobs, container, false);
        initView(view);
        return view;

    }

    private void initView(View view) {
home_activity=(Home_Activity)getActivity();
        preferences= Preferences.getInstance();
        userModel=preferences.getUserData(home_activity);
ll_no_store=view.findViewById(R.id.ll_no_store);
        progBar = view.findViewById(R.id.progBar);

        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(home_activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);



    }



}
