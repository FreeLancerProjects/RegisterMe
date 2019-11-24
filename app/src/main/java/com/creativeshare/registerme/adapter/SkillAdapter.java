package com.creativeshare.registerme.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.creativeshare.registerme.R;
import com.creativeshare.registerme.activities_fragments.activities.home_activity.fragments.fragment_home.fragment_create_cv.Fragment_Create_Cv;
import com.creativeshare.registerme.activities_fragments.activities.home_activity.fragments.fragment_home.fragment_create_cv.Fragment_Edit_Cv;
import com.creativeshare.registerme.models.AllInFo_Model;
import com.creativeshare.registerme.models.BankDataModel;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;


public class SkillAdapter extends RecyclerView.Adapter<SkillAdapter.MyHolder> {

    private List<AllInFo_Model.Data.Skills> bankDataModelList;
    private Context context;
    private String current_language;
    private Fragment fragment;
    private Fragment_Edit_Cv fragment_edit_cv;
    private Fragment_Create_Cv fragment_create_cv;
    public SkillAdapter(List<AllInFo_Model.Data.Skills> bankDataModelList, Context context, Fragment fragment) {
        this.bankDataModelList = bankDataModelList;
        this.context = context;
        Paper.init(context);
        current_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
this.fragment=fragment;
    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.service_row, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int position) {

        AllInFo_Model.Data.Skills bankModel = bankDataModelList.get(position);
        holder.BindData(bankModel);
holder.image_delete.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if(fragment instanceof Fragment_Create_Cv){
            fragment_create_cv=(Fragment_Create_Cv)fragment;
            fragment_create_cv.deletitem(holder.getLayoutPosition());
        }
        else {
            fragment_edit_cv=(Fragment_Edit_Cv)fragment;
            fragment_edit_cv.deletitem(holder.getLayoutPosition());
        }
    }
});

    }

    @Override
    public int getItemCount() {
        return bankDataModelList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private TextView tv_title;
private ImageView image_delete;

        public MyHolder(View itemView) {
            super(itemView);
tv_title=itemView.findViewById(R.id.tv_title);
image_delete=itemView.findViewById(R.id.image_delete);



        }

        public void BindData(AllInFo_Model.Data.Skills bankModel) {
if(current_language.equals("ar")){
            tv_title.setText(bankModel.getAr_title());}
else {
    tv_title.setText(bankModel.getEn_title());
}
           // tv_account_iban.setText(bankModel.getIban());


        }
    }
}
