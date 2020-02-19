package com.endpoint.registerme.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.endpoint.registerme.R;
import com.endpoint.registerme.activities_fragments.activities.home_activity.activity.Home_Activity;
import com.endpoint.registerme.models.NotificationDataModel;
import com.endpoint.registerme.share.TimeAgo;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class Notification_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int ITEM_DATA = 1;
    private final int ITEM_LOAD = 2;

    private List<NotificationDataModel.NotificationModel> data;
    private Context context;
    private Home_Activity activity;
    private Fragment fragment;
    private String current_lang;

    public Notification_Adapter(List<NotificationDataModel.NotificationModel> data, Context context, Fragment fragment) {

        this.data = data;
        this.context = context;
        activity = (Home_Activity) context;

        this.fragment = fragment;
        Paper.init(activity);
        current_lang = Paper.book().read("lang", Locale.getDefault().getLanguage());


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == ITEM_DATA) {
            View view = LayoutInflater.from(context).inflate(R.layout.notification_row, parent, false);
            return new MyHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.load_more_row, parent, false);
            return new LoadMoreHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MyHolder) {

            final MyHolder myHolder = (MyHolder) holder;
            final NotificationDataModel.NotificationModel data1 = data.get(position);
         if(current_lang.equals("ar")){
             myHolder.tv_title.setText(data1.getAr_notification_title());
             if(data1.getAr_notification_body()!=null){
                 myHolder.tv_body.setText(data1.getAr_notification_body());
             }
         }
         else {
             myHolder.tv_title.setText(data1.getEn_notification_title());
             if(data1.getEn_notification_body()!=null){
                 myHolder.tv_body.setText(data1.getEn_notification_body());
             }
         }
            Log.e("date",data1.getNotification_date()+"");
         myHolder.tv_date.setText(TimeAgo.getTimeAgo(data1.getNotification_date(),activity));




            //Log.e("msg",advertsing.getMain_image());
        } else {
            LoadMoreHolder loadMoreHolder = (LoadMoreHolder) holder;
            loadMoreHolder.progBar.setIndeterminate(true);
        }
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private TextView tv_title, tv_body,tv_date;


        public MyHolder(View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tvTitle);
            tv_body = itemView.findViewById(R.id.tvContent);
        tv_date=itemView.findViewById(R.id.tvDate);

        }

    }

    public class LoadMoreHolder extends RecyclerView.ViewHolder {
        private ProgressBar progBar;

        public LoadMoreHolder(View itemView) {
            super(itemView);
            progBar = itemView.findViewById(R.id.progBar);
            progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        }
    }

    @Override
    public int getItemViewType(int position) {
        NotificationDataModel.NotificationModel data1 = data.get(position);
        if (data1 == null) {
            return ITEM_LOAD;
        } else {
            return ITEM_DATA;

        }


    }
}
