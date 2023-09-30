package com.example.yelpapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class resRecAdapter extends RecyclerView.Adapter<resViewHolder> {
    ArrayList<reservation_info> list;
    private final String pre_name = "Reservation_list";
    Context context;
    private AdapterListener listener;
    public resRecAdapter(Context context, AdapterListener listener) {
        this.listener = listener;
        this.context = context;
    }

    @Override
    public resViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.res_item, parent, false);
        resViewHolder viewHolder = new resViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull resViewHolder viewHolder, int position) {
        reservation_info item = list.get(position);
        viewHolder.res_index_view.setText(String.valueOf(position + 1));
        viewHolder.res_name_view.setText(item.getBusiness_name());
        viewHolder.res_date_view.setText(item.getRes_date());
        viewHolder.res_time_view.setText(item.getRes_time());
        viewHolder.res_email_view.setText(item.getRes_email());
    }

    public interface AdapterListener {
        public void list_empty();
    }
    @Override
    public int getItemCount()
    {
        return list.size();
    }

    public void removeItem(int pos) {
        SharedPreferences pref = context.getSharedPreferences(pre_name, 0);
        SharedPreferences.Editor editor = pref.edit();
        String id = list.get(pos).getId();
        if (pref.contains(id)) {
            editor.remove(id);
            editor.commit();
        }
        list.remove(pos);
        notifyItemRemoved(pos);
        notifyDataSetChanged();
        if (getItemCount() == 0) {
            listener.list_empty();
        }
    }
    public void updateReservation(ArrayList<reservation_info> list) {
        this.list = list;
        notifyDataSetChanged();
    }
    @Override
    public void onAttachedToRecyclerView (RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public ArrayList<reservation_info> getData() {
        return list;
    }
}
