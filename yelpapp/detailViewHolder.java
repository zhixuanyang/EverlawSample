package com.example.yelpapp;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class detailViewHolder extends RecyclerView.ViewHolder {
    TextView index_view;
    ImageView image_view;
    TextView name_view;
    TextView rating_view;
    TextView distance_view;
    RelativeLayout parent_view;
    View underline_view;
    View view;
    public detailViewHolder(@NonNull View itemView) {
        super(itemView);
        index_view = itemView.findViewById(R.id.search_results_item_index);
        image_view = itemView.findViewById(R.id.search_results_item_image);
        name_view = itemView.findViewById(R.id.search_results_item_businessName);
        rating_view = itemView.findViewById(R.id.search_results_item_rating);
        distance_view = itemView.findViewById(R.id.search_results_item_distance);
        parent_view = itemView.findViewById(R.id.search_results_item_parent);
        underline_view = itemView.findViewById(R.id.search_results_item_underline);
        view = itemView;
    }
}
