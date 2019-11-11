package com.creativeshare.registerme.activities_fragments.activities.home_activity.fragments.fragment_home;

import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.creativeshare.registerme.R;
import com.creativeshare.registerme.activities_fragments.activities.home_activity.activity.Home_Activity;
import com.creativeshare.registerme.adapter.Order_Adapter;
import com.creativeshare.registerme.models.Order_Model;
import com.creativeshare.registerme.models.UserModel;
import com.creativeshare.registerme.preferences.Preferences;
import com.creativeshare.registerme.remote.Api;
import com.creativeshare.registerme.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Fragment_MyOrders extends Fragment {


    private Home_Activity activity;

    private ProgressBar progBar;
    private RecyclerView rec_depart;
    private List<Order_Model.Data> dataList;
    private Order_Adapter order_adapter;
    private GridLayoutManager gridLayoutManager;
    private LinearLayout ll_no_store;
private Preferences preferences;
    private UserModel userModel;
    public Fragment_MyOrders() {
        // Required empty public constructor
    }

    public static Fragment_MyOrders newInstance() {

        return new Fragment_MyOrders();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_myorders, container, false);
        intitview(view);
        if(userModel!=null){
            getOrders();
        }
        return view;
    }

    private void intitview(View view) {
        dataList=new ArrayList<>();
        activity = (Home_Activity) getActivity();
        preferences= Preferences.getInstance();
        userModel=preferences.getUserData(activity);

        progBar = view.findViewById(R.id.progBar2);
        ll_no_store=view.findViewById(R.id.ll_no_store);
        rec_depart=view.findViewById(R.id.rec_orders);

        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);



        progBar.setVisibility(View.GONE);
        rec_depart.setDrawingCacheEnabled(true);
        rec_depart.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        rec_depart.setItemViewCacheSize(25);


        gridLayoutManager=new GridLayoutManager(activity,1);
        rec_depart.setLayoutManager(gridLayoutManager);
        order_adapter =new Order_Adapter(dataList,activity,this);
        rec_depart.setAdapter(order_adapter);
    }

    public void getOrders() {
        //   Common.CloseKeyBoard(homeActivity, edt_name);

        // rec_sent.setVisibility(View.GONE);
        progBar.setVisibility(View.VISIBLE);
        Api.getService(Tags.base_url)
                .getorders(userModel.getUser().getId())
                .enqueue(new Callback<Order_Model>() {
                    @Override
                    public void onResponse(Call<Order_Model> call, Response<Order_Model> response) {
                        progBar.setVisibility(View.GONE);
                        if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                            dataList.clear();
                            dataList.addAll(response.body().getData());
                            if (response.body().getData().size() > 0) {
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
                    public void onFailure(Call<Order_Model> call, Throwable t) {
                        try {

                            progBar.setVisibility(View.GONE);
                            //    Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("error", t.getMessage());
                        } catch (Exception e) {
                        }
                    }
                });

    }


}
