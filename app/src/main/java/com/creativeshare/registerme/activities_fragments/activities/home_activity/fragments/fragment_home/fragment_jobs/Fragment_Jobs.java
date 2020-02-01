package com.creativeshare.registerme.activities_fragments.activities.home_activity.fragments.fragment_home.fragment_jobs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.creativeshare.registerme.R;
import com.creativeshare.registerme.activities_fragments.activities.home_activity.activity.Home_Activity;
import com.creativeshare.registerme.adapter.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;


public class Fragment_Jobs extends Fragment {
    private Home_Activity homeActivity;
    private String cuurent_language;
    private TabLayout tab;
    private ViewPager pager;
    private ImageView im_back;
    private ViewPagerAdapter adapter;
    private List<Fragment> fragmentList;
    private List<String> titleList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_jobs, container, false);
        initView(view);
        return view;

    }

    private void initView(View view) {
        homeActivity = (Home_Activity) getActivity();
        Paper.init(homeActivity);
        cuurent_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
        tab = view.findViewById(R.id.tab);
        pager = view.findViewById(R.id.pager);
        tab.setupWithViewPager(pager);
        im_back=view.findViewById(R.id.arrow);
        if(cuurent_language.equals("en")){
            im_back.setRotation(180.0f);
        }
        im_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeActivity.Back();
            }
        });
        pager.setOffscreenPageLimit(3);
        fragmentList = new ArrayList<>();
        titleList = new ArrayList<>();
      AddFragmentList();

        titleList.add(getString(R.string.government_sector));
        titleList.add(getString(R.string.private_sector));
        titleList.add(getString(R.string.applay_for_job));

        adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.AddFragments(fragmentList);
        adapter.AddTitles(titleList);
        pager.setAdapter(adapter);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab));

tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        pager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
});

    }

    private void AddFragmentList() {
        fragmentList.add(Fragment_Government_Sector.newInstance());
        fragmentList.add(Fragment_Private_Sector.newInstance());
        fragmentList.add(Fragment_Apply_For_Job.newInstance());

    }

    public static Fragment_Jobs newInstance() {
        return new Fragment_Jobs();
    }




}
