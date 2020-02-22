package com.endpoint.registerme.activities_fragments.activities.home_activity.fragments.fragment_home.fragment_create_email;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.endpoint.registerme.R;
import com.endpoint.registerme.activities_fragments.activities.home_activity.activity.Home_Activity;
import com.endpoint.registerme.adapter.Mail_Adapter;
import com.endpoint.registerme.models.AllInFo_Model;
import com.endpoint.registerme.models.ServicePriceModel;
import com.endpoint.registerme.models.UserModel;
import com.endpoint.registerme.preferences.Preferences;
import com.endpoint.registerme.remote.Api;
import com.endpoint.registerme.share.Common;
import com.endpoint.registerme.tags.Tags;
import com.telr.mobile.sdk.activty.WebviewActivity;
import com.telr.mobile.sdk.entity.request.payment.Address;
import com.telr.mobile.sdk.entity.request.payment.App;
import com.telr.mobile.sdk.entity.request.payment.Billing;
import com.telr.mobile.sdk.entity.request.payment.MobileRequest;
import com.telr.mobile.sdk.entity.request.payment.Name;
import com.telr.mobile.sdk.entity.request.payment.Tran;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

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
    private EditText edt_name, edt_password;
    private String cuurent_language;
    private int email_id = -1;
    private UserModel userModel;
    private Preferences preferences;
    private ServicePriceModel servicepricemodel;
    public static final String KEY = "gQq9M^TFrFs~FJPr";        // TODO: Insert your Key here
    public static final String STORE_ID = "22865";    // TODO: Insert your Store ID here
    public static final String EMAIL= "al-waafi8567@hotmail.com";     // TODO: Insert the customer email here

