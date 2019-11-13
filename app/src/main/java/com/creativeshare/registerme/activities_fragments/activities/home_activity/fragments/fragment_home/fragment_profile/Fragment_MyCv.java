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
import com.creativeshare.registerme.adapter.Profile_Order_Adapter;
import com.creativeshare.registerme.models.AllInFo_Model;
import com.creativeshare.registerme.models.Profile_Order_Model;
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
    private RecyclerView rec_orders;
    private Preferences preferences;
    private UserModel userModel;
    private List<Profile_Order_Model.Orders> ordersList;
    private Profile_Order_Adapter order_adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_cv, container, false);
        initView(view);
        if(userModel!=null){
            getOrders();
        }
        return view;

    }

    private void initView(View view) {
        ordersList=new ArrayList<>();
        home_activity=(Home_Activity)getActivity();
        preferences= Preferences.getInstance();
        userModel=preferences.getUserData(home_activity);
        ll_no_store=view.findViewById(R.id.ll_no_store);
        progBar = view.findViewById(R.id.progBar);
        rec_orders =view.findViewById(R.id.rec_orders);

        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(home_activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);



        progBar.setVisibility(View.GONE);
        rec_orders.setDrawingCacheEnabled(true);
        rec_orders.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        rec_orders.setItemViewCacheSize(25);


        rec_orders.setLayoutManager(new GridLayoutManager(home_activity,1));
        order_adapter =new Profile_Order_Adapter(ordersList,home_activity,this);
        rec_orders.setAdapter(order_adapter);


    }

    public void getOrders() {
        //   Common.CloseKeyBoard(homeActivity, edt_name);

        // rec_sent.setVisibility(View.GONE);
        progBar.setVisibility(View.VISIBLE);
        Api.getService(Tags.base_url)
                .getorders_type(userModel.getUser().getId(),1)
                .enqueue(new Callback<Profile_Order_Model>() {
                    @Override
                    public void onResponse(Call<Profile_Order_Model> call, Response<Profile_Order_Model> response) {
                        progBar.setVisibility(View.GONE);
                        if (response.isSuccessful() && response.body() != null && response.body().getOrders() != null) {
                            ordersList.clear();
                            ordersList.addAll(response.body().getOrders());
                            if (response.body().getOrders().size() > 0) {
                                // rec_sent.setVisibility(View.VISIBLE);

                                ll_no_store.setVisibility(View.GONE);
                                order_adapter.notifyDataSetChanged();
                                //   total_page = response.body().getMeta().getLast_page();

                            } else {
                                ll_no_store.setVisibility(View.VISIBLE);

                            }
                        } else {
                            ll_no_store.setVisibility(View.VISIBLE);

                            //  Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            try {
                                Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Profile_Order_Model> call, Throwable t) {
                        try {

                            progBar.setVisibility(View.GONE);
                            //    Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("error", t.getMessage());
                        } catch (Exception e) {
                        }
                    }
                });

    }
    public static Fragment_MyCv newInstance() {
        return new Fragment_MyCv();
    }



}
