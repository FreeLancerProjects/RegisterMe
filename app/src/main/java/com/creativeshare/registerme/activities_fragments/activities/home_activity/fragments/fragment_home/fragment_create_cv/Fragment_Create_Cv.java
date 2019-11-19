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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.Toast;

import com.creativeshare.registerme.R;
import com.creativeshare.registerme.activities_fragments.activities.home_activity.activity.Home_Activity;
import com.creativeshare.registerme.adapter.ImagesAdapter;
import com.creativeshare.registerme.adapter.Spinner_HandGrafuation_Adapter;
import com.creativeshare.registerme.adapter.Spinner_Qulificatin_Adapter;
import com.creativeshare.registerme.adapter.Spinner_Skills_Adapter;
import com.creativeshare.registerme.models.AllInFo_Model;
import com.creativeshare.registerme.models.UserModel;
import com.creativeshare.registerme.preferences.Preferences;
import com.creativeshare.registerme.remote.Api;
import com.creativeshare.registerme.share.Common;
import com.creativeshare.registerme.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    private List<AllInFo_Model.Data.Skills> skillsList;

    private RecyclerView recyclerView_images;
    private Spinner spinner_qualification, spinner_handgraduate, spinner_skill;
    private EditText edt_email,edt_note;
    private Button bt_Send;
private int qulifid=0,skillid=0,qradutateid=0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_cv, container, false);
        initView(view);
        get_cvinfo();
        return view;

    }

    private void initView(View view) {
        quallifcationList = new ArrayList<>();
        handGraduationsList = new ArrayList<>();
        skillsList = new ArrayList<>();
        uriList = new ArrayList<>();
        activity = (Home_Activity) getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);
        image_selection = view.findViewById(R.id.imageSelectPhoto);
        recyclerView_images = view.findViewById(R.id.recView);
        spinner_qualification = view.findViewById(R.id.spinner_qualification);
        spinner_handgraduate = view.findViewById(R.id.spinner_hanfgraduate);
        spinner_skill = view.findViewById(R.id.spinner_skill);
edt_email=view.findViewById(R.id.edt_email);
edt_note=view.findViewById(R.id.edt_note);
bt_Send=view.findViewById(R.id.btn_send);
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
                    qulifid =0 ;
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
                    qradutateid =0 ;
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
                if (position == 0) {
                    skillid =0 ;
                } else {
                    skillid = skillsList.get(position).getId();
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

    private void checkdata() {
        String email=edt_email.getText().toString();
        String note=edt_note.getText().toString();
        if(!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(note)&& Patterns.EMAIL_ADDRESS.matcher(email).matches()&&qulifid!=0&&qradutateid!=0&&skillid!=0){
if(uriList!=null&&uriList.size()>0){
 CreateCvWithImage(email,note);   
}
else {
    CreateCvWithOutImage(email,note);

}
        }
        else {
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                edt_email.setError(getResources().getString(R.string.inv_email));
            }
            if(TextUtils.isEmpty(email)){
                edt_email.setError(getResources().getString(R.string.field_req));
            }
            if(TextUtils.isEmpty(note)){
                edt_note.setError(getResources().getString(R.string.field_req));
            }
            if(qradutateid==0){
                Toast.makeText(activity,getResources().getString(R.string.choose_handgradauted),Toast.LENGTH_LONG).show();
            }
            if(qulifid==0){
                Toast.makeText(activity,getResources().getString(R.string.choose_qalified),Toast.LENGTH_LONG).show();
            }
            if(skillid==0){
                Toast.makeText(activity,getResources().getString(R.string.choose_Skill),Toast.LENGTH_LONG).show();
            }
        }
    }

    private void CreateCvWithOutImage(String email, String note) {
        final Dialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();



        try {
            Api.getService(Tags.base_url)
                    .createcvwithouimage(userModel.getUser().getId()+"", email,note,qulifid+"",qradutateid+"", skillid+"").enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    dialog.dismiss();
                    if (response.isSuccessful()) {
                        // Common.CreateSignAlertDialog(adsActivity,getResources().getString(R.string.suc));
                        Toast.makeText(activity, getString(R.string.suc), Toast.LENGTH_SHORT).show();
                        activity.Displayorder();

                        //  adsActivity.finish(response.body().getId_advertisement());

                    } else {
                        try {

                            Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            Log.e("Error", response.toString()+" "+response.code() + "" + response.message() + "" + response.errorBody() + response.raw() + response.body() + response.headers()+" "+response.errorBody().toString());
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

    private void CreateCvWithImage(String email, String note) {
        final Dialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();

        RequestBody user_part = Common.getRequestBodyText(userModel.getUser().getId() + "");

        RequestBody email_part = Common.getRequestBodyText(email);
        RequestBody note_part = Common.getRequestBodyText(note);
        RequestBody qualif_part = Common.getRequestBodyText(qulifid+"");
        RequestBody graduate_part = Common.getRequestBodyText(qradutateid+"");
        RequestBody skill_part = Common.getRequestBodyText(skillid+"");


        List<MultipartBody.Part> partimageList = getMultipartBodyList(uriList, "image[]");
        try {
            Api.getService(Tags.base_url)
                    .createcv(user_part, email_part,note_part,qualif_part,graduate_part, skill_part,partimageList).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    dialog.dismiss();
                    if (response.isSuccessful()) {
                        // Common.CreateSignAlertDialog(adsActivity,getResources().getString(R.string.suc));
                        Toast.makeText(activity, getString(R.string.suc), Toast.LENGTH_SHORT).show();
                        activity.Displayorder();

                        //  adsActivity.finish(response.body().getId_advertisement());

                    } else {
                        try {

                            Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            Log.e("Error", response.toString()+" "+response.code() + "" + response.message() + "" + response.errorBody() + response.raw() + response.body() + response.headers()+" "+response.errorBody().toString());
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
                    Toast.makeText(activity,getString(R.string.something), Toast.LENGTH_SHORT).show();

//error.setText(activity.getString(R.string.faild));
                    //recc.setVisibility(View.GONE);
                } catch (Exception e) {

                }

            }
        });

    }

    private void updatedata(AllInFo_Model body) {
        if(body.getData().getHandGraduations()!=null&&body.getData().getHandGraduations().size()>0){
            handGraduationsList.clear();
            handGraduationsList.add(new AllInFo_Model.Data.HandGraduations("اختر","choose"));
            handGraduationsList.addAll(body.getData().getHandGraduations());
        }
        if(body.getData().getQuallifcation()!=null&&body.getData().getQuallifcation().size()>0){
            quallifcationList.clear();
            quallifcationList.add(new AllInFo_Model.Data.Quallifcation("اختر","choose"));
            quallifcationList.addAll(body.getData().getQuallifcation());
        }
        if(body.getData().getSkills()!=null&&body.getData().getSkills().size()>0){
            skillsList.clear();
            skillsList.add(new AllInFo_Model.Data.Skills("اختر","choose"));
            skillsList.addAll(body.getData().getSkills());
        }
        spinner_skills_adapter.notifyDataSetChanged();
        spinner_handGrafuation_adapter.notifyDataSetChanged();
        spinner_qulificatin_adapter.notifyDataSetChanged();
    }


}
