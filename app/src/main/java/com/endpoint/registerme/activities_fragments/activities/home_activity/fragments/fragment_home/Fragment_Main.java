package com.endpoint.registerme.activities_fragments.activities.home_activity.fragments.fragment_home;

import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.endpoint.registerme.R;
import com.endpoint.registerme.activities_fragments.activities.home_activity.activity.Home_Activity;
import com.endpoint.registerme.adapter.CategoryAdapter;
import com.endpoint.registerme.adapter.SlidingImage_Adapter;
import com.endpoint.registerme.models.CategoryModel;
import com.endpoint.registerme.models.Slider_Model;
import com.endpoint.registerme.remote.Api;
import com.endpoint.registerme.tags.Tags;
import com.google.android.material.tabs.TabLayout;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.Pivot;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


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
    private ProgressBar progBarAds;

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
        get_slider();
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
    private void get_slider() {
        Api.getService(Tags.base_url).get_slider().enqueue(new Callback<Slider_Model>() {
            @Override
            public void onResponse(Call<Slider_Model> call, Response<Slider_Model> response) {
                progBarAds.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    if (response.body().getData().size() > 0) {
                        NUM_PAGES = response.body().getData().size();
                        slidingImage__adapter = new SlidingImage_Adapter(activity, response.body().getData());
                        mPager.setAdapter(slidingImage__adapter);
                        indicator.setupWithViewPager(mPager);
                    } else {
                        //    error.setText("No data Found");
                        // recc.setVisibility(View.GONE);
                        mPager.setVisibility(View.GONE);
                    }
                } else if (response.code() == 404) {
                    //  error.setText(activity.getString(R.string.no_data));
                    //recc.setVisibility(View.GONE);
                    mPager.setVisibility(View.GONE);
                } else {
                    // recc.setVisibility(View.GONE);
                    mPager.setVisibility(View.GONE);
                    try {
                        Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<Slider_Model> call, Throwable t) {
                try {
                    Log.e("Error", t.getMessage());
                    progBarAds.setVisibility(View.GONE);
                    //error.setText(activity.getString(R.string.faild));
                    //recc.setVisibility(View.GONE);
                    mPager.setVisibility(View.GONE);
                } catch (Exception e) {

                }

            }
        });

    }

    private void initview(View view) {
        dataList=new ArrayList<>();
        categoryModelList=new ArrayList<>();
        activity=(Home_Activity)getActivity(); 
        discreteScrollView=view.findViewById(R.id.discreteScrollView);
        mPager = view.findViewById(R.id.pager);
        indicator = view.findViewById(R.id.tabLayout);
        progBarAds = view.findViewById(R.id.progBarSlider);
        progBarAds.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);



        discreteScrollView.setItemTransformer(new ScaleTransformer.Builder()
                .setMaxScale(1.0f)
                .setMinScale(0.75f)
                .setPivotX(Pivot.X.CENTER)
                .setPivotY(Pivot.Y.BOTTOM)
                .build());
    }




}
