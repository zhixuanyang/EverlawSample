package com.example.yelpapp;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class tabFragmentAdapter extends FragmentStateAdapter {
    private String id;
    private String address;
    private String price;
    private String phone;
    private String status;
    private String name;
    private String category;
    private String yelp_url;
    private boolean hasCor;
    private ArrayList<String> image_list;
    private double lat;
    private double lng;


    public tabFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, String id, String address,
                              String price, String phone, String status, String category, String yelp_url, double lat,
                              double lng, ArrayList<String> image_list, boolean hasCor, String name) {
        super(fragmentManager, lifecycle);
        this.id = id;
        this.address = address;
        this.price = price;
        this.phone = phone;
        this.status = status;
        this.category = category;
        this.yelp_url = yelp_url;
        this.lat = lat;
        this.lng = lng;
        this.image_list = image_list;
        this.name = name;
        this.hasCor = hasCor;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return new MapFragment(this.lat, this.lng, this.hasCor);
            case 2:
                return new ReviewFragment(this.id);
            default:
                return new DetailFragment(this.id, this.name, this.address, this.price, this.phone, this.status, this.category, this.yelp_url, this.image_list);
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
