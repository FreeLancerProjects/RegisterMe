package com.creativeshare.registerme.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;


import com.creativeshare.registerme.R;
import com.creativeshare.registerme.activities_fragments.activities.home_activity.activity.Home_Activity;
import com.creativeshare.registerme.models.Order_Model;
import com.creativeshare.registerme.tags.Tags;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class Order_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int ITEM_DATA = 1;
    private final int ITEM_LOAD = 2;

    private List<Order_Model.Data> data;
    private Context context;
    private Home_Activity activity;
    private Fragment fragment;
    private String current_lang;

    public Order_Adapter(List<Order_Model.Data> data, Context context, Fragment fragment) {

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
            View view = LayoutInflater.from(context).inflate(R.layout.order_row, parent, false);
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
             Order_Model.Data data1 = data.get(position);
          myHolder.tv_order_num.setText(data1.getId()+"");
          if(data1.getType()==1){
              ((MyHolder) holder).tvtype.setText(activity.getResources().getString(R.string.cv));
          }
          else if(data1.getType()==2){
              ((MyHolder) holder).tvtype.setText(activity.getResources().getString(R.string.emailjob));

          }
          else if(data1.getType()==3){
              ((MyHolder) holder).tvtype.setText(activity.getResources().getString(R.string.job));

          }
            myHolder.imnew.setImageResource(R.drawable.un_checked_circle);

            myHolder.imdistrub.setImageResource(R.drawable.un_checked_circle);
            myHolder.imrecive.setImageResource(R.drawable.un_checked_circle);
            myHolder.imcomplete.setImageResource(R.drawable.un_checked_circle);

            String status="";
          if(data1.getStatus()==0){
              status=activity.getString(R.string.new_order);
              myHolder.imnew.setImageResource(R.drawable.ic_checked_circle);
          }
          else if(data1.getStatus()==1){
              status=activity.getString(R.string.your_request_is_being_distributed);
              myHolder.imnew.setImageResource(R.drawable.ic_checked_circle);

              myHolder.imdistrub.setImageResource(R.drawable.ic_checked_circle);


          }
          else if(data1.getStatus()==2){
              status=activity.getString(R.string.received);
              myHolder.imnew.setImageResource(R.drawable.ic_checked_circle);

              myHolder.imdistrub.setImageResource(R.drawable.ic_checked_circle);
              myHolder.imrecive.setImageResource(R.drawable.ic_checked_circle);


          }
          else if(data1.getStatus()==3){
              status=activity.getString(R.string.been_completed);
              myHolder.imnew.setImageResource(R.drawable.ic_checked_circle);

              myHolder.imdistrub.setImageResource(R.drawable.ic_checked_circle);
              myHolder.imrecive.setImageResource(R.drawable.ic_checked_circle);
              myHolder.imcomplete.setImageResource(R.drawable.ic_checked_circle);


          }
          myHolder.tv_satus.setText(status);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            String date = dateFormat.format(new Date(data1.getDate() * 1000));
            ((MyHolder) holder).tv_date.setText(date);



//            Log.e("msg",data1.getStatus()+" "+data1.getId());
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
        private TextView tv_order_num, tv_satus,tv_date,tvtype;
private ImageView imnew,imdistrub,imrecive,imcomplete;

        public MyHolder(View itemView) {
            super(itemView);
            tv_order_num = itemView.findViewById(R.id.tvOrderNumber);
            tv_satus = itemView.findViewById(R.id.tvstatus);
        tv_date=itemView.findViewById(R.id.tv_date);
        tvtype=itemView.findViewById(R.id.tvtype);
imnew=itemView.findViewById(R.id.image1);
            imdistrub=itemView.findViewById(R.id.image2);
            imrecive=itemView.findViewById(R.id.image3);
            imcomplete=itemView.findViewById(R.id.image4);

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
        Order_Model.Data data1 = data.get(position);
        if (data1 == null) {
            return ITEM_LOAD;
        } else {
            return ITEM_DATA;

        }


    }
}
