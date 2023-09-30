package com.example.yelpapp;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class resViewHolder extends RecyclerView.ViewHolder {
    View view;
    TextView res_index_view;
    TextView res_name_view;
    TextView res_date_view;
    TextView res_time_view;
    TextView res_email_view;
    public resViewHolder(@NonNull View itemView) {
        super(itemView);
        res_index_view = itemView.findViewById(R.id.res_item_index);
        res_name_view = itemView.findViewById(R.id.res_item_businessName);
        res_date_view = itemView.findViewById(R.id.res_item_date);
        res_time_view = itemView.findViewById(R.id.res_item_time);
        res_email_view = itemView.findViewById(R.id.res_item_email);
        view = itemView;
    }
}
