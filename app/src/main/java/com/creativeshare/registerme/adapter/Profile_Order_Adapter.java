package com.creativeshare.registerme.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.net.Uri;
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
import com.creativeshare.registerme.models.Profile_Order_Model;
import com.creativeshare.registerme.tags.Tags;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class Profile_Order_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int ITEM_DATA = 1;
    private final int ITEM_LOAD = 2;

    private List<Profile_Order_Model.Orders> data;
    private Context context;
    private Home_Activity activity;
    private Fragment fragment;
    private String current_lang;

    public Profile_Order_Adapter(List<Profile_Order_Model.Orders> data, Context context, Fragment fragment) {

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
            View view = LayoutInflater.from(context).inflate(R.layout.profile_order_row, parent, false);
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
            final Profile_Order_Model.Orders data1 = data.get(position);
         // myHolder.tv_order_num.setText(data1.getId()+"");
if(data1.getType().equals("1")){
    myHolder.im_order.setImageResource(R.drawable.ic_cv);
    if(data1.getCv()!=null){
    myHolder.tv_order_name.setText(data1.getCv());}
    else {
        myHolder.tv_order_name.setText(activity.getResources().getString(R.string.no_cv_found));
    }

}
else if(data1.getType().equals("3")){
    Picasso.with(context).load(Uri.parse(Tags.IMAGE_URL+data1.getCompany_logo())).placeholder(R.drawable.ic_team).fit().into(myHolder.im_order);
if(data1.getCompany_name()!=null){
    myHolder.tv_order_name.setText(data1.getCompany_name());
}
else {
    myHolder.tv_order_name.setText(data1.getJob_link());

}
}
else {
    Picasso.with(context).load(Uri.parse(Tags.IMAGE_URL+data1.getEmail_type_logo())).placeholder(R.drawable.ic_email3).fit().into(myHolder.im_order);
myHolder.tv_order_name.setText(data1.getEmail_name());
}
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            String date = dateFormat.format(new Date(data1.getDate() * 1000));
            ((MyHolder) holder).tv_date.setText(date);



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
        private TextView tv_order_name,tv_date;
private RoundedImageView im_order;
        public MyHolder(View itemView) {
            super(itemView);
            tv_order_name = itemView.findViewById(R.id.tvname);
        tv_date=itemView.findViewById(R.id.tv_date);
im_order=itemView.findViewById(R.id.im_order);


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
        Profile_Order_Model.Orders data1 = data.get(position);
        if (data1 == null) {
            return ITEM_LOAD;
        } else {
            return ITEM_DATA;

        }


    }
}
