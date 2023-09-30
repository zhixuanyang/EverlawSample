package com.example.yelpapp;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.fragment.NavHostFragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.yelpapp.databinding.ActivityDetailBinding;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {
    private ActivityDetailBinding binding;
    private ViewPager2 viewPager;
    private tabFragmentAdapter adapter;
    private TabLayout tabs;
    private final String BACKEND_URL = "https://csci571-hw8-backend-367910.wl.r.appspot.com/searchdetail";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        String business_id = getIntent().getStringExtra("id");
        viewPager = findViewById(R.id.viewPager);
        viewPager.setUserInputEnabled(false);
        tabs = findViewById(R.id.tabLayout);
        FragmentManager fm = getSupportFragmentManager();
        String final_url = BACKEND_URL + "?id=" + business_id;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        JsonObjectRequest search_data = new JsonObjectRequest
                (Request.Method.GET, final_url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject search_response) {
                        try {
                            String name = "N/A";
                            if (search_response.has("name")) {
                                name = search_response.getString("name");
                            }
                            String phone = "N/A";
                            if (search_response.has("display_phone") && search_response.getString("display_phone").length() > 0) {
                                phone = search_response.getString("display_phone");
                            }
                            String price = "N/A";
                            if (search_response.has("price")) {
                                price = search_response.getString("price");
                            }
                            String url = search_response.getString("url");
                            String category = "N/A";
                            JSONArray category_array = search_response.getJSONArray("categories");
                            if (category_array.length() > 0) {
                                category = "";
                                for (int i = 0; i < category_array.length(); i++) {
                                    JSONObject item = category_array.getJSONObject(i);
                                    if (i == category_array.length() - 1) {
                                        category += item.getString("title");
                                    } else {
                                        category += item.getString("title") + " | ";
                                    }
                                }
                            }
                            boolean hasCor = false;
                            double lat = 0;
                            double lng = 0;
                            if (search_response.has("coordinates")) {
                                JSONObject coordinates = search_response.getJSONObject("coordinates");
                                hasCor = true;
                                lat = coordinates.getDouble("latitude");
                                lng = coordinates.getDouble("longitude");
                            }
                            ArrayList<String> photo_list = new ArrayList<>();
                            if (search_response.has("photos")) {
                                JSONArray photos = search_response.getJSONArray("photos");
                                for (int i = 0; i < photos.length(); i++) {
                                    photo_list.add((String) photos.get(i));
                                }
                            }
                            String address = "N/A";
                            if (search_response.has("location")) {
                                JSONObject location = search_response.getJSONObject("location");
                                if (location.has("display_address")) {
                                    JSONArray display_address = location.getJSONArray("display_address");
                                    address = "";
                                    for (int i = 0; i < display_address.length(); i++) {
                                        if (i == display_address.length() - 1) {
                                            address += (String) display_address.get(i);
                                        } else {
                                            address += (String) display_address.get(i) + ", ";
                                        }
                                    }
                                }
                            }
                            getSupportActionBar().setTitle(name);
                            JSONArray hours_array = search_response.getJSONArray("hours");
                            JSONObject hours = (JSONObject) hours_array.get(0);
                            String is_open = String.valueOf(hours.getBoolean("is_open_now"));
                            adapter = new tabFragmentAdapter(fm, getLifecycle(), business_id, address, price, phone, is_open, category, url, lat, lng, photo_list, hasCor, name);
                            viewPager.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("test", String.valueOf(error));
                    }
                });
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(search_data);
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabs.selectTab(tabs.getTabAt(position));
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        String yelp_link = getIntent().getStringExtra("yelp_link");
        String bus_name = getIntent().getStringExtra("bus_name");
        if (id == android.R.id.home) {
            this.finish();
            return true;
        }
        if (id == R.id.action_facebook) {
            Intent open_url = new Intent(Intent.ACTION_VIEW);
            String facebook_url = "https://www.facebook.com/sharer/sharer.php?u=" + yelp_link;
            open_url.setData(Uri.parse(facebook_url));
            startActivity(open_url);
            return true;
        }
        if (id == R.id.action_twitter) {
            Intent open_url = new Intent(Intent.ACTION_VIEW);
            String twitter_url = "http://twitter.com/share?text=Check " + bus_name + " on Yelp.&url=" + yelp_link;
            open_url.setData(Uri.parse(twitter_url));
            startActivity(open_url);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}