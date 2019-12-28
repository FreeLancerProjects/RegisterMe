package com.creativeshare.registerme.activities_fragments.activities.home_activity.fragments.fragment_home.fragment_jobs;

import android.Manifest;
import android.app.Activity;
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

import com.creativeshare.registerme.R;
import com.creativeshare.registerme.activities_fragments.activities.home_activity.activity.Home_Activity;
import com.creativeshare.registerme.adapter.Sovernment_Job_Adapter;
import com.creativeshare.registerme.models.AllInFo_Model;
import com.creativeshare.registerme.models.UserModel;
import com.creativeshare.registerme.preferences.Preferences;
import com.creativeshare.registerme.remote.Api;
import com.creativeshare.registerme.share.Common;
import com.creativeshare.registerme.tags.Tags;
import com.jaiselrahman.filepicker.activity.FilePickerActivity;
import com.jaiselrahman.filepicker.config.Configurations;
import com.jaiselrahman.filepicker.model.MediaFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Fragment_Private_Sector extends Fragment {
    private RecyclerView rec_job;
    private List<AllInFo_Model.Data.Scompanies> dataList;
    private Sovernment_Job_Adapter sovernmentJob_adapter;
    private Home_Activity activity;
    private LinearLayout ll_no_store;
    private ProgressBar progBar;
    private Button bt_send;
    private int company_id=-1;
private Preferences preferences;
private UserModel userModel;
    private final int File_REQ1 = 1;
    private Uri fileUri1 = null;
    private final String READ_PERM = Manifest.permission.READ_EXTERNAL_STORAGE;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_private_sector, container, false);
        initView(view);
        get_Soverjob();
        return view;

    }

    private void initView(View view) {
        activity =(Home_Activity)getActivity();
        preferences=Preferences.getInstance();
        userModel=preferences.getUserData(activity);
        dataList=new ArrayList<>();
        sovernmentJob_adapter =new Sovernment_Job_Adapter(dataList, activity,this);
        rec_job=view.findViewById(R.id.recView);
        rec_job.setLayoutManager(new GridLayoutManager(activity,3));
        rec_job.setAdapter(sovernmentJob_adapter);
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

                    activity.Displayorder();

                  //  Common.CreateSignAlertDialog(activity, getResources().getString(R.string.sucess));

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
    public static Fragment_Private_Sector newInstance() {
        return new Fragment_Private_Sector();
    }
    private void get_Soverjob() {
        progBar.setVisibility(View.VISIBLE);
        Api.getService(Tags.base_url).get_Info().enqueue(new Callback<AllInFo_Model>() {
            @Override
            public void onResponse(Call<AllInFo_Model> call, Response<AllInFo_Model> response) {
                progBar.setVisibility(View.GONE);
                dataList.clear();
                if (response.isSuccessful()) {
                    if (response.body().getData().getScompanies().size() > 0) {
                        ll_no_store.setVisibility(View.GONE);
                        dataList.addAll(response.body().getData().getScompanies());
                        sovernmentJob_adapter.notifyDataSetChanged();
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
                //    tv_name.setText(fileUri1.getLastPathSegment());

            }
            //   image_upload.setVisibility(View.GONE);
            //    image_form.setImageDrawable(getResources().getDrawable(R.drawable.ic_document));
            //   image_form.setImageDrawable(matches.get(0).loadIcon(activity.getPackageManager()));
            //  tv_name.setText(fileUri1.get);
            //String type = data.getType();
            // Log.e("datass",type);
            // editImageProfile(userModel.getUser().getId()+"",fileUri1.toString());
            Company_odere();

        }

    }
}
