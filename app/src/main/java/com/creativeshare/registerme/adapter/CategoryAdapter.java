package com.creativeshare.registerme.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.creativeshare.registerme.R;
import com.creativeshare.registerme.activities_fragments.activities.home_activity.fragments.fragment_home.Fragment_Main;
import com.creativeshare.registerme.models.CategoryModel;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyHolder> {
    private List<CategoryModel> categoryModelList;
    private Context context;
    private Fragment_Main fragment_main;
    public CategoryAdapter(List<CategoryModel> categoryModelList, Context context, Fragment_Main fragment_main) {
        this.categoryModelList = categoryModelList;
        this.context = context;
        this.fragment_main = fragment_main;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_circle_row, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        CategoryModel categoryModel=categoryModelList.get(position);
tv_title.setText(categoryModel.getTitle());
image.setImageDrawable(context.getResources().getDrawable(categoryModel.getImg()));
    }

    @Override
    public int getItemCount() {
        return categoryModelList.size();
    }
private TextView tv_title;
    private ImageView image;
    public class MyHolder extends RecyclerView.ViewHolder {

        public MyHolder(View itemView) {
            super(itemView);
            tv_title=itemView.findViewById(R.id.tvTitle);
            image=itemView.findViewById(R.id.image);
        }
    }
}
