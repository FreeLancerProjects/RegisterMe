package com.creativeshare.registerme.activities_fragments.activities.home_activity.fragments.fragment_home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

import com.creativeshare.registerme.R;
import com.creativeshare.registerme.activities_fragments.activities.home_activity.activity.Home_Activity;
import com.creativeshare.registerme.models.UserModel;
import com.creativeshare.registerme.preferences.Preferences;
import com.creativeshare.registerme.share.Common;
import com.google.android.material.navigation.NavigationView;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Home extends Fragment {
    private Home_Activity homeActivity;
    private AHBottomNavigation ah_bottom_nav;
    private String cuurent_language;

    private TextView tv_title;
    private Preferences preferences;
    private UserModel userModel;
    private ImageView im_cart,im_menu;
    private TextView textNotify;
    private int amount=0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initView(view);
        return view;
    }


    private void initView(View view) {
        homeActivity = (Home_Activity) getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(homeActivity);
        Paper.init(homeActivity);
        cuurent_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
        ah_bottom_nav = view.findViewById(R.id.ah_bottom_nav);
        tv_title = view.findViewById(R.id.tv_title);






        setUpBottomNavigation();
        updateBottomNavigationPosition(0);
        ah_bottom_nav.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                switch (position) {
                    case 0:
                        homeActivity.DisplayFragmentMain();
                        break;
                    case 1:
                        if(userModel!=null){

                            homeActivity.DisplayFragmentMyorders();}
                        else {
                            Common.CreateUserNotSignInAlertDialog(homeActivity);

                        }

                        break;
                    case 2:
                        if(userModel!=null){

                            homeActivity.DisplayFragmentNotifications();}
                        else {
                            Common.CreateUserNotSignInAlertDialog(homeActivity);

                        }

                        break;
                    case 3:
                        if(userModel!=null){
                            homeActivity.DisplayFragmentclientprofile();}
                        else {
                            Common.CreateUserNotSignInAlertDialog(homeActivity);
                        }
                        break;
                    case 4:
                        homeActivity.DisplayFragmentMore();
                        break;

                }
                return false;
            }
        });

    }




    private void setUpBottomNavigation() {

        AHBottomNavigationItem item1 = new AHBottomNavigationItem(getString(R.string.home), R.drawable.ic_nav_home);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(getString(R.string.my_orders), R.drawable.ic_order);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(getString(R.string.notifications), R.drawable.ic_nav_notification);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem(getString(R.string.my_profile), R.drawable.ic_user_profile);
        AHBottomNavigationItem item5 = new AHBottomNavigationItem(getString(R.string.more), R.drawable.ic_more);

        ah_bottom_nav.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        ah_bottom_nav.setDefaultBackgroundColor(ContextCompat.getColor(homeActivity, R.color.white));
        ah_bottom_nav.setTitleTextSizeInSp(14, 12);
        ah_bottom_nav.setForceTint(true);
        ah_bottom_nav.setAccentColor(ContextCompat.getColor(homeActivity, R.color.colorPrimary));
        ah_bottom_nav.setInactiveColor(ContextCompat.getColor(homeActivity, R.color.gray4));

        ah_bottom_nav.addItem(item1);
        ah_bottom_nav.addItem(item2);
        ah_bottom_nav.addItem(item3);
        ah_bottom_nav.addItem(item4);
        ah_bottom_nav.addItem(item5);


    }

    public void updateBottomNavigationPosition(int pos) {
        ah_bottom_nav.setCurrentItem(pos, false);
        if (pos == 0) {
            tv_title.setText(getResources().getString(R.string.home));
        } else if (pos == 1) {
            tv_title.setText(getResources().getString(R.string.my_orders));
        } else if (pos == 2) {
            tv_title.setText(getResources().getString(R.string.notifications));
        } else if (pos == 3) {
            tv_title.setText(getResources().getString(R.string.my_profile));
        } else if (pos == 4) {
            tv_title.setText(getResources().getString(R.string.more));
        }

    }

    public static Fragment_Home newInstance() {
        return new Fragment_Home();
    }


}
