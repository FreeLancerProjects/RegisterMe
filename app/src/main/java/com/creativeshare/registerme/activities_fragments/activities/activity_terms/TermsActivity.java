package com.creativeshare.registerme.activities_fragments.activities.activity_terms;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;


import com.creativeshare.registerme.R;
import com.creativeshare.registerme.activities_fragments.activities.home_activity.activity.Home_Activity;
import com.creativeshare.registerme.language.Language_Helper;
import com.creativeshare.registerme.models.AppDataModel;
import com.creativeshare.registerme.preferences.Preferences;
import com.creativeshare.registerme.remote.Api;
import com.creativeshare.registerme.tags.Tags;

import java.io.IOException;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TermsActivity extends AppCompatActivity  {
    private String lang;
    private ImageView back;
    private TextView tv_content;
    private Home_Activity activity;
    private String cuurent_language;
    private Preferences preferences;
private Button bt_Close;
    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language_Helper.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
setContentView( R.layout.activity_terms);
        initView();

    }


    private void initView() {

        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());

        cuurent_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
        preferences = Preferences.getInstance();
        tv_content = findViewById(R.id.tv_content);
        back = findViewById(R.id.arrow_back);
bt_Close=findViewById(R.id.bt_Close);
bt_Close.setVisibility(View.GONE);
bt_Close.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        finish();
    }
});
        if (cuurent_language.equals("en")) {

            back.setRotation(180);
        }
        getAppData();


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.Back();
            }
        });
    }

    private void getAppData() {

        Api.getService(Tags.base_url)
                .getterms()
                .enqueue(new Callback<AppDataModel>() {
                    @Override
                    public void onResponse(Call<AppDataModel> call, Response<AppDataModel> response) {
                        //  smoothprogressbar.setVisibility(View.GONE);
                        Log.e("Error", response.code() + "" );
                        if (response.isSuccessful()&&response.body()!=null&&response.body().getData()!=null){
                            updateTermsContent(response.body());
                        } else {
                            try {
                                Log.e("Error", response.code() + "" + response.raw());

                            }
                            catch (Exception e){

                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<AppDataModel> call, Throwable t) {
                        try {
                            // smoothprogressbar.setVisibility(View.GONE);
                            Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("Error", t.getMessage());

                        } catch (Exception e) {
                        }
                    }
                });

    }

    private void updateTermsContent(AppDataModel appDataModel) {
        if(cuurent_language.equals("ar")){
            tv_content.setText(appDataModel.getData().getAr_content());}
        else {
            tv_content.setText(appDataModel.getData().getEn_content());
        }


    }

}
