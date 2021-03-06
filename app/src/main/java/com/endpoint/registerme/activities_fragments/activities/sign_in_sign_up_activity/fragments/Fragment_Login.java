package com.endpoint.registerme.activities_fragments.activities.sign_in_sign_up_activity.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;


import com.endpoint.registerme.R;
import com.endpoint.registerme.activities_fragments.activities.home_activity.activity.Home_Activity;
import com.endpoint.registerme.activities_fragments.activities.sign_in_sign_up_activity.activity.Login_Activity;
import com.endpoint.registerme.models.UserModel;
import com.endpoint.registerme.preferences.Preferences;
import com.endpoint.registerme.remote.Api;
import com.endpoint.registerme.share.Common;
import com.endpoint.registerme.tags.Tags;
import com.mukesh.countrypicker.Country;
import com.mukesh.countrypicker.CountryPicker;
import com.mukesh.countrypicker.listeners.OnCountryPickerListener;

import java.io.IOException;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Fragment_Login extends Fragment  implements OnCountryPickerListener {
    private Button btn_login;
    private TextView tv_skip, tv_sign_up,tv_code;
    private EditText edt_phone;
    private Login_Activity activity;
    private Preferences preferences;
    private CountryPicker picker;
    private String code = "";
    private String current_language;
    private Home_Activity homeActivity;
    private ImageView image_phone_code;
    public static Fragment_Login newInstance() {

        return new Fragment_Login();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        initView(view);
        return view;
    }

    private void initView(final View view) {
        activity = (Login_Activity) getActivity();
        Paper.init(activity);
        current_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
        btn_login = view.findViewById(R.id.btn_login);
        image_phone_code=view.findViewById(R.id.image_phone_code);
        edt_phone=view.findViewById(R.id.edt_phone);
        tv_code=view.findViewById(R.id.tv_code);
        tv_skip = view.findViewById(R.id.tv_skip);
        tv_sign_up = view.findViewById(R.id.tv_sign_up);
        CreateCountryDialog();
        if (current_language.equals("ar")) {
            image_phone_code.setRotation(180.0f);
        }
        tv_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.DisplayFragmentSignUp();
            }
        });

        tv_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, Home_Activity.class);
                startActivity(intent);
                activity.finish();
            }
        });
        image_phone_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picker.showDialog(activity);
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkData();
            }
        });

    }
    private void updateUi(Country country) {

        tv_code.setText(country.getDialCode());
        code = country.getDialCode();


    }
    private void CreateCountryDialog() {
        CountryPicker.Builder builder = new CountryPicker.Builder()
                .canSearch(true)
                .with(activity)
                .listener(this);
        picker = builder.build();

        TelephonyManager telephonyManager = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);


        if (picker.getCountryFromSIM() != null) {
            updateUi(picker.getCountryFromSIM());

        } else if (telephonyManager != null && picker.getCountryByISO(telephonyManager.getNetworkCountryIso()) != null) {
            updateUi(picker.getCountryByISO(telephonyManager.getNetworkCountryIso()));


        } else if (picker.getCountryByLocale(Locale.getDefault()) != null) {
            updateUi(picker.getCountryByLocale(Locale.getDefault()));

        } else {
            tv_code.setText("+966");
            code = "+966";
        }


    }

    @Override
    public void onSelectCountry(Country country) {
        updateUi(country);
    }
    private void checkData() {
        String m_phone = edt_phone.getText().toString().trim();
        if (!TextUtils.isEmpty(m_phone) &&

                !TextUtils.isEmpty(code)
        ) {
            edt_phone.setError(null);

            Common.CloseKeyBoard(activity, edt_phone);
            if (m_phone.startsWith("0"))
            {
                m_phone=m_phone.replaceFirst("0","");
            }
            Login(m_phone);

        } else {


            if (TextUtils.isEmpty(m_phone)) {
                //    edt_username.setError(getString(R.string.field_req));
            } else {
                edt_phone.setError(null);

            }



            if (TextUtils.isEmpty(code)) {
                tv_code.setError(getString(R.string.field_req));
            } else {
                tv_code.setError(null);

            }
        }
    }



    private void Login(String m_phone) {
        final ProgressDialog dialog = Common.createProgressDialog(activity,getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .Signin( m_phone, code.replace("+","00"))
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()&&response.body()!=null) {

                            activity.sendverficationcode(m_phone,code.replace("00","+"),response.body());

                        } else if (response.code() == 404) {
                            Common.CreateSignAlertDialog(activity,getString(R.string.user_not_found));
                        } else {
                            Common.CreateSignAlertDialog(activity,getString(R.string.inc_phone_pas));

                            try {
                                Log.e("Error_code",response.code()+"_"+response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            Toast.makeText(activity,getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("Error",t.getMessage());
                        } catch (Exception e) {
                        }
                    }
                });
    }




}
