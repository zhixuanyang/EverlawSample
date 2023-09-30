package com.example.yelpapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.yelpapp.databinding.FragmentReviewBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ReviewFragment extends Fragment {
    private String id;
    private RecyclerView recyclerview;
    private reviewRecAdapter adapter;
    private FragmentReviewBinding binding;
    private final String BACKEND_URL = "https://csci571-hw8-backend-367910.wl.r.appspot.com/business_review";
    public ReviewFragment(String id) {
        this.id = id;
    }

    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentReviewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerview = view.findViewById(R.id.review_view_recycler);
        String final_url = BACKEND_URL + "?id=" + this.id;
        adapter = new reviewRecAdapter(getActivity());
        JsonObjectRequest search_data = new JsonObjectRequest
                (Request.Method.GET, final_url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject search_response) {
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = search_response.getJSONArray("reviews");
                            ArrayList<ReviewDetail> list = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                JSONObject user = jsonObject.getJSONObject("user");
                                String author = user.getString("name");
                                Object rating = jsonObject.get("rating");
                                String content = jsonObject.getString("text");
                                String time = jsonObject.getString("time_created");
                                String[] date = time.split(" ");
                                String rating_value = "Rating :" + String.valueOf(rating) + "/5\n";
                                String content_value = content + "\n";
                                String date_value = date[0];
                                ReviewDetail review_item = new ReviewDetail(author, rating_value, content_value, date_value);
                                list.add(review_item);
                            }
                            adapter.updateReviews(list);
                            recyclerview.setAdapter(adapter);
                            recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Log.d("test", String.valueOf(error));
                    }
                });
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        queue.add(search_data);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
