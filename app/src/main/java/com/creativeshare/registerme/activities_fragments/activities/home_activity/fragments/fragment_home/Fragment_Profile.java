package com.creativeshare.registerme.activities_fragments.activities.home_activity.fragments.fragment_home;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.creativeshare.registerme.R;
import com.creativeshare.registerme.activities_fragments.activities.home_activity.activity.Home_Activity;
import com.creativeshare.registerme.models.UserModel;
import com.creativeshare.registerme.preferences.Preferences;
import com.creativeshare.registerme.tags.Tags;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;


public class Fragment_Profile extends Fragment {



   private Preferences preferences;
   private Home_Activity activity;
   private UserModel userModel;

private TextView tv_name,tv_phone;
private CircleImageView im_user;
    public static Fragment_Profile newInstance() {

        return new Fragment_Profile();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile, container, false);
        intitview(view);
        if(userModel!=null){
            updateprofile(userModel);
        }
        return view;
    }

    private void updateprofile(UserModel userModel) {
        this.userModel=userModel;
        tv_name.setText(userModel.getUser().getName());
        tv_phone.setText(userModel.getUser().getPhone());
        Picasso.with(activity).load(Uri.parse(Tags.IMAGE_URL+userModel.getUser().getImage())).fit().into(im_user);

    }

    private void intitview(View view) {
        activity=(Home_Activity)activity;
        Paper.init(activity);
        preferences=Preferences.getInstance();
        userModel=preferences.getUserData(activity);
        tv_name=view.findViewById(R.id.tv_name);
        tv_phone=view.findViewById(R.id.tv_phone);
        im_user=view.findViewById(R.id.image);
    }


}
