package com.example.yelpapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class reviewRecAdapter extends RecyclerView.Adapter<reviewViewHolder> {

    ArrayList<ReviewDetail> list = new ArrayList<>();
    Context context;

    public reviewRecAdapter(Context context) {
        this.context = context;
    }

    @Override
    public reviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.review_item, parent, false);
        reviewViewHolder viewHolder = new reviewViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull reviewViewHolder viewHolder, int position) {
        ReviewDetail detail = list.get(position);
        viewHolder.author_view.setText(detail.getAuthor());
        viewHolder.rating_view.setText(detail.getRating());
        viewHolder.content_view.setText(detail.getContent());
        viewHolder.date_view.setText(detail.getDate());
        if (position == getItemCount() - 1) {
            viewHolder.underline.setVisibility(View.INVISIBLE);
        }
    }
    @Override
    public int getItemCount()
    {
        return list.size();
    }

    public void updateReviews(ArrayList<ReviewDetail> list) {
        this.list = list;
        notifyDataSetChanged();
    }
    @Override
    public void onAttachedToRecyclerView (RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}