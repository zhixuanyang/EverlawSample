package com.example.yelpapp;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.*;
import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.yelpapp.databinding.FragmentFirstBinding;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.core.widget.NestedScrollView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SearchFragment extends Fragment {
    private final String IP_INFO_API = "1d33eadb5f5e85";
    private final String BACKEND_URL = "https://csci571-hw8-backend-367910.wl.r.appspot.com/searchbusiness";
    private final String auto_complete_url = "https://csci571-hw8-backend-367910.wl.r.appspot.com/autocomplete";
    private FragmentFirstBinding binding;
    private AutoCompleteTextView keywordEditText;
    private EditText distanceEditText;
    private Spinner categorySpinner;
    private EditText locationEditText;
    private CheckBox auto_location_box;
    private Boolean display_location;
    private detailRecAdapter adapter;
    private RecyclerView recyclerview;
    private NestedScrollView scrollView;
    private TextView no_result;
    public SearchFragment() {

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ArrayList<String> category_list = new ArrayList<>();
        categorySpinner = view.findViewById(R.id.category_spinner);
        keywordEditText = view.findViewById(R.id.search_keyword);
        distanceEditText = view.findViewById(R.id.search_distance);
        locationEditText = view.findViewById(R.id.search_location);
        auto_location_box = view.findViewById(R.id.search_auto_location);
        recyclerview = view.findViewById(R.id.search_scroll_view_recycler);
        adapter = new detailRecAdapter(getActivity());
        scrollView = view.findViewById(R.id.search_scroll_view);
        no_result = view.findViewById(R.id.no_search_result);
        no_result.setVisibility(View.INVISIBLE);
        scrollView.setVisibility(View.INVISIBLE);
        locationEditText.setVisibility(View.VISIBLE);
        display_location = true;
        category_list.add("Default");
        category_list.add("Arts & Entertainment");
        category_list.add("Health & Medical");
        category_list.add("Hotels & Travel");
        category_list.add("Food");
        category_list.add("Professional Services");
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_spinner_item,
                category_list
        );
        categorySpinner.setAdapter(categoryAdapter);
        categorySpinner.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    hideKeyboard(v);
                }
                return false;
            }
        });
        categorySpinner.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
                    hideKeyboard(v);
                    return true;
                } else {
                    return false;
                }
            }
        });
        binding.searchAutoLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard(view);
               if (display_location == true) {
                   display_location = false;
                   locationEditText.setVisibility(View.INVISIBLE);
               } else {
                   display_location = true;
                   locationEditText.setText("");
                   locationEditText.setVisibility(View.VISIBLE);
               }
            }
        });
        binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard(view);
                String keyword = keywordEditText.getText().toString().trim();
                if(TextUtils.isEmpty(keyword)) {
                    keywordEditText.setError("This field is required");
                    return;
                }
                String distance = distanceEditText.getText().toString().trim();
                if(TextUtils.isEmpty(distance)) {
                    distanceEditText.setError("This field is required");
                    return;
                }
                String location = locationEditText.getText().toString().trim();
                if(display_location && (TextUtils.isEmpty(location))) {
                    locationEditText.setError("This field is required");
                    return;
                }
                String category = category_value(categorySpinner.getSelectedItem().toString());
                keyword = keywordEditText.getText().toString();
                boolean auto_checked = auto_location_box.isChecked();
                if (auto_checked) {
                    String ip_info_url = "https://ipinfo.io/?token=" + IP_INFO_API;
                    String finalKeyword = keyword;
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                            (Request.Method.GET, ip_info_url, null, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        String final_url = BACKEND_URL + "?keyword=" +
                                                finalKeyword + "&distance=" + distance + "&category=" + category +
                                                "&location=" + response.getString("loc") + "&auto_checked=" + String.valueOf(auto_checked);
                                        JsonObjectRequest search_data = new JsonObjectRequest
                                                (Request.Method.GET, final_url, null, new Response.Listener<JSONObject>() {
                                                    @Override
                                                    public void onResponse(JSONObject search_response) {
                                                        JSONArray jsonArray = null;
                                                        try {
                                                            jsonArray = search_response.getJSONArray("businesses");
                                                            ArrayList<BusinessDetail> list = new ArrayList<>();
                                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                                                String business_id = jsonObject.getString("id");
                                                                String image_url = jsonObject.getString("image_url");
                                                                String business_rating = jsonObject.getString("rating");
                                                                double business_distance = jsonObject.getDouble("distance");
                                                                String business_name = jsonObject.getString("name");
                                                                String yelp_link = jsonObject.getString("url");
                                                                BusinessDetail business_item = new BusinessDetail(business_id, image_url, business_name, i + 1, business_rating, business_distance, yelp_link);
                                                                list.add(business_item);
                                                            }
                                                            if (list.size() == 0) {
                                                                no_result.setVisibility(View.VISIBLE);
                                                                scrollView.setVisibility(View.INVISIBLE);
                                                            } else {
                                                                scrollView.setVisibility(View.VISIBLE);
                                                                no_result.setVisibility(View.INVISIBLE);
                                                                adapter.updateBusiness(list);
                                                                recyclerview.setAdapter(adapter);
                                                                recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
                                                            }
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
                    queue.add(jsonObjectRequest);
                } else {
                    String finalKeyword = keyword;
                    String final_url = BACKEND_URL + "?keyword=" +
                            finalKeyword + "&distance=" + distance + "&category=" + category +
                            "&location=" + locationEditText.getText().toString() + "&auto_checked=" + String.valueOf(auto_checked);
                    JsonObjectRequest search_data = new JsonObjectRequest
                            (Request.Method.GET, final_url, null, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject search_response) {
                                    JSONArray jsonArray = null;
                                    try {
                                        jsonArray = search_response.getJSONArray("businesses");
                                        ArrayList<BusinessDetail> list = new ArrayList<>();
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                            String business_id = jsonObject.getString("id");
                                            String image_url = jsonObject.getString("image_url");
                                            String business_rating = jsonObject.getString("rating");
                                            double business_distance = jsonObject.getDouble("distance");
                                            String business_name = jsonObject.getString("name");
                                            String yelp_link = jsonObject.getString("url");
                                            BusinessDetail business_item = new BusinessDetail(business_id, image_url, business_name, i + 1, business_rating, business_distance, yelp_link);
                                            list.add(business_item);
                                        }
                                        if (list.size() == 0) {
                                            no_result.setVisibility(View.VISIBLE);
                                            scrollView.setVisibility(View.INVISIBLE);
                                        } else {
                                            scrollView.setVisibility(View.VISIBLE);
                                            no_result.setVisibility(View.INVISIBLE);
                                            adapter.updateBusiness(list);
                                            recyclerview.setAdapter(adapter);
                                            recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
                                        }
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
            }
        });

        keywordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String words = charSequence.toString();
                String auto_complete_url_final = auto_complete_url + "?keyword=" + words;
                JsonObjectRequest auto_complete_data = new JsonObjectRequest
                        (Request.Method.GET, auto_complete_url_final, null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject search_response) {
                                JSONArray jsonArray = null;
                                try {
                                    ArrayList<String> auto_complete_words = new ArrayList<>();
                                    jsonArray = search_response.getJSONArray("terms");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                        String value = jsonObject.getString("text");
                                        auto_complete_words.add(value);
                                    }
                                    jsonArray = search_response.getJSONArray("categories");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                        String value = jsonObject.getString("title");
                                        auto_complete_words.add(value);
                                    }
                                    ArrayAdapter auto_complete_adapter = new ArrayAdapter<>
                                            (getContext(), android.R.layout.simple_dropdown_item_1line, auto_complete_words);
                                    keywordEditText.setAdapter(auto_complete_adapter);
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
                queue.add(auto_complete_data);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String words = charSequence.toString();
                String auto_complete_url_final = auto_complete_url + "?keyword=" + words;
                JsonObjectRequest auto_complete_data = new JsonObjectRequest
                        (Request.Method.GET, auto_complete_url_final, null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject search_response) {
                                JSONArray jsonArray = null;
                                try {
                                    ArrayList<String> auto_complete_words = new ArrayList<>();
                                    jsonArray = search_response.getJSONArray("terms");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                        String value = jsonObject.getString("text");
                                        auto_complete_words.add(value);
                                    }
                                    jsonArray = search_response.getJSONArray("categories");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                        String value = jsonObject.getString("title");
                                        auto_complete_words.add(value);
                                    }
                                    ArrayAdapter auto_complete_adapter = new ArrayAdapter<>
                                            (getContext(), android.R.layout.simple_dropdown_item_1line, auto_complete_words);
                                    keywordEditText.setAdapter(auto_complete_adapter);
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
                queue.add(auto_complete_data);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String words = editable.toString();
                String auto_complete_url_final = auto_complete_url + "?keyword=" + words;
                JsonObjectRequest auto_complete_data = new JsonObjectRequest
                        (Request.Method.GET, auto_complete_url_final, null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject search_response) {
                                JSONArray jsonArray = null;
                                try {
                                    ArrayList<String> auto_complete_words = new ArrayList<>();
                                    jsonArray = search_response.getJSONArray("terms");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                        String value = jsonObject.getString("text");
                                        auto_complete_words.add(value);
                                    }
                                    jsonArray = search_response.getJSONArray("categories");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                        String value = jsonObject.getString("title");
                                        auto_complete_words.add(value);
                                    }
                                    ArrayAdapter auto_complete_adapter = new ArrayAdapter<>
                                            (getContext(), android.R.layout.simple_dropdown_item_1line, auto_complete_words);
                                    keywordEditText.setAdapter(auto_complete_adapter);
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
                queue.add(auto_complete_data);
            }
        });

        keywordEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        distanceEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        locationEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        binding.buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                keywordEditText.setText("");
                distanceEditText.setText("");
                locationEditText.setText("");
                categorySpinner.setSelection(0);
                no_result.setVisibility(View.INVISIBLE);
                scrollView.setVisibility(View.INVISIBLE);
                hideKeyboard(view);
                display_location = true;
                locationEditText.setVisibility(View.VISIBLE);
                auto_location_box.setChecked(false);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private String category_value(String input) {
        if(input == "Default"){
            return "All";
        }
        else if(input == "Arts & Entertainment") {
            return "arts";
        }
        else if(input == "Health & Medical") {
            return "health";
        }
        else if(input == "Hotels & Travel") {
            return "hotelstravel";
        }
        else if(input == "Food") {
            return "food";
        }
        else {
            return "professional";
        }
    }
}