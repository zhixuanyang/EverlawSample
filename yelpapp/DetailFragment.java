package com.example.yelpapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.app.Dialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.viewpager.widget.ViewPager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.yelpapp.databinding.FragmentDetailsBinding;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DetailFragment extends Fragment {

    private String id;
    private String name;
    private String address;
    private String price;
    private String phone;
    private String status;
    private String category;
    private String yelp_url;
    private ArrayList<String> image_list;
    private Button reservation;
    private FragmentDetailsBinding binding;
    private TextView address_view;
    private TextView price_view;
    private TextView phone_view;
    private TextView status_view;
    private TextView category_view;
    private TextView url_view;
    private ViewPager slider;
    private sliderAdapter slide_adapter;
    private final String pre_name = "Reservation_list";
    public DetailFragment(String id, String name, String address, String price, String phone, String status,
                          String category, String yelp_url, ArrayList<String> image_list) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.price = price;
        this.phone = phone;
        this.status = status;
        this.category = category;
        this.yelp_url = yelp_url;
        this.image_list = image_list;
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
        binding = FragmentDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        address_view = view.findViewById(R.id.address_var);
        price_view = view.findViewById(R.id.price_var);
        category_view = view.findViewById(R.id.category_var);
        phone_view = view.findViewById(R.id.phone_var);
        status_view = view.findViewById(R.id.status_var);
        url_view = view.findViewById(R.id.link_var);
        slider = view.findViewById(R.id.slider);
        reservation = view.findViewById(R.id.button_reserve);
        address_view.setText(this.address);
        price_view.setText(this.price);
        phone_view.setText(this.phone);
        category_view.setText(this.category);
        if (status == "true") {
            status_view.setText(R.string.open);
        } else if (status == "false") {
            status_view.setText(R.string.closed);
        } else {
            status_view.setText("N/A");
        }
        url_view.setText(R.string.link);
        url_view.setPaintFlags(url_view.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        url_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent open_url = new Intent(Intent.ACTION_VIEW);
                open_url.setData(Uri.parse(yelp_url));
                startActivity(open_url);
            }
        });
        reservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.dialog);
                TextView title = dialog.findViewById(R.id.dialog_title);
                title.setText(name);
                Button cancel = dialog.findViewById(R.id.dialog_cancel);
                EditText email = dialog.findViewById(R.id.dialog_email);
                EditText date = dialog.findViewById(R.id.dialog_date);
                date.setInputType(InputType.TYPE_NULL);
                date.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        switch (motionEvent.getAction()){
                            case MotionEvent.ACTION_DOWN:
                                Calendar calendar = Calendar.getInstance();
                                int day = calendar.get(Calendar.DAY_OF_MONTH);
                                int month = calendar.get(Calendar.MONTH);
                                int year = calendar.get(Calendar.YEAR);
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                    DatePickerDialog picker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                                        @Override
                                        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                            date.setText(new StringBuilder().append(i1 + 1).append("-").append(i2).append("-").append(i).toString());
                                        }
                                    }, year, month, day);
                                    picker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                                    picker.show();
                                }

                                break;
                        }
                        return false;
                    }
                });
                EditText time = dialog.findViewById(R.id.dialog_time);
                time.setInputType(InputType.TYPE_NULL);
                final int[] hour_value = {0};
                final int[] minute_value = {0};
                time.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        switch (motionEvent.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                Calendar calendar = Calendar.getInstance();
                                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                                int minute = calendar.get(Calendar.MINUTE);
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                    TimePickerDialog picker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                                        @Override
                                        public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                            String minute_temp = String.valueOf(i1);
                                            if (i1 < 10) {
                                                minute_temp = "0" + minute_temp;
                                            }
                                            time.setText(new StringBuilder().append(i).append(":").append(minute_temp).toString());
                                            hour_value[0] = i;
                                            minute_value[0] = i1;
                                        }
                                    }, hour, minute, false);
                                    picker.show();
                                }
                                break;
                        }
                        return false;
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                Button submit = dialog.findViewById(R.id.dialog_submit);
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String email_value = email.getText().toString().trim();
                        if (!email_validation(email_value)) {
                            LayoutInflater toast_layout = LayoutInflater.from(getContext());
                            View toast_view = toast_layout.inflate(R.layout.toast_email, null);
                            Toast toast = new Toast(getContext());
                            toast.setView(toast_view);
                            toast.setDuration(Toast.LENGTH_LONG);
                            toast.show();
                            dialog.dismiss();
                            return;
                        }
                        int hour = hour_value[0];
                        int minute = minute_value[0];
                        if (!time_validation(hour, minute)) {
                            LayoutInflater toast_layout = LayoutInflater.from(getContext());
                            View toast_view = toast_layout.inflate(R.layout.toast_time, null);
                            Toast toast = new Toast(getContext());
                            toast.setView(toast_view);
                            toast.setDuration(Toast.LENGTH_LONG);
                            toast.show();
                            dialog.dismiss();
                            return;
                        }
                        SharedPreferences pref = getContext().getSharedPreferences(pre_name, 0);
                        SharedPreferences.Editor editor = pref.edit();
                        reservation_info res_item = new reservation_info(id, name, email.getText().toString(), date.getText().toString(), time.getText().toString());
                        Gson gson = new Gson();
                        String reservation_json = gson.toJson(res_item);
                        editor.putString(id, reservation_json);
                        editor.commit();
                        LayoutInflater toast_layout = LayoutInflater.from(getContext());
                        View toast_view = toast_layout.inflate(R.layout.toast_success, null);
                        Toast toast = new Toast(getContext());
                        toast.setView(toast_view);
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.show();
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        slide_adapter = new sliderAdapter(getContext(), this.image_list);
        slider.setAdapter(slide_adapter);
    }

    private boolean email_validation(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean time_validation(int hour, int minute) {
        if (hour > 17) {
            return false;
        }
        if (hour < 10) {
            return false;
        }
        if (hour == 17 && minute != 0) {
            return false;
        }
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}