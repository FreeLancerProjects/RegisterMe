package com.creativeshare.registerme.activities_fragments.activities.home_activity.fragments.fragment_home.fragment_jobs;

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


public class Fragment_Government_Sector extends Fragment {
private RecyclerView rec_job;
private List<AllInFo_Model.Data.Gcompanies> dataList;
private Government_Job_Adapter governmentJob_adapter;
private Home_Activity home_activity;
private LinearLayout ll_no_store;
    private ProgressBar progBar;
    private Button bt_send;
    private int company_id=-1;
    private Preferences preferences;
    private UserModel userModel;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_government_sector, container, false);
        initView(view);
        get_goverjob();
        return view;

    }

    private void initView(View view) {
home_activity=(Home_Activity)getActivity();
        preferences= Preferences.getInstance();
        userModel=preferences.getUserData(home_activity);
dataList=new ArrayList<>();
governmentJob_adapter =new Government_Job_Adapter(dataList,home_activity,this);
rec_job=view.findViewById(R.id.recView);
rec_job.setLayoutManager(new GridLayoutManager(home_activity,3));
rec_job.setAdapter(governmentJob_adapter);
ll_no_store=view.findViewById(R.id.ll_no_store);
        progBar = view.findViewById(R.id.progBar);
        bt_send=view.findViewById(R.id.btn_send);

        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(home_activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);


        bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkdata();
            }
        });
    }

    private void checkdata() {
        if(company_id!=-1){
            if(userModel!=null){
                Company_odere();
            }
            else {
                Common.CreateUserNotSignInAlertDialog(home_activity);
            }
        }
        else {
            Toast.makeText(home_activity,getResources().getString(R.string.choose_company),Toast.LENGTH_LONG).show();
        }
    }
    private void Company_odere() {

        final ProgressDialog dialog = Common.createProgressDialog(home_activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url).send_company(userModel.getUser().getId(),company_id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                dialog.dismiss();
                if (response.isSuccessful()) {
                    Toast.makeText(home_activity,getResources().getString(R.string.sucess),Toast.LENGTH_LONG).show();

                  //  Common.CreateSignAlertDialog(home_activity, getResources().getString(R.string.sucess));
                    home_activity.Displayorder();
                } else {
                    Common.CreateSignAlertDialog(home_activity, getString(R.string.failed));

                    try {
                        Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                try {
                    dialog.dismiss();
                    Toast.makeText(home_activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                    Log.e("Error", t.getMessage());
                } catch (Exception e) {
                }
            }
        });
    }

    public void setid(int id) {
        this.company_id=id;
    }
    public static Fragment_Government_Sector newInstance() {
        return new Fragment_Government_Sector();
    }

    private void get_goverjob() {
     progBar.setVisibility(View.VISIBLE);
        Api.getService(Tags.base_url).get_Info().enqueue(new Callback<AllInFo_Model>() {
            @Override
            public void onResponse(Call<AllInFo_Model> call, Response<AllInFo_Model> response) {
progBar.setVisibility(View.GONE);
dataList.clear();
                if (response.isSuccessful()) {
                    if (response.body().getData().getGcompanies().size() > 0) {
                        ll_no_store.setVisibility(View.GONE);
                        dataList.addAll(response.body().getData().getGcompanies());
                        governmentJob_adapter.notifyDataSetChanged();
                    } else {
                        //    error.setText("No data Found");
                        // recc.setVisibility(View.GONE);
                        ll_no_store.setVisibility(View.VISIBLE);
                    }
                } else {
                    // recc.setVisibility(View.GONE);
                    ll_no_store.setVisibility(View.VISIBLE);
                    try {

                        Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<AllInFo_Model> call, Throwable t) {
                try {
                    Log.e("Error", t.getMessage());
                   progBar.setVisibility(View.GONE);
                    Toast.makeText(home_activity,getString(R.string.something), Toast.LENGTH_SHORT).show();

//error.setText(activity.getString(R.string.faild));
                    //recc.setVisibility(View.GONE);
                } catch (Exception e) {

                }

            }
        });

    }

}
