package com.creativeshare.registerme.activities_fragments.activities.home_activity.fragments.fragment_home.fragment_jobs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creativeshare.registerme.R;
import com.creativeshare.registerme.activities_fragments.activities.home_activity.activity.Home_Activity;
import com.creativeshare.registerme.adapter.Job_Adapter;
import com.creativeshare.registerme.models.Order_Model;

import java.util.ArrayList;
import java.util.List;


public class Fragment_Private_Sector extends Fragment {
    private RecyclerView rec_job;
    private List<Order_Model.Data> dataList;
    private Job_Adapter job_adapter;
    private Home_Activity home_activity;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_private_sector, container, false);
        initView(view);
        return view;

    }

    private void initView(View view) {
        home_activity=(Home_Activity)getActivity();
        dataList=new ArrayList<>();
        job_adapter=new Job_Adapter(dataList,home_activity,this);
        rec_job=view.findViewById(R.id.recView);
        rec_job.setLayoutManager(new GridLayoutManager(home_activity,3));
        rec_job.setAdapter(job_adapter);
        dataList.add(new Order_Model.Data());
        dataList.add(new Order_Model.Data());
        dataList.add(new Order_Model.Data());
        dataList.add(new Order_Model.Data());
        job_adapter.notifyDataSetChanged();
    }
    public static Fragment_Private_Sector newInstance() {
        return new Fragment_Private_Sector();
    }

}
