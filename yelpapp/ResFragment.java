package com.example.yelpapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.yelpapp.databinding.FragmentResBinding;
import com.google.gson.Gson;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Map;

public class ResFragment extends Fragment implements resRecAdapter.AdapterListener {
    private FragmentResBinding binding;
    private RecyclerView recyclerview;
    private TextView not_found;
    private final String pre_name = "Reservation_list";
    resRecAdapter adapter;
    public ResFragment() {

    }
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentResBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerview = view.findViewById(R.id.res_recycler);
        not_found = view.findViewById(R.id.bookings_not_found);
        not_found.setVisibility(View.INVISIBLE);
        SharedPreferences pref = getContext().getSharedPreferences(pre_name, 0);
        Map<String, ?> allEntries = pref.getAll();
        Gson gson = new Gson();
        ArrayList<reservation_info> list = new ArrayList<>();
        adapter = new resRecAdapter(getActivity(), this);
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String json = entry.getValue().toString();
            reservation_info item = gson.fromJson(json, reservation_info.class);
            list.add(item);
        }
        if (list.size() == 0) {
            not_found.setVisibility(View.VISIBLE);
            recyclerview.setVisibility(View.INVISIBLE);
        } else {
            adapter.updateReservation(list);
            recyclerview.setAdapter(adapter);
            enableSwipeToDelete();
            recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
    }

    private void enableSwipeToDelete() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(getContext()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                final int position = viewHolder.getBindingAdapterPosition();
                adapter.removeItem(position);
            }
        };
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerview);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void list_empty() {
        not_found.setVisibility(View.VISIBLE);
        recyclerview.setVisibility(View.INVISIBLE);
    }
}
