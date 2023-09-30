package com.example.yelpapp;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class reviewViewHolder extends RecyclerView.ViewHolder {
    TextView author_view;
    TextView rating_view;
    TextView content_view;
    TextView date_view;
    LinearLayout review_parent;
    View underline;
    View view;
    public reviewViewHolder(@NonNull View itemView) {
        super(itemView);
        author_view = itemView.findViewById(R.id.reviewer_name);
        rating_view = itemView.findViewById(R.id.reviewer_rating);
        content_view = itemView.findViewById(R.id.reviewer_content);
        date_view = itemView.findViewById(R.id.reviewer_date);
        review_parent = itemView.findViewById(R.id.review_parent);
        underline = itemView.findViewById(R.id.review_underline);
        view = itemView;
    }
}
