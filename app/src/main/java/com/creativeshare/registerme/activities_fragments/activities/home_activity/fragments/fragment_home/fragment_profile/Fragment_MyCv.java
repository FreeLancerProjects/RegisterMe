package com.creativeshare.registerme.activities_fragments.activities.home_activity.fragments.fragment_home.fragment_profile;

import android.app.ProgressDialog;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creativeshare.registerme.R;
import com.creativeshare.registerme.activities_fragments.activities.home_activity.activity.Home_Activity;
import com.creativeshare.registerme.adapter.Government_Job_Adapter;
import com.creativeshare.registerme.models.AllInFo_Model;
import com.creativeshare.registerme.models.UserModel;
import com.creativeshare.registerme.preferences.Preferences;
import com.creativeshare.registerme.remote.Api;
import com.creativeshare.registerme.share.Common;
import com.creativeshare.registerme.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Fragment_MyCv extends Fragment {

private Home_Activity home_activity;
private LinearLayout ll_no_store;
    private ProgressBar progBar;
    private int company_id=-1;
    private Preferences preferences;
    private UserModel userModel;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_cv, container, false);
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
    public static Fragment_MyCv newInstance() {
        return new Fragment_MyCv();
    }



}
