package com.creativeshare.registerme.activities_fragments.activities.home_activity.fragments.fragment_home.fragment_jobs;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.creativeshare.registerme.R;
import com.creativeshare.registerme.activities_fragments.activities.home_activity.activity.Home_Activity;
import com.creativeshare.registerme.models.UserModel;
import com.creativeshare.registerme.preferences.Preferences;
import com.creativeshare.registerme.remote.Api;
import com.creativeshare.registerme.share.Common;
import com.creativeshare.registerme.tags.Tags;

import java.io.IOException;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Fragment_Apply_For_Job extends Fragment {
private UserModel userModel;
private Preferences preferences;
private Home_Activity home_activity;
private EditText edt_link;
private Button bt_send;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_apply_for_job, container, false);
        initView(view);
        return view;

    }

    private void initView(View view) {
home_activity=(Home_Activity)getActivity();
        Paper.init(home_activity);
        preferences=Preferences.getInstance();
        userModel=preferences.getUserData(home_activity);
        edt_link=view.findViewById(R.id.edt_link);
        bt_send=view.findViewById(R.id.btn_send);
        bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chechdata();
            }
        });


    }

    private void chechdata() {
        String link=edt_link.getText().toString();
        if(!TextUtils.isEmpty(link)){
            edt_link.setError(null);
            if(userModel!=null){
                Send_link(link);}
            else {
                Common.CreateUserNotSignInAlertDialog(home_activity);
            }
        }
        else {
            if(TextUtils.isEmpty(link)){
                edt_link.setError(getResources().getString(R.string.field_req));
            }
            else {
                edt_link.setError("");
            }
        }
    }

    private void Send_link(String link) {

        final ProgressDialog dialog = Common.createProgressDialog(home_activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url).send_link(userModel.getUser().getId(),link).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                dialog.dismiss();
                if (response.isSuccessful()) {
                    Toast.makeText(home_activity,getResources().getString(R.string.sucess),Toast.LENGTH_LONG).show();

                    home_activity.Back();

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

    public static Fragment_Apply_For_Job newInstance() {
        return new Fragment_Apply_For_Job();
    }

}
