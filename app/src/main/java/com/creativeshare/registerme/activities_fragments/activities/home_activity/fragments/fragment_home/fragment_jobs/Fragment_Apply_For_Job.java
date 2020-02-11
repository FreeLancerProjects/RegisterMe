package com.creativeshare.registerme.activities_fragments.activities.home_activity.fragments.fragment_home.fragment_jobs;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.aditya.filebrowser.Constants;
import com.aditya.filebrowser.FileChooser;
import com.creativeshare.registerme.R;
import com.creativeshare.registerme.activities_fragments.activities.home_activity.activity.Home_Activity;
import com.creativeshare.registerme.models.ServicePriceModel;
import com.creativeshare.registerme.models.UserModel;
import com.creativeshare.registerme.preferences.Preferences;
import com.creativeshare.registerme.remote.Api;
import com.creativeshare.registerme.share.Common;
import com.creativeshare.registerme.tags.Tags;
import com.jaiselrahman.filepicker.config.Configurations;
import com.jaiselrahman.filepicker.model.MediaFile;
import com.nononsenseapps.filepicker.FilePickerActivity;
import com.nononsenseapps.filepicker.Utils;
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
import java.util.regex.Pattern;

import io.paperdb.Paper;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Fragment_Apply_For_Job extends Fragment {
private UserModel userModel;
private Preferences preferences;
private Home_Activity activity;
private EditText edt_link;
private Button bt_send;
    private final int File_REQ1 = 2;
    private Uri fileUri1 = null;
    private final String READ_PERM = Manifest.permission.READ_EXTERNAL_STORAGE;
    private String link;
    private ServicePriceModel servicepricemodel;
    public static final String KEY = "gQq9M^TFrFs~FJPr";        // TODO: Insert your Key here
    public static final String STORE_ID = "22865";    // TODO: Insert your Store ID here
    public static final String EMAIL= "al-waafi8567@hotmail.com";
    public static final boolean isSecurityEnabled = false;
    private int order_id;
    private TextView tv_price;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_apply_for_job, container, false);
        initView(view);
        getserviceprive();
        return view;

    }

    private void initView(View view) {
activity =(Home_Activity)getActivity();
        Paper.init(activity);
        preferences= Preferences.getInstance();
        userModel=preferences.getUserData(activity);
        edt_link=view.findViewById(R.id.edt_link);
        bt_send=view.findViewById(R.id.btn_send);
        tv_price=view.findViewById(R.id.tv_price);
        bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chechdata();
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
        tv_price.setText(activity.getResources().getString(R.string.price)+servicepricemodel.getApply_job());

    }
    private void chechdata() {
      link=edt_link.getText().toString();
        if(!TextUtils.isEmpty(link)){
            edt_link.setError(null);
            if(userModel!=null){
CreateUserNotSignInAlertDialog(activity);
            }
            else {
                Common.CreateUserNotSignInAlertDialog(activity);
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
Log.e("kkkkkk",fileUri1.toString());
        final ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        RequestBody name_part = Common.getRequestBodyText(userModel.getUser().getId()+"");

        RequestBody link_part = Common.getRequestBodyText(link);
        MultipartBody.Part image_part = Common.getMultiPartdoc(activity, Uri.parse(fileUri1.toString()), "cv");

        Api.getService(Tags.base_url).send_link(name_part,link_part,image_part).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                dialog.dismiss();
                if (response.isSuccessful()) {
                    Toast.makeText(activity,getResources().getString(R.string.sucess),Toast.LENGTH_LONG).show();
                    sendMessage(response.body());
//                    activity.Displayorder();

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
    private void Send_linkwithoutimage(String link) {
        Log.e("kkkkkk",fileUri1.toString());
        final ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();


        Api.getService(Tags.base_url).send_link_without_image(userModel.getUser().getId()+"",link).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                dialog.dismiss();
                if (response.isSuccessful()) {
                    Toast.makeText(activity,getResources().getString(R.string.sucess),Toast.LENGTH_LONG).show();
                    sendMessage(response.body());
//                    activity.Displayorder();

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
    public static Fragment_Apply_For_Job newInstance() {
        return new Fragment_Apply_For_Job();
    }
    private void CheckReadPermission() {
        if (ActivityCompat.checkSelfPermission(activity, READ_PERM) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{READ_PERM}, File_REQ1);
        } else {
            select_photo(File_REQ1);
        }
    }
    public  void CreateUserNotSignInAlertDialog(final Context context)
    {


        final AlertDialog dialog = new AlertDialog.Builder(context)
                .setCancelable(true)
                .create();


        View view = LayoutInflater.from(context).inflate(R.layout.question_dialog,null);
        Button btn_sign_in = view.findViewById(R.id.btn_sign_in);
        Button btn_cancel = view.findViewById(R.id.btn_cancel);

        TextView tv_msg = view.findViewById(R.id.tv_msg);
        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();


                    CheckReadPermission();



            }
        });


        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Send_linkwithoutimage(link);
            }
        });

        dialog.getWindow().getAttributes().windowAnimations= R.style.dialog_congratulation_animation;
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(view);
        dialog.show();
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
//        Intent intent = new Intent(activity, FilePickerActivity.class);
//        intent.putExtra(FilePickerActivity.CONFIGS, new Configurations.Builder()
//                .setCheckPermission(true)
//                .enableImageCapture(false).setShowImages(true).setShowAudios(false).setShowVideos(false)
//                .setMaxSelection(1).setSuffixes("pdf").enableImageCapture(true)
//                .setSkipZeroSizeFiles(true).setShowFiles(true)
//                .build());
//        startActivityForResult(intent, file_req);
        Intent i2 = new Intent(activity, FileChooser.class);
        i2.putExtra(Constants.SELECTION_MODE, Constants.SELECTION_MODES.SINGLE_SELECTION.ordinal());
        startActivityForResult(i2, File_REQ1);

    }
    private void select_photo(int img1) {
        Intent intent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            if (img1 == 2) {
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            }
        } else {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            if (img1 == 2) {
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            }

        }
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setType("image/*");
        startActivityForResult(intent, img1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == File_REQ1 && resultCode == Activity.RESULT_OK && data != null) {
            // matches = activity.getPackageManager().queryIntentActivities(data, 0);
//            List<Uri> files = Utils.getSelectedFilesFromResult(data);
//            for(int i=0;i<files.size();i++){
//                fileUri1=files.get(i);;
//            //    tv_name.setText(fileUri1.getLastPathSegment());
//
//            }
            fileUri1 = data.getData();

            //   image_upload.setVisibility(View.GONE);
            //    image_form.setImageDrawable(getResources().getDrawable(R.drawable.ic_document));
            //   image_form.setImageDrawable(matches.get(0).loadIcon(activity.getPackageManager()));
            //  tv_name.setText(fileUri1.get);
            //String type = data.getType();
            // Log.e("datass",type);
            // editImageProfile(userModel.getUser().getId()+"",fileUri1.toString());
            Send_link(link);


        }
        else  if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {

            Log.e("kvnnvjvb",data.getStringExtra("text"));





        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == File_REQ1) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                select_photo(File_REQ1);
            } else {
                Toast.makeText(activity, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
            }

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
                    }                      activity.Displayorder();
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
