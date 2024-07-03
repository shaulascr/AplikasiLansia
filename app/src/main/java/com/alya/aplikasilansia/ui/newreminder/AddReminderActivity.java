package com.alya.aplikasilansia.ui.newreminder;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.alya.aplikasilansia.R;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

public class AddReminderActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvHourReminder;
    private TextView tvIconReminder;
    private ImageView imgIconReminder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);

        tvIconReminder = findViewById(R.id.tv_edit_ic_reminder);
        dialogIconReminder(tvIconReminder);
        imgIconReminder = findViewById(R.id.img_edit_ic_reminder);
        Button btnBackFromAddReminder = findViewById(R.id.btn_back_addreminder);
        btnBackFromAddReminder.setOnClickListener(this);

        tvHourReminder = findViewById(R.id.tv_hour_reminder);
        setTimePicker(tvHourReminder);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_back_addreminder) {
            finish();
        }
    }
    public void dialogIconReminder (TextView tvIconReminder) {
        tvIconReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IconReminderFragment iconReminderFragment = new IconReminderFragment();
                iconReminderFragment.show(getSupportFragmentManager(), "IconReminderDialog");
//                Toast.makeText(this, "Button Clicked!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setTimePicker(TextView tvHourReminder) {
        tvHourReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });
    }
    private void showTimePicker() {
        MaterialTimePicker materialTimePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .build();

        materialTimePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = materialTimePicker.getHour();
                int minute = materialTimePicker.getMinute();
                String selectedTime = String.format("%02d:%02d", hour, minute);
                tvHourReminder.setText(selectedTime);
            }
        });

        materialTimePicker.show(getSupportFragmentManager(), "timePicker");
    }
}