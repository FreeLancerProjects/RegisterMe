package com.creativeshare.registerme.activities_fragments.activities.home_activity.fragments.fragment_home.fragment_create_email;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creativeshare.registerme.R;
import com.creativeshare.registerme.activities_fragments.activities.home_activity.activity.Home_Activity;
import com.creativeshare.registerme.adapter.Mail_Adapter;
import com.creativeshare.registerme.models.AllInFo_Model;
import com.creativeshare.registerme.models.UserModel;
import com.creativeshare.registerme.preferences.Preferences;
import com.creativeshare.registerme.remote.Api;
import com.creativeshare.registerme.share.Common;
import com.creativeshare.registerme.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Fragment_Create_Email extends Fragment {
    private RecyclerView rec_job;
    private List<AllInFo_Model.Data.EmailTypes> dataList;
    private Mail_Adapter mail_adapter;
    private Home_Activity home_activity;
    private ImageView im_back;
    private Button btn_send;
    private EditText edt_name,edt_password;
    private String cuurent_language;
    private int email_id=-1;
private UserModel userModel;
private Preferences preferences;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_email, container, false);
        initView(view);
        get_mailtype();
        return view;

    }

    private void initView(View view) {
        home_activity = (Home_Activity) getActivity();
        dataList = new ArrayList<>();
        Paper.init(home_activity);
        cuurent_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
preferences=Preferences.getInstance();
userModel=preferences.getUserData(home_activity);
        mail_adapter = new Mail_Adapter(dataList, home_activity, this);
        rec_job = view.findViewById(R.id.recView);
        im_back = view.findViewById(R.id.arrow);
        edt_name =view.findViewById(R.id.edt_name);
        edt_password = view.findViewById(R.id.edt_password);

        btn_send=view.findViewById(R.id.btn_send);
        if (cuurent_language.equals("en")) {
            im_back.setRotation(180.0f);
        }
        im_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home_activity.Back();
            }
        });
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chechdata();
            }
        });
        rec_job.setLayoutManager(new LinearLayoutManager(home_activity, RecyclerView.HORIZONTAL, false));
        rec_job.setAdapter(mail_adapter);

    }

    private void chechdata() {
        String name=edt_name.getText().toString();
        String pass=edt_password.getText().toString();
        if(email_id!=-1&&!TextUtils.isEmpty(name)&&!TextUtils.isEmpty(pass)&&pass.length()>=6){
         edt_name.setError(null);
edt_password.setError(null);
if(userModel!=null){
Create_email(name,pass);}
else {
    Common.CreateUserNotSignInAlertDialog(home_activity);
}
        }
        else {
            if(email_id==-1){
                Toast.makeText(home_activity,getResources().getString(R.string.choose_the_type_of_email_you_want),Toast.LENGTH_LONG).show();
            }
            if(TextUtils.isEmpty(name)){
                edt_name.setError(getResources().getString(R.string.field_req));
            }
            else {
                edt_name.setError("");
            }
            if(TextUtils.isEmpty(pass)){
                edt_password.setError(getResources().getString(R.string.field_req));
            }
            else if(pass.length()<6){
                edt_password.setError(getResources().getString(R.string.pass_must_more_than_6_digit));
            }
            else {
                edt_password.setError("");
            }
        }
    }

    private void Create_email(String name,String pass) {

        final ProgressDialog dialog = Common.createProgressDialog(home_activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url).create_email(name,pass,userModel.getUser().getId(),email_id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                dialog.dismiss();
                if (response.isSuccessful()) {
                    Toast.makeText(home_activity,getResources().getString(R.string.sucess),Toast.LENGTH_LONG).show();
                    home_activity.Displayorder();

                    // Common.CreateSignAlertDialog(home_activity, getResources().getString(R.string.sucess));

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


    public static Fragment_Create_Email newInstance() {
        return new Fragment_Create_Email();
    }

    private void get_mailtype() {
        final ProgressDialog dialog = Common.createProgressDialog(home_activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url).get_Info().enqueue(new Callback<AllInFo_Model>() {
            @Override
            public void onResponse(Call<AllInFo_Model> call, Response<AllInFo_Model> response) {
                dialog.dismiss();
                dataList.clear();
                if (response.isSuccessful()) {
                    if (response.body().getData().getEmailTypes().size() > 0) {
                        dataList.addAll(response.body().getData().getEmailTypes());
                        mail_adapter.notifyDataSetChanged();
                    } else {
                        //    error.setText("No data Found");
                        // recc.setVisibility(View.GONE);
                    }
                } else {
                    // recc.setVisibility(View.GONE);
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
dialog.dismiss();
                    Toast.makeText(home_activity,getString(R.string.something), Toast.LENGTH_SHORT).show();

//error.setText(activity.getString(R.string.faild));
                    //recc.setVisibility(View.GONE);
                } catch (Exception e) {

                }

            }
        });

    }


    public void setid(int id) {
        this.email_id=id;
    }
}
