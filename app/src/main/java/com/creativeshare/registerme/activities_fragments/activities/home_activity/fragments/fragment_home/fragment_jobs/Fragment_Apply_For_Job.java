package com.creativeshare.registerme.activities_fragments.activities.home_activity.fragments.fragment_home.fragment_jobs;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.creativeshare.registerme.R;
import com.creativeshare.registerme.activities_fragments.activities.home_activity.activity.Home_Activity;
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
    private final int File_REQ1 = 1;
    private Uri fileUri1 = null;
    private final String READ_PERM = Manifest.permission.READ_EXTERNAL_STORAGE;
    private String link;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_apply_for_job, container, false);
        initView(view);
        return view;

    }

    private void initView(View view) {
activity =(Home_Activity)getActivity();
        Paper.init(activity);
        preferences=Preferences.getInstance();
        userModel=preferences.getUserData(activity);
        edt_link=view.findViewById(R.id.edt_link);
        bt_send=view.findViewById(R.id.btn_send);
        bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chechdata();
            }
        });


    }

    private void chechdata() {
      link=edt_link.getText().toString();
        if(!TextUtils.isEmpty(link)){
            edt_link.setError(null);
            if(userModel!=null){
CheckReadPermission();
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

                    activity.Displayorder();

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
            Send_link(link);


        }

    }
}
