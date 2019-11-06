package com.creativeshare.registerme.activities_fragments.activities.home_activity.fragments.fragment_home.fragment_create_email;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creativeshare.registerme.R;
import com.creativeshare.registerme.activities_fragments.activities.home_activity.activity.Home_Activity;
import com.creativeshare.registerme.adapter.Job_Adapter;
import com.creativeshare.registerme.adapter.Mail_Adapter;
import com.creativeshare.registerme.models.Order_Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;


public class Fragment_Create_Email extends Fragment {
    private RecyclerView rec_job;
    private List<Order_Model.Data> dataList;
    private Mail_Adapter mail_adapter;
    private Home_Activity home_activity;
    private ImageView im_back;
    private String cuurent_language;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_email, container, false);
        initView(view);
        return view;

    }

    private void initView(View view) {
        home_activity=(Home_Activity)getActivity();
        dataList=new ArrayList<>();
        Paper.init(home_activity);
        cuurent_language = Paper.book().read("lang", Locale.getDefault().getLanguage());

        mail_adapter=new Mail_Adapter(dataList,home_activity,this);
        rec_job=view.findViewById(R.id.recView);
        im_back=view.findViewById(R.id.arrow);
        if(cuurent_language.equals("en")){
            im_back.setRotation(180.0f);
        }
        im_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home_activity.Back();
            }
        });
        rec_job.setLayoutManager(new LinearLayoutManager(home_activity,RecyclerView.HORIZONTAL,false));
        rec_job.setAdapter(mail_adapter);
        dataList.add(new Order_Model.Data());
        dataList.add(new Order_Model.Data());
        dataList.add(new Order_Model.Data());
        dataList.add(new Order_Model.Data());
        mail_adapter.notifyDataSetChanged();
    }

    public static Fragment_Create_Email newInstance() {
        return new Fragment_Create_Email();
    }


}
