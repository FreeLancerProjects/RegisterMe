package com.creativeshare.registerme.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.creativeshare.registerme.R;
import com.creativeshare.registerme.models.AllInFo_Model;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class Spinner_Qulificatin_Adapter extends BaseAdapter {
    private List<AllInFo_Model.Data.Quallifcation> quallifcations;
    private LayoutInflater inflater;
    private String current_language;

    public Spinner_Qulificatin_Adapter(Context context, List<AllInFo_Model.Data.Quallifcation> quallifcations) {
        this.quallifcations = quallifcations;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        current_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
    }

    @Override
    public int getCount() {
        return quallifcations.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.spinner_row, parent, false);
        }
        TextView tv_name = convertView.findViewById(R.id.tv_name);

        AllInFo_Model.Data.Quallifcation quallifcation = quallifcations.get(position);
if(current_language.equals("ar")){
    tv_name.setText(quallifcation.getAr_title());

}
else {
    tv_name.setText(quallifcation.getEn_title());
}
        return convertView;
    }
}
