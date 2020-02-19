package com.endpoint.registerme.activities_fragments.activities.home_activity.fragments.fragment_home.fragmnet_more;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.endpoint.registerme.R;
import com.endpoint.registerme.activities_fragments.activities.home_activity.activity.Home_Activity;
import com.endpoint.registerme.models.AppDataModel;
import com.endpoint.registerme.preferences.Preferences;
import com.endpoint.registerme.remote.Api;
import com.endpoint.registerme.tags.Tags;

import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Terms_Conditions extends Fragment {
    private Preferences preferences;
    private ImageView back;
    private TextView tv_content;
    private Home_Activity activity;
    private String cuurent_language;

    public static Fragment_Terms_Conditions newInstance() {

        Fragment_Terms_Conditions fragment_terms_conditions = new Fragment_Terms_Conditions();
        return fragment_terms_conditions;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_terms__conditions, container, false);
        intitview(view);
        // Inflate the layout for this fragment
        return view;
    }

    private void intitview(View view) {
        activity = (Home_Activity) getActivity();
        Paper.init(activity);
        cuurent_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
        preferences = Preferences.getInstance();
        tv_content = view.findViewById(R.id.tv_content);
        back = (ImageView) view.findViewById(R.id.arrow_back);
        preferences = Preferences.getInstance();

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
