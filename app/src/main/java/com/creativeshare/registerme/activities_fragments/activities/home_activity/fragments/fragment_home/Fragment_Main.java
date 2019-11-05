package com.creativeshare.registerme.activities_fragments.activities.home_activity.fragments.fragment_home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.creativeshare.registerme.R;
import com.creativeshare.registerme.activities_fragments.activities.home_activity.activity.Home_Activity;
import com.creativeshare.registerme.adapter.CategoryAdapter;
import com.creativeshare.registerme.models.CategoryModel;
import com.yarolegovich.discretescrollview.DiscreteScrollView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


public class Fragment_Main extends Fragment {
private DiscreteScrollView discreteScrollView;
    private List<CategoryModel> categoryModelList;
private Home_Activity activity;
    private CategoryAdapter adapter;

    public static Fragment_Main newInstance() {

        return new Fragment_Main();
    }


    private void updateUI() {
        categoryModelList.add(new CategoryModel(R.drawable.ic_cv, getString(R.string.create_cv)));
        categoryModelList.add(new CategoryModel(R.drawable.ic_email3, getString(R.string.create_email)));
        categoryModelList.add(new CategoryModel(R.drawable.ic_team, getString(R.string.jobs)));
 
        adapter = new CategoryAdapter(categoryModelList,activity ,this);
        discreteScrollView.setAdapter(adapter);

        new Handler()
                .postDelayed(() -> {
               discreteScrollView.scrollToPosition(1);
                   
                }, 500);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_main, container, false);
        initview(view);
        updateUI();
        return view;
    }

    private void initview(View view) {
        categoryModelList=new ArrayList<>();
        activity=(Home_Activity)getActivity(); 
        discreteScrollView=view.findViewById(R.id.discreteScrollView);
    }




}