private TextView tv_price;
    public static final boolean isSecurityEnabled = false;
    private int order_id;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_email, container, false);
        initView(view);
        get_mailtype();
        getserviceprive();
        return view;

    }

    private void initView(View view) {
        home_activity = (Home_Activity) getActivity();
        dataList = new ArrayList<>();
        Paper.init(home_activity);
        cuurent_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
        preferences = Preferences.getInstance();
        tv_price=view.findViewById(R.id.tv_price);
        userModel = preferences.getUserData(home_activity);
        mail_adapter = new Mail_Adapter(dataList, home_activity, this);
        rec_job = view.findViewById(R.id.recView);
        im_back = view.findViewById(R.id.arrow);
        edt_name = view.findViewById(R.id.edt_name);
        edt_password = view.findViewById(R.id.edt_password);

        btn_send = view.findViewById(R.id.btn_send);
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
    public void sendMessage(ResponseBody body){
        JSONObject obj = null;

        try {
            String re=body.string();
            Log.e("data",re);
            obj = new JSONObject(re);
            // Log.e("data",obj.stri);
            order_id=obj.getInt("id");

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("data",e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(home_activity, WebviewActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


        intent.putExtra(WebviewActivity.EXTRA_MESSAGE, getMobileRequest());
        intent.putExtra(WebviewActivity.SUCCESS_ACTIVTY_CLASS_NAME, "com.endpoint.registerme.activities_fragments.activities.activity_payment.SuccessTransationActivity");
        intent.putExtra(WebviewActivity.FAILED_ACTIVTY_CLASS_NAME, "com.endpoint.registerme.activities_fragments.activities.activity_payment.FailedTransationActivity");
        intent.putExtra(WebviewActivity.IS_SECURITY_ENABLED, isSecurityEnabled);

        startActivityForResult(intent,1);
    }
    private MobileRequest getMobileRequest() {
        MobileRequest mobile = new MobileRequest();
        mobile.setStore(STORE_ID);                       // Store ID
        mobile.setKey(KEY);                              // Authentication Key : The Authentication Key will be supplied by Telr as part of the Mobile API setup process after you request that this integration type is enabled for your account. This should not be stored permanently within the App.
        App app = new App();
        app.setId("com.endpoint.registerme");                          // Application installation ID
        app.setName("Create Email");                    // Application name
        app.setUser(userModel.getUser().getName());                           // Application user ID : Your reference for the customer/user that is running the App. This should relate to their account within your systems.
        app.setVersion("0.0.1");                         // Application version
        app.setSdk("123");
        mobile.setApp(app);
        Tran tran = new Tran();
        tran.setTest("0");                              // Test mode : Test mode of zero indicates a live transaction. If this is set to any other value the transaction will be treated as a test.
        tran.setType("sale");                           /* Transaction type
                                                            'auth'   : Seek authorisation from the card issuer for the amount specified. If authorised, the funds will be reserved but will not be debited until such time as a corresponding capture command is made. This is sometimes known as pre-authorisation.
                                                            'sale'   : Immediate purchase request. This has the same effect as would be had by performing an auth transaction followed by a capture transaction for the full amount. No additional capture stage is required.
                                                            'verify' : Confirm that the card details given are valid. No funds are reserved or taken from the card.
                                                        */
        tran.setClazz("paypage");                       // Transaction class only 'paypage' is allowed on mobile, which means 'use the hosted payment page to capture and process the card details'
        tran.setCartid(String.valueOf(new BigInteger(128, new Random()))); //// Transaction cart ID : An example use of the cart ID field would be your own transaction or order reference.
        tran.setDescription("Test Mobile API");         // Transaction description
        tran.setCurrency("SAR");                        // Transaction currency : Currency must be sent as a 3 character ISO code. A list of currency codes can be found at the end of this document. For voids or refunds, this must match the currency of the original transaction.
        tran.setAmount(servicepricemodel.getCreate_email());                         // Transaction amount : The transaction amount must be sent in major units, for example 9 dollars 50 cents must be sent as 9.50 not 950. There must be no currency symbol, and no thousands separators. Thedecimal part must be separated using a dot.
        //tran.setRef(???);                           // (Optinal) Previous transaction reference : The previous transaction reference is required for any continuous authority transaction. It must contain the reference that was supplied in the response for the original transaction.
        tran.setLangauge("en");                        // (Optinal) default is en -> English
        mobile.setTran(tran);
        Billing billing = new Billing();
        Address address = new Address();
        address.setCity("Saudi");                       // City : the minimum required details for a transaction to be processed
        address.setCountry("SA");                       // Country : Country must be sent as a 2 character ISO code. A list of country codes can be found at the end of this document. the minimum required details for a transaction to be processed
        address.setRegion("Saudi");                     // Region
        address.setLine1("SIT G=Towe");                 // Street address – line 1: the minimum required details for a transaction to be processed
        //address.setLine2("SIT G=Towe");               // (Optinal)
        //address.setLine3("SIT G=Towe");               // (Optinal)
        //address.setZip("SIT G=Towe");                 // (Optinal)
        billing.setAddress(address);
        Name name = new Name();
        name.setFirst(userModel.getUser().getName());                          // Forename : the minimum required details for a transaction to be processed
        name.setLast(userModel.getUser().getName());                          // Surname : the minimum required details for a transaction to be processed
        name.setTitle(userModel.getUser().getName());                           // Title
        billing.setName(name);
        billing.setEmail(EMAIL);                 // TODO: Insert your email here : the minimum required details for a transaction to be processed.
        billing.setPhone(userModel.getUser().getPhone());                // Phone number, required if enabled in your merchant dashboard.
        mobile.setBilling(billing);
        return mobile;

    }

    /* This example used for continuous authority after using the first request, it used for recurring payment without asking the user to fill again the card details  */
    private void getserviceprive() {
        final Dialog dialog = Common.createProgressDialog(home_activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
       /* if (uri == null&&userModel.getLogo()!=null) {
            uri = Uri.parse(Tags.IMAGE_URL + userModel.getLogo());

        }*/




        Api.getService(Tags.base_url).getserviceprice().enqueue(new Callback<ServicePriceModel>() {
            @Override
            public void onResponse(Call<ServicePriceModel> call, Response<ServicePriceModel> response) {
                dialog.dismiss();
                if (response.isSuccessful() && response.body() != null) {


                    // Common.CreateSignAlertDialog(homeActivity, getResources().getString(R.string.suc));

                    // edt_pass.setText("");
                    //  updateprofile();
                    updatesrvice(response. body());
                } else {

                    try {
                        //  Toast.makeText(homeActivity, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                        Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ServicePriceModel> call, Throwable t) {
                dialog.dismiss();
                try {
                    //  Toast.makeText(homeActivity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                    Log.e("Error", t.getMessage());
                }
                catch (Exception e){

                }

            }
        });

    }

    private void updatesrvice(ServicePriceModel body) {
        this.servicepricemodel=body;
        tv_price.setText(home_activity.getResources().getString(R.string.price)+servicepricemodel.getCreate_email());
    }
    private void chechdata() {
        String name = edt_name.getText().toString();
        String pass = edt_password.getText().toString();
        if (email_id != -1 && !TextUtils.isEmpty(name) && !TextUtils.isEmpty(pass) && pass.length() >= 6) {
            edt_name.setError(null);
            edt_password.setError(null);
            Common.CloseKeyBoard(home_activity,edt_name);

            if (userModel != null) {
                Create_email(name, pass);
            } else {
                Common.CreateUserNotSignInAlertDialog(home_activity);
            }
        } else {
            if (email_id == -1) {
                Toast.makeText(home_activity, getResources().getString(R.string.choose_the_type_of_email_you_want), Toast.LENGTH_LONG).show();
            }
            if (TextUtils.isEmpty(name)) {
                edt_name.setError(getResources().getString(R.string.field_req));
            } else {
                edt_name.setError("");
            }
            if (TextUtils.isEmpty(pass)) {
                edt_password.setError(getResources().getString(R.string.field_req));
            } else if (pass.length() < 6) {
                edt_password.setError(getResources().getString(R.string.pass_must_more_than_6_digit));
            } else {
                edt_password.setError("");
            }
        }
    }

    private void Create_email(String name, String pass) {

        final ProgressDialog dialog = Common.createProgressDialog(home_activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url).create_email(name, pass, userModel.getUser().getId(), email_id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                dialog.dismiss();
                if (response.isSuccessful()) {
                    Toast.makeText(home_activity, getResources().getString(R.string.sucess), Toast.LENGTH_LONG).show();
                    //home_activity.Displayorder();
sendMessage(response.body());
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
                    Toast.makeText(home_activity, getString(R.string.something), Toast.LENGTH_SHORT).show();

//error.setText(activity.getString(R.string.faild));
                    //recc.setVisibility(View.GONE);
                } catch (Exception e) {

                }

            }
        });

    }


    public void setid(int id) {
        this.email_id = id;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 &&  data != null) {

Log.e("kvnnvjvb",data.getStringExtra("text"));





        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("gggggg",preferences.Ispaid(home_activity)+""+order_id);
        if(order_id!=0){
        if(preferences.Ispaid(home_activity)){
            paid(1);
        }
        else {
            paid(0);
        }}
    }
    private void paid(int i) {

        final ProgressDialog dialog = Common.createProgressDialog(home_activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url).setpaid(order_id,i).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                dialog.dismiss();
                if (response.isSuccessful()) {
                    if(i==1){
                    Toast.makeText(home_activity, getResources().getString(R.string.sucess), Toast.LENGTH_LONG).show();}
                    else {
                        Toast.makeText(home_activity, getResources().getString(R.string.order_sent), Toast.LENGTH_LONG).show();
                    }
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

}
