package com.creativeshare.registerme.activities_fragments.activities.home_activity.fragments.fragment_home.fragment_jobs;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.net.Uri;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aditya.filebrowser.Constants;
import com.aditya.filebrowser.FileChooser;
import com.creativeshare.registerme.R;
import com.creativeshare.registerme.activities_fragments.activities.home_activity.activity.Home_Activity;
import com.creativeshare.registerme.adapter.Government_Job_Adapter;
import com.creativeshare.registerme.models.AllInFo_Model;
import com.creativeshare.registerme.models.ServicePriceModel;
import com.creativeshare.registerme.models.UserModel;
import com.creativeshare.registerme.preferences.Preferences;
import com.creativeshare.registerme.remote.Api;
import com.creativeshare.registerme.share.Common;
import com.creativeshare.registerme.tags.Tags;
import com.jaiselrahman.filepicker.activity.FilePickerActivity;
import com.jaiselrahman.filepicker.config.Configurations;
import com.jaiselrahman.filepicker.model.MediaFile;
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
import java.util.Random;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Fragment_Government_Sector extends Fragment {
private RecyclerView rec_job;
private List<AllInFo_Model.Data.Gcompanies> dataList;
private Government_Job_Adapter governmentJob_adapter;
private Home_Activity activity;
private LinearLayout ll_no_store;
    private ProgressBar progBar;
    private Button bt_send;
    private int company_id=-1;
    private Preferences preferences;
    private UserModel userModel;
    private final int File_REQ1 = 2;
    private Uri fileUri1 = null;
    private final String READ_PERM = Manifest.permission.READ_EXTERNAL_STORAGE;
    private ServicePriceModel servicepricemodel;
    public static final String KEY = "gQq9M^TFrFs~FJPr";        // TODO: Insert your Key here
    public static final String STORE_ID = "22865";    // TODO: Insert your Store ID here
    public static final String EMAIL= "al-waafi8567@hotmail.com";
    public static final boolean isSecurityEnabled = false;
    private int order_id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_government_sector, container, false);
        initView(view);
        get_goverjob();
        getserviceprive();
        return view;

    }

    private void initView(View view) {
activity =(Home_Activity)getActivity();
        preferences= Preferences.getInstance();
        userModel=preferences.getUserData(activity);
dataList=new ArrayList<>();
governmentJob_adapter =new Government_Job_Adapter(dataList, activity,this);
rec_job=view.findViewById(R.id.recView);
rec_job.setLayoutManager(new GridLayoutManager(activity,3));
rec_job.setAdapter(governmentJob_adapter);
ll_no_store=view.findViewById(R.id.ll_no_store);
        progBar = view.findViewById(R.id.progBar);
        bt_send=view.findViewById(R.id.btn_send);

        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);


        bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkdata();
            }
        });
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
        Intent intent = new Intent(activity, WebviewActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


        intent.putExtra(WebviewActivity.EXTRA_MESSAGE, getMobileRequest());
        intent.putExtra(WebviewActivity.SUCCESS_ACTIVTY_CLASS_NAME, "com.creativeshare.registerme.activities_fragments.activities.activity_payment.SuccessTransationActivity");
        intent.putExtra(WebviewActivity.FAILED_ACTIVTY_CLASS_NAME, "com.creativeshare.registerme.activities_fragments.activities.activity_payment.FailedTransationActivity");
        intent.putExtra(WebviewActivity.IS_SECURITY_ENABLED, isSecurityEnabled);

        startActivityForResult(intent,1);
    }
    private MobileRequest getMobileRequest() {
        MobileRequest mobile = new MobileRequest();
        mobile.setStore(STORE_ID);                       // Store ID
        mobile.setKey(KEY);                              // Authentication Key : The Authentication Key will be supplied by Telr as part of the Mobile API setup process after you request that this integration type is enabled for your account. This should not be stored permanently within the App.
        App app = new App();
        app.setId("com.creativeshare.registerme");                          // Application installation ID
        app.setName("applying for a job");                    // Application name
        app.setUser(userModel.getUser().getName());                           // Application user ID : Your reference for the customer/user that is running the App. This should relate to their account within your systems.
        app.setVersion("0.0.1");                         // Application version
        app.setSdk("123");
        mobile.setApp(app);
        Tran tran = new Tran();
        tran.setTest("0");                              // Test mode : Test mode of zero indicates a live transaction. If this is set to any other value the transaction will be treated as a test.
        tran.setType("auth");                           /* Transaction type
                                                            'auth'   : Seek authorisation from the card issuer for the amount specified. If authorised, the funds will be reserved but will not be debited until such time as a corresponding capture command is made. This is sometimes known as pre-authorisation.
                                                            'sale'   : Immediate purchase request. This has the same effect as would be had by performing an auth transaction followed by a capture transaction for the full amount. No additional capture stage is required.
                                                            'verify' : Confirm that the card details given are valid. No funds are reserved or taken from the card.
                                                        */
        tran.setClazz("paypage");                       // Transaction class only 'paypage' is allowed on mobile, which means 'use the hosted payment page to capture and process the card details'
        tran.setCartid(String.valueOf(new BigInteger(128, new Random()))); //// Transaction cart ID : An example use of the cart ID field would be your own transaction or order reference.
        tran.setDescription("Test Mobile API");         // Transaction description
        tran.setCurrency("SAR");                        // Transaction currency : Currency must be sent as a 3 character ISO code. A list of currency codes can be found at the end of this document. For voids or refunds, this must match the currency of the original transaction.
        tran.setAmount(servicepricemodel.getApply_job());                         // Transaction amount : The transaction amount must be sent in major units, for example 9 dollars 50 cents must be sent as 9.50 not 950. There must be no currency symbol, and no thousands separators. Thedecimal part must be separated using a dot.
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
        name.setTitle("Mr");                           // Title
        billing.setName(name);
        billing.setEmail(EMAIL);                 // TODO: Insert your email here : the minimum required details for a transaction to be processed.
        billing.setPhone(userModel.getUser().getPhone());                // Phone number, required if enabled in your merchant dashboard.
        mobile.setBilling(billing);
        return mobile;

    }

    /* This example used for continuous authority after using the first request, it used for recurring payment without asking the user to fill again the card details  */
    private void getserviceprive() {
        final Dialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
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
    }
    private void checkdata() {
        if(company_id!=-1){
            if(userModel!=null){
CheckReadPermission();            }
            else {
                Common.CreateUserNotSignInAlertDialog(activity);
            }
        }
        else {
            Toast.makeText(activity,getResources().getString(R.string.choose_company),Toast.LENGTH_LONG).show();
        }
    }
    private void Company_odere() {

        final ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        RequestBody name_part = Common.getRequestBodyText(userModel.getUser().getId()+"");

        RequestBody company_part = Common.getRequestBodyText(company_id+"");
        MultipartBody.Part image_part = Common.getMultiPartdoc(activity, Uri.parse(fileUri1.toString()), "cv");
        Api.getService(Tags.base_url).send_company(name_part,company_part,image_part).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                dialog.dismiss();
                if (response.isSuccessful()) {
                    Toast.makeText(activity,getResources().getString(R.string.sucess),Toast.LENGTH_LONG).show();

                  //  Common.CreateSignAlertDialog(activity, getResources().getString(R.string.sucess));
                   // activity.Displayorder();
                    sendMessage(response.body());
                } else {
                    Common.CreateSignAlertDialog(activity, getString(R.string.failed));

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
                    Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
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
                        bt_send.setVisibility(View.VISIBLE);

                    } else {
                        //    error.setText("No data Found");
                        // recc.setVisibility(View.GONE);
                        ll_no_store.setVisibility(View.VISIBLE);
                        bt_send.setVisibility(View.GONE);
                    }
                } else {
                    // recc.setVisibility(View.GONE);
                    ll_no_store.setVisibility(View.VISIBLE);
                    bt_send.setVisibility(View.GONE);

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
                    Toast.makeText(activity,getString(R.string.something), Toast.LENGTH_SHORT).show();

//error.setText(activity.getString(R.string.faild));
                    //recc.setVisibility(View.GONE);
                } catch (Exception e) {

                }

            }
        });

    }
    private void CheckReadPermission() {
        if (ActivityCompat.checkSelfPermission(activity, READ_PERM) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{READ_PERM}, File_REQ1);
        } else {
            SelectFile(File_REQ1);
        }
    }

    private void SelectFile(int file_req) {

/*
        Intent intent = new Intent();

        if (file_req == File_REQ1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            } else {
                intent.setAction(Intent.ACTION_GET_CONTENT);

            }
           // intent.putExtra("android.content.extra.SHOW_ADVANCED", true);

         //   intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(intent, file_req);*/
//
        Intent i2 = new Intent(activity, FileChooser.class);
        i2.putExtra(Constants.SELECTION_MODE, Constants.SELECTION_MODES.SINGLE_SELECTION.ordinal());
        startActivityForResult(i2, File_REQ1);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == File_REQ1) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                SelectFile(File_REQ1);
            } else {
                Toast.makeText(activity, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
            }

        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == File_REQ1 && resultCode == Activity.RESULT_OK && data != null) {
            // matches = activity.getPackageManager().queryIntentActivities(data, 0);
//            ArrayList<MediaFile> files = data.getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES);
//            for(int i=0;i<files.size();i++){
//                fileUri1=files.get(i).getUri();;
//                //    tv_name.setText(fileUri1.getLastPathSegment());
//
//            }
            //   image_upload.setVisibility(View.GONE);
            //    image_form.setImageDrawable(getResources().getDrawable(R.drawable.ic_document));
            //   image_form.setImageDrawable(matches.get(0).loadIcon(activity.getPackageManager()));
            //  tv_name.setText(fileUri1.get);
            //String type = data.getType();
            // Log.e("datass",type);
            // editImageProfile(userModel.getUser().getId()+"",fileUri1.toString());
            fileUri1 = data.getData();

            Company_odere();

        }
        else  if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {

            Log.e("kvnnvjvb",data.getStringExtra("text"));





        }

    }
    @Override
    public void onResume() {
        super.onResume();
        Log.e("gggggg",preferences.Ispaid(activity)+"");
        if(order_id!=0){
            if(preferences.Ispaid(activity)){
                paid(1);
            }
            else {
                paid(0);
            }}
    }
    private void paid(int i) {

        final ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url).setpaid(order_id,i).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                dialog.dismiss();
                if (response.isSuccessful()) {
                    if(i==1){
                        Toast.makeText(activity, getResources().getString(R.string.sucess), Toast.LENGTH_LONG).show();}
                    else {
                        Toast.makeText(activity, getResources().getString(R.string.order_sent), Toast.LENGTH_LONG).show();
                    }
                    activity.Displayorder();
                    // Common.CreateSignAlertDialog(activity, getResources().getString(R.string.sucess));

                } else {
                    Common.CreateSignAlertDialog(activity, getString(R.string.failed));

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
                    Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                    Log.e("Error", t.getMessage());
                } catch (Exception e) {
                }
            }
        });
    }

}
