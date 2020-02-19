package com.endpoint.registerme.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;


import com.endpoint.registerme.R;
import com.endpoint.registerme.activities_fragments.activities.home_activity.fragments.fragment_home.fragment_create_cv.Fragment_Create_Cv;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.MyHolder> {

    private List<Uri> urlList;
    private Context context;
private Fragment_Create_Cv fragment_create_cv;
private Fragment fragment;
    public ImagesAdapter(List<Uri> urlList, Context context, Fragment fragment) {
        this.urlList = urlList;
        this.context = context;
this.fragment=fragment;


    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_row,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int position) {

        Uri url = urlList.get(position);

        Picasso.with(context).load(url).fit().into(holder.ivGallery);
        holder.delete.setOnClickListener(view -> {
                   if(fragment instanceof Fragment_Create_Cv){
                       fragment_create_cv=(Fragment_Create_Cv)fragment;
fragment_create_cv.deleteImage(holder.getLayoutPosition());
                   }

                }
        );

    }

    @Override
    public int getItemCount() {
        return urlList.size();
    }
    public class MyHolder extends RecyclerView.ViewHolder {

        private RoundedImageView ivGallery;
        private ImageView delete;


        public MyHolder(View itemView) {
            super(itemView);
            ivGallery = itemView.findViewById(R.id.image);
            delete=itemView.findViewById(R.id.imageDelete);
        }


    }
}
