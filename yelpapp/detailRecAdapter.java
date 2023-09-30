package com.example.yelpapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class detailRecAdapter extends RecyclerView.Adapter<detailViewHolder> {

    ArrayList<BusinessDetail> list = new ArrayList<>();
    Context context;

    public detailRecAdapter(Context context) {
        this.context = context;
    }

    @Override
    public detailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.result_item, parent, false);
        detailViewHolder viewHolder = new detailViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull detailViewHolder viewHolder, int position) {
        BusinessDetail detail = list.get(position);
        viewHolder.index_view.setText(String.valueOf(detail.getIndex()));
        viewHolder.rating_view.setText(detail.getRating());
        viewHolder.name_view.setText(detail.getName());
        if (detail.getImage_url().length() != 0) {
            Picasso.with(this.context).load(detail.getImage_url()).into(viewHolder.image_view);
        }
        DecimalFormat formatter = new DecimalFormat("#0.00");
        viewHolder.distance_view.setText(formatter.format(detail.getDistance()));
        if (position == getItemCount() - 1) {
            viewHolder.underline_view.setVisibility(View.INVISIBLE);
        }
        viewHolder.parent_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,DetailActivity.class);
                intent.putExtra("id", detail.getId());
                intent.putExtra("bus_name", detail.getName());
                intent.putExtra("yelp_link", detail.getYelp_link());
                context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount()
    {
        return list.size();
    }
    public void updateBusiness(ArrayList<BusinessDetail> list) {
        this.list = list;
        notifyDataSetChanged();
    }
    @Override
    public void onAttachedToRecyclerView (RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}