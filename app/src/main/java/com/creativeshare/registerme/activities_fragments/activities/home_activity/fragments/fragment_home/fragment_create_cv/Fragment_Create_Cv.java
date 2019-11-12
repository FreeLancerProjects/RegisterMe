package com.creativeshare.registerme.activities_fragments.activities.home_activity.fragments.fragment_home.fragment_create_cv;

import android.Manifest;
import android.app.Activity;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.creativeshare.registerme.R;
import com.creativeshare.registerme.activities_fragments.activities.home_activity.activity.Home_Activity;
import com.creativeshare.registerme.adapter.ImagesAdapter;
import com.creativeshare.registerme.models.UserModel;
import com.creativeshare.registerme.preferences.Preferences;
import com.creativeshare.registerme.share.Common;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;


public class Fragment_Create_Cv extends Fragment {
private ImageView image_selection;
private Preferences preferences;
private UserModel userModel;
private Home_Activity activity;
    private final int  IMG2 = 2;
    private final String read_permission = Manifest.permission.READ_EXTERNAL_STORAGE;
    private ImagesAdapter galleryAdapter;
    private List<Uri> uriList;
    private RecyclerView recyclerView_images;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_cv, container, false);
        initView(view);
        return view;

    }

    private void initView(View view) {
        uriList=new ArrayList<>();
activity=(Home_Activity)getActivity();
preferences=Preferences.getInstance();
userModel=preferences.getUserData(activity);
image_selection=view.findViewById(R.id.imageSelectPhoto);
recyclerView_images=view.findViewById(R.id.recView);
image_selection.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Check_ReadPermission(IMG2);
    }
});


recyclerView_images.setLayoutManager(new LinearLayoutManager(activity,RecyclerView.HORIZONTAL,false));
        galleryAdapter = new ImagesAdapter(uriList,activity,this);
       recyclerView_images.setAdapter(galleryAdapter);


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
            MultipartBody.Part part = Common.getMultiPart(activity,uri,image_cv);
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



}
