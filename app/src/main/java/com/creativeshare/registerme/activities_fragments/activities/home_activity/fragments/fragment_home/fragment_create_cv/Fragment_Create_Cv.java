package com.creativeshare.registerme.activities_fragments.activities.home_activity.fragments.fragment_home.fragment_create_cv;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creativeshare.registerme.R;
import com.creativeshare.registerme.activities_fragments.activities.home_activity.activity.Home_Activity;
import com.creativeshare.registerme.adapter.ImagesAdapter;
import com.creativeshare.registerme.adapter.SkillAdapter;
import com.creativeshare.registerme.adapter.Spinner_HandGrafuation_Adapter;
import com.creativeshare.registerme.adapter.Spinner_Qulificatin_Adapter;
import com.creativeshare.registerme.adapter.Spinner_Skills_Adapter;
import com.creativeshare.registerme.models.AllInFo_Model;
import com.creativeshare.registerme.models.ImageTypeModel;
import com.creativeshare.registerme.models.ServicePriceModel;
import com.creativeshare.registerme.models.UserModel;
import com.creativeshare.registerme.preferences.Preferences;
import com.creativeshare.registerme.remote.Api;
import com.creativeshare.registerme.share.Common;
import com.creativeshare.registerme.tags.Tags;
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
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Fragment_Create_Cv extends Fragment {
    private ImageView image_selection;
    private Preferences preferences;
    private UserModel userModel;
    private Home_Activity activity;
    private final int IMG2 = 2;
    private final String read_permission = Manifest.permission.READ_EXTERNAL_STORAGE;
    private ImagesAdapter galleryAdapter;
    private Spinner_Qulificatin_Adapter spinner_qulificatin_adapter;
    private Spinner_HandGrafuation_Adapter spinner_handGrafuation_adapter;
    private Spinner_Skills_Adapter spinner_skills_adapter;

    private List<Uri> uriList;
    private List<AllInFo_Model.Data.Quallifcation> quallifcationList;
    private List<AllInFo_Model.Data.HandGraduations> handGraduationsList;
    private List<AllInFo_Model.Data.Skills> skillsList,skills;

    private RecyclerView recyclerView_images,recyclerViewskil;
private SkillAdapter skillAdapter;
    private Spinner spinner_qualification, spinner_handgraduate, spinner_skill;
    private EditText edt_email, edt_note, edt_phone, edt_name;
    private TextView tv_type;
    private Button bt_Send;
    private int qulifid = 0,  qradutateid = 0;
    private List<Integer> skillid;
    private String current_lang;
    private ServicePriceModel servicepricemodel;
    public static final String KEY = "gQq9M^TFrFs~FJPr";        // TODO: Insert your Key here
    public static final String STORE_ID = "22865";    // TODO: Insert your Store ID here
    public static final String EMAIL= "al-waafi8567@hotmail.com";     // TODO: Insert the customer email here


    public static final boolean isSecurityEnabled = false;
    private int order_id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_cv, container, false);
        initView(view);
        get_cvinfo();
        get_cvImagetype();
        getserviceprive();
        return view;

    }

    private void initView(View view) {
        quallifcationList = new ArrayList<>();
        handGraduationsList = new ArrayList<>();
        skills=new ArrayList<>();
        skillsList = new ArrayList<>();
        uriList = new ArrayList<>();
        skillid=new ArrayList<>();
        activity = (Home_Activity) getActivity();
        Paper.init(activity);
        current_lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);
        image_selection = view.findViewById(R.id.imageSelectPhoto);
        recyclerView_images = view.findViewById(R.id.recView);
        spinner_qualification = view.findViewById(R.id.spinner_qualification);
        spinner_handgraduate = view.findViewById(R.id.spinner_hanfgraduate);
        spinner_skill = view.findViewById(R.id.spinner_skill);
        tv_type = view.findViewById(R.id.tv_type);
        edt_email = view.findViewById(R.id.edt_email);
        edt_phone = view.findViewById(R.id.edt_phone);
        edt_name = view.findViewById(R.id.edt_name);
        edt_note = view.findViewById(R.id.edt_note);
        bt_Send = view.findViewById(R.id.btn_send);
        recyclerViewskil=view.findViewById(R.id.recViewservice);
        recyclerViewskil.setLayoutManager(new GridLayoutManager(activity,3));
        skillAdapter=new SkillAdapter(skills,activity,this);
        recyclerViewskil.setAdapter(skillAdapter);
        if(userModel!=null){
            if(userModel.getUser().getEmail()!=null){
                edt_email.setText(userModel.getUser().getEmail());
            }
            edt_name.setText(userModel.getUser().getName());
            edt_phone.setText(userModel.getUser().getPhone());
        }
        spinner_qulificatin_adapter = new Spinner_Qulificatin_Adapter(activity, quallifcationList);
        spinner_handGrafuation_adapter = new Spinner_HandGrafuation_Adapter(activity, handGraduationsList);
        spinner_skills_adapter = new Spinner_Skills_Adapter(activity, skillsList);
        spinner_qualification.setAdapter(spinner_qulificatin_adapter);
        spinner_handgraduate.setAdapter(spinner_handGrafuation_adapter);
        spinner_skill.setAdapter(spinner_skills_adapter);
        image_selection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Check_ReadPermission(IMG2);
            }
        });


        recyclerView_images.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false));
        galleryAdapter = new ImagesAdapter(uriList, activity, this);
        recyclerView_images.setAdapter(galleryAdapter);
        spinner_qualification.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    qulifid = 0;
                } else {
                    qulifid = quallifcationList.get(position).getId();
                    // Move_Data_Model.setcityt(to_city);


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_handgraduate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    qradutateid = 0;
                } else {
                    qradutateid = handGraduationsList.get(position).getId();
                    // Move_Data_Model.setcityt(to_city);


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_skill.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0) {
                    if(notidisfound(position)){
                    skillid.add( skillsList.get(position).getId());
                    skills.add(skillsList.get(position));
                    skillAdapter.notifyDataSetChanged();
                    recyclerViewskil.setVisibility(View.VISIBLE);
                    }
                    // Move_Data_Model.setcityt(to_city);


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        bt_Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkdata();
            }
        });
    }

    private boolean notidisfound(int position) {
        for(int i=0;i<skillid.size();i++){
            if(skillsList.get(position).getId()==skillid.get(i)){
                return false;
            }
        }
        return true;
    }

    private void checkdata() {
        String email = edt_email.getText().toString();
        String note = edt_note.getText().toString();
        String name = edt_name.getText().toString();
        String phone = edt_phone.getText().toString();
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(phone) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(note) && Patterns.EMAIL_ADDRESS.matcher(email).matches() && qulifid != 0 && qradutateid != 0 && skillid != null&&skillid.size()>0) {
            if (uriList != null && uriList.size() > 0) {
                Common.CloseKeyBoard(activity,edt_phone);
                if (userModel == null) {
                    Common.CreateUserNotSignInAlertDialog(activity);
                } else {
                    CreateCvWithImage(email, note, name, phone);
                }
            } else {

                if (userModel == null) {
                    Common.CreateUserNotSignInAlertDialog(activity);
                } else {
                    CreateCvWithOutImage(email, note, name, phone);
                }

            }
        } else {
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                edt_email.setError(getResources().getString(R.string.inv_email));
            }
            if (TextUtils.isEmpty(email)) {
                edt_email.setError(getResources().getString(R.string.field_req));
            }
            if (TextUtils.isEmpty(note)) {
                edt_note.setError(getResources().getString(R.string.field_req));
            }
            if (qradutateid == 0) {
                Toast.makeText(activity, getResources().getString(R.string.choose_handgradauted), Toast.LENGTH_LONG).show();
            }
            if (qulifid == 0) {
                Toast.makeText(activity, getResources().getString(R.string.choose_qalified), Toast.LENGTH_LONG).show();
            }
            if (skillid == null||skillid.size()==0) {
                Toast.makeText(activity, getResources().getString(R.string.choose_Skill), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void CreateCvWithOutImage(String email, String note, String name, String phone) {
        final Dialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();


        try {
            Api.getService(Tags.base_url)
                    .createcvwithouimage(userModel.getUser().getId() + "", name, phone, email, note, qulifid + "", qradutateid + "", skillid ).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    dialog.dismiss();
                    if (response.isSuccessful()) {
                        // Common.CreateSignAlertDialog(adsActivity,getResources().getString(R.string.suc));
                        Toast.makeText(activity, getString(R.string.suc), Toast.LENGTH_SHORT).show();
                      //  activity.Displayorder();
sendMessage(response.body());
                        //  adsActivity.finish(response.body().getId_advertisement());

                    } else {
                        try {

                            Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            Log.e("Error", response.toString() + " " + response.code() + "" + response.message() + "" + response.errorBody() + response.raw() + response.body() + response.headers() + " " + response.errorBody().toString());
                        } catch (Exception e) {


                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    dialog.dismiss();
                    try {
                        Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                        Log.e("Error", t.getMessage());
                    } catch (Exception e) {

                    }
                }
            });
        } catch (Exception e) {
            dialog.dismiss();
            Log.e("error", e.getMessage().toString());
        }
    }

    private void CreateCvWithImage(String email, String note, String name, String phone) {
        final Dialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();

        RequestBody user_part = Common.getRequestBodyText(userModel.getUser().getId() + "");
        RequestBody name_part = Common.getRequestBodyText(name);
        RequestBody phone_part = Common.getRequestBodyText(phone);
        RequestBody email_part = Common.getRequestBodyText(email);
        RequestBody note_part = Common.getRequestBodyText(note);
        RequestBody qualif_part = Common.getRequestBodyText(qulifid + "");
        RequestBody graduate_part = Common.getRequestBodyText(qradutateid + "");

        List<RequestBody> skill_part = new ArrayList<>();
        for(int i=0;i<skillid.size();i++){
            skill_part.add(Common.getRequestBodyText(skillid.get(i)+""));
        }


        List<MultipartBody.Part> partimageList = getMultipartBodyList(uriList, "image[]");
        try {
            Api.getService(Tags.base_url)
                    .createcv(user_part, name_part, phone_part, email_part, note_part, qualif_part, graduate_part, skill_part, partimageList).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    dialog.dismiss();
                    if (response.isSuccessful()) {
                        // Common.CreateSignAlertDialog(adsActivity,getResources().getString(R.string.suc));
                        Toast.makeText(activity, getString(R.string.suc), Toast.LENGTH_SHORT).show();
                       // activity.Displayorder();
                        sendMessage(response.body());
                        //  adsActivity.finish(response.body().getId_advertisement());

                    } else {
                        try {

                            Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            Log.e("Error", response.toString() + " " + response.code() + "" + response.message() + "" + response.errorBody() + response.raw() + response.body() + response.headers() + " " + response.errorBody().toString());
                        } catch (Exception e) {


                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    dialog.dismiss();
                    try {
                        Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                        Log.e("Error", t.getMessage());
                    } catch (Exception e) {

                    }
                }
            });
        } catch (Exception e) {
            dialog.dismiss();
            Log.e("error", e.getMessage().toString());
        }
    }

    public static Fragment_Create_Cv newInstance() {
        return new Fragment_Create_Cv();
    }

    private void Check_ReadPermission(int img_req) {
        if (ContextCompat.checkSelfPermission(activity, read_permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{read_permission}, img_req);
        } else {
            select_photo(img_req);
        }
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

    private List<MultipartBody.Part> getMultipartBodyList(List<Uri> uriList, String image_cv) {
        List<MultipartBody.Part> partList = new ArrayList<>();
        for (Uri uri : uriList) {
            MultipartBody.Part part = Common.getMultiPart(activity, uri, image_cv);
            partList.add(part);
        }
        return partList;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG2 && resultCode == Activity.RESULT_OK && data != null) {

            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            if (data.getData() != null) {

                Uri mImageUri = data.getData();

                // Get the cursor
                Cursor cursor = activity.getContentResolver().query(mImageUri,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();


                cursor.close();

                ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                mArrayUri.add(mImageUri);

                uriList.addAll(mArrayUri);
                galleryAdapter.notifyDataSetChanged();

                //     uploadCV();
            } else {

                if (data.getClipData() != null) {
                    ClipData mClipData = data.getClipData();
                    ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                    for (int i = 0; i < mClipData.getItemCount(); i++) {

                        ClipData.Item item = mClipData.getItemAt(i);
                        Uri uri = item.getUri();
                        mArrayUri.add(uri);
                        // Get the cursor
                        Cursor cursor = activity.getContentResolver().query(uri, filePathColumn, null, null, null);
                        // Move to first row
                        cursor.moveToFirst();


                        cursor.close();
                    }
                    uriList.addAll(mArrayUri);


                    galleryAdapter.notifyDataSetChanged();
                }

            }
        }
        else  if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {

            Log.e("kvnnvjvb",data.getStringExtra("text"));





        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == IMG2) {
            if (grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    select_photo(IMG2);
                } else {
                    Toast.makeText(activity, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void deleteImage(int adapterPosition) {
        uriList.remove(adapterPosition);
        galleryAdapter.notifyItemRemoved(adapterPosition);

    }

    private void get_cvinfo() {
        final ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url).get_Info().enqueue(new Callback<AllInFo_Model>() {
            @Override
            public void onResponse(Call<AllInFo_Model> call, Response<AllInFo_Model> response) {
                dialog.dismiss();
                //  dataList.clear();
                if (response.isSuccessful()) {
                    updatedata(response.body());
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
                    Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();

//error.setText(activity.getString(R.string.faild));
                    //recc.setVisibility(View.GONE);
                } catch (Exception e) {

                }

            }
        });

    }

    private void get_cvImagetype() {
        final ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url).get_Inmagetype().enqueue(new Callback<ImageTypeModel>() {
            @Override
            public void onResponse(Call<ImageTypeModel> call, Response<ImageTypeModel> response) {
                dialog.dismiss();
                //  dataList.clear();
                if (response.isSuccessful()) {
                    updateImagetype(response.body());
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
            public void onFailure(Call<ImageTypeModel> call, Throwable t) {
                try {
                    Log.e("Error", t.getMessage());
                    dialog.dismiss();
                    Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();

//error.setText(activity.getString(R.string.faild));
                    //recc.setVisibility(View.GONE);
                } catch (Exception e) {

                }

            }
        });

    }

    private void updatedata(AllInFo_Model body) {
        if (body.getData().getHandGraduations() != null && body.getData().getHandGraduations().size() > 0) {
            handGraduationsList.clear();
            handGraduationsList.add(new AllInFo_Model.Data.HandGraduations("اختر", "choose"));
            handGraduationsList.addAll(body.getData().getHandGraduations());
        }
        if (body.getData().getQuallifcation() != null && body.getData().getQuallifcation().size() > 0) {
            quallifcationList.clear();
            quallifcationList.add(new AllInFo_Model.Data.Quallifcation("اختر", "choose"));
            quallifcationList.addAll(body.getData().getQuallifcation());
        }
        if (body.getData().getSkills() != null && body.getData().getSkills().size() > 0) {
            skillsList.clear();
            skillsList.add(new AllInFo_Model.Data.Skills("اختر", "choose"));
            skillsList.addAll(body.getData().getSkills());
        }
        spinner_skills_adapter.notifyDataSetChanged();
        spinner_handGrafuation_adapter.notifyDataSetChanged();
        spinner_qulificatin_adapter.notifyDataSetChanged();
    }

    private void updateImagetype(ImageTypeModel body) {
        String type = getResources().getString(R.string.upload_image) + ":";
        if (body.getData() != null && body.getData().size() > 0) {
            for (int i = 0; i < body.getData().size(); i++) {
                if (current_lang.equals("ar")) {
                    type += body.getData().get(i).getAr_title();
                } else {
                    type += body.getData().get(i).getEn_title();

                }
                if (i < body.getData().size() - 1) {
                    type += ",";
                }
            }

            tv_type.setText(type);
        }
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
        app.setName("Create Cv");                    // Application name
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
        tran.setAmount(servicepricemodel.getCreate_cv());                         // Transaction amount : The transaction amount must be sent in major units, for example 9 dollars 50 cents must be sent as 9.50 not 950. There must be no currency symbol, and no thousands separators. Thedecimal part must be separated using a dot.
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


    public void deletitem(int layoutPosition) {
        skills.remove(layoutPosition);
        skillid.remove(layoutPosition);
        skillAdapter.notifyDataSetChanged();
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
