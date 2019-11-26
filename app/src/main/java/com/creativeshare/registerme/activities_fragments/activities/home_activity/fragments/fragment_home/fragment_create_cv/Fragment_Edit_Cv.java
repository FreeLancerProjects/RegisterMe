package com.creativeshare.registerme.activities_fragments.activities.home_activity.fragments.fragment_home.fragment_create_cv;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.SyncStateContract;
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

import com.creativeshare.registerme.R;
import com.creativeshare.registerme.activities_fragments.activities.home_activity.activity.Home_Activity;
import com.creativeshare.registerme.adapter.SkillAdapter;
import com.creativeshare.registerme.adapter.Spinner_HandGrafuation_Adapter;
import com.creativeshare.registerme.adapter.Spinner_Qulificatin_Adapter;
import com.creativeshare.registerme.adapter.Spinner_Skills_Adapter;
import com.creativeshare.registerme.models.AllInFo_Model;
import com.creativeshare.registerme.models.UserModel;
import com.creativeshare.registerme.preferences.Preferences;
import com.creativeshare.registerme.remote.Api;
import com.creativeshare.registerme.share.Common;
import com.creativeshare.registerme.tags.Tags;
import com.jaiselrahman.filepicker.activity.FilePickerActivity;
import com.jaiselrahman.filepicker.config.Configurations;
import com.jaiselrahman.filepicker.model.MediaFile;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Fragment_Edit_Cv extends Fragment {
    //private ImageView image_selection;
    private Preferences preferences;
    private UserModel userModel;
    private Home_Activity activity;

    private Spinner_Qulificatin_Adapter spinner_qulificatin_adapter;
    private Spinner_HandGrafuation_Adapter spinner_handGrafuation_adapter;
    private Spinner_Skills_Adapter spinner_skills_adapter;
    private RecyclerView recyclerViewskil;
    private SkillAdapter skillAdapter;

    private List<AllInFo_Model.Data.Quallifcation> quallifcationList;
    private List<AllInFo_Model.Data.HandGraduations> handGraduationsList;
    private List<AllInFo_Model.Data.Skills> skillsList,skills;
    private final int File_REQ1 = 1;
    private Uri fileUri1 = null;
    private final String READ_PERM = Manifest.permission.READ_EXTERNAL_STORAGE;
    //private RecyclerView recyclerView_images;
    private Spinner spinner_qualification, spinner_handgraduate, spinner_skill;
    private EditText edt_email, edt_note, edt_phone, edt_name;
    private Button bt_Send;
    private ImageView image_upload;
    private RoundedImageView image_form;
    private TextView tv_name;
    private int qulifid = 0, qradutateid = 0;
    private List<ResolveInfo> matches;
    private List<Integer> skillid;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_cv, container, false);
        initView(view);
        get_cvinfo();
        return view;

    }


    private void initView(View view) {
        quallifcationList = new ArrayList<>();
        handGraduationsList = new ArrayList<>();
        skillsList = new ArrayList<>();
        skills=new ArrayList<>();
        skillid=new ArrayList<>();
        activity = (Home_Activity) getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);
        //  image_selection = view.findViewById(R.id.imageSelectPhoto);
        //recyclerView_images = view.findViewById(R.id.recView);
        spinner_qualification = view.findViewById(R.id.spinner_qualification);
        spinner_handgraduate = view.findViewById(R.id.spinner_hanfgraduate);
        spinner_skill = view.findViewById(R.id.spinner_skill);
        tv_name = view.findViewById(R.id.tv_name);
        edt_email = view.findViewById(R.id.edt_email);
        edt_note = view.findViewById(R.id.edt_note);
        edt_phone = view.findViewById(R.id.edt_phone);
        edt_name = view.findViewById(R.id.edt_name);
        bt_Send = view.findViewById(R.id.btn_send);
        image_upload = view.findViewById(R.id.icon_form);
        image_form = view.findViewById(R.id.image_form);
        spinner_qulificatin_adapter = new Spinner_Qulificatin_Adapter(activity, quallifcationList);
        spinner_handGrafuation_adapter = new Spinner_HandGrafuation_Adapter(activity, handGraduationsList);
        spinner_skills_adapter = new Spinner_Skills_Adapter(activity, skillsList);
        spinner_qualification.setAdapter(spinner_qulificatin_adapter);
        spinner_handgraduate.setAdapter(spinner_handGrafuation_adapter);
        spinner_skill.setAdapter(spinner_skills_adapter);
        recyclerViewskil=view.findViewById(R.id.recViewservice);
        recyclerViewskil.setLayoutManager(new GridLayoutManager(activity,3));
        skillAdapter=new SkillAdapter(skills,activity,this);
        recyclerViewskil.setAdapter(skillAdapter);
        image_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckReadPermission();
            }
        });
        image_form.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckReadPermission();
            }
        });

        // recyclerView_images.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false));

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
        if(userModel!=null){
            if(userModel.getUser().getEmail()!=null){
                edt_email.setText(userModel.getUser().getEmail());
            }
            edt_name.setText(userModel.getUser().getName());
            edt_phone.setText(userModel.getUser().getPhone());
        }
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
        MultipartBody.Part image_part = Common.getMultiPartdoc(activity, Uri.parse(fileUri1.toString()), "cv");
        try {
            Api.getService(Tags.base_url)
                    .updatecv(user_part, name_part, phone_part, email_part, note_part, qualif_part, graduate_part, skill_part, image_part).enqueue(new Callback<ResponseBody>() {
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

    private void checkdata() {
        String email = edt_email.getText().toString();
        String note = edt_note.getText().toString();
        String name = edt_name.getText().toString();
        String phone = edt_phone.getText().toString();
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(phone) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(note) && Patterns.EMAIL_ADDRESS.matcher(email).matches() && qulifid != 0 && qradutateid != 0 && skillid != null&&skillid.size()>0 && fileUri1 != null) {
            if (userModel == null) {
                Common.CreateUserNotSignInAlertDialog(activity);
            } else {
                CreateCvWithImage(email, note, name, phone);
            }

        } else {
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                edt_email.setError(getResources().getString(R.string.inv_email));
            }
            if (TextUtils.isEmpty(name)) {
                edt_name.setError(getResources().getString(R.string.field_req));
            }
            if (TextUtils.isEmpty(phone)) {
                edt_phone.setError(getResources().getString(R.string.field_req));
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
            if (fileUri1 == null) {
                Toast.makeText(activity, getResources().getString(R.string.attach_previous_cv), Toast.LENGTH_LONG).show();
            }
        }
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

    public static Fragment_Edit_Cv newInstance() {
        return new Fragment_Edit_Cv();
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
        Intent intent = new Intent(activity, FilePickerActivity.class);
        intent.putExtra(FilePickerActivity.CONFIGS, new Configurations.Builder()
                .setCheckPermission(true)
                .enableImageCapture(false).setShowImages(false).setShowAudios(false).setShowVideos(false)
                .setMaxSelection(1)
                .setSkipZeroSizeFiles(true).setShowFiles(true)
                .build());
        startActivityForResult(intent, file_req);

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
            ArrayList<MediaFile> files = data.getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES);
            for(int i=0;i<files.size();i++){
                fileUri1=files.get(i).getUri();;
                tv_name.setText(fileUri1.getLastPathSegment());

            }
            image_upload.setVisibility(View.GONE);
            image_form.setImageDrawable(getResources().getDrawable(R.drawable.ic_document));
            //   image_form.setImageDrawable(matches.get(0).loadIcon(activity.getPackageManager()));
            //  tv_name.setText(fileUri1.get);
            //String type = data.getType();
            // Log.e("datass",type);
            // editImageProfile(userModel.getUser().getId()+"",fileUri1.toString());


        }

    }
    private boolean notidisfound(int position) {
        for(int i=0;i<skillid.size();i++){
            if(skillsList.get(position).getId()==skillid.get(i)){
                return false;
            }
        }
        return true;
    }
    public void deletitem(int layoutPosition) {
        skills.remove(layoutPosition);
        skillid.remove(layoutPosition);
        skillAdapter.notifyDataSetChanged();
    }
}
