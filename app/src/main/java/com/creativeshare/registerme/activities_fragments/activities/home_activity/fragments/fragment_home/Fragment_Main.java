package com.creativeshare.registerme.activities_fragments.activities.home_activity.fragments.fragment_home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.creativeshare.registerme.R;
import com.creativeshare.registerme.activities_fragments.activities.home_activity.activity.Home_Activity;
import com.creativeshare.registerme.adapter.CategoryAdapter;
import com.creativeshare.registerme.adapter.SlidingImage_Adapter;
import com.creativeshare.registerme.models.CategoryModel;
import com.creativeshare.registerme.models.Slider_Model;
import com.google.android.material.tabs.TabLayout;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.Pivot;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;


public class Fragment_Main extends Fragment {
private DiscreteScrollView discreteScrollView;
    private List<CategoryModel> categoryModelList;
private Home_Activity activity;
    private CategoryAdapter adapter;
    private int current_page = 0, NUM_PAGES;
    private ViewPager mPager;
    private TabLayout indicator;
private List<Slider_Model.Data>dataList;
    private SlidingImage_Adapter slidingImage__adapter;

    public static Fragment_Main newInstance() {

        return new Fragment_Main();
    }


    private void updateUI() {
        categoryModelList.add(new CategoryModel(R.drawable.ic_cv, getString(R.string.Create_Cv)));
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
        change_slide_image();
        return view;
    }
    private void change_slide_image() {
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (current_page == NUM_PAGES) {
                    current_page = 0;
                }
                mPager.setCurrentItem(current_page++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);
    }

    private void initview(View view) {
        dataList=new ArrayList<>();
        categoryModelList=new ArrayList<>();
        activity=(Home_Activity)getActivity(); 
        discreteScrollView=view.findViewById(R.id.discreteScrollView);
        mPager = view.findViewById(R.id.pager);
        indicator = view.findViewById(R.id.tabLayout);
        dataList.add(new Slider_Model.Data());
        dataList.add(new Slider_Model.Data());

        dataList.add(new Slider_Model.Data());

        slidingImage__adapter = new SlidingImage_Adapter(activity, dataList);
        mPager.setAdapter(slidingImage__adapter);
        indicator.setupWithViewPager(mPager);
        discreteScrollView.setItemTransformer(new ScaleTransformer.Builder()
                .setMaxScale(1.0f)
                .setMinScale(0.75f)
                .setPivotX(Pivot.X.CENTER)
                .setPivotY(Pivot.Y.BOTTOM)
                .build());
    }




}
