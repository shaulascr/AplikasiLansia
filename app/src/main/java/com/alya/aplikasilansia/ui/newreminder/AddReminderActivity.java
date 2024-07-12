package com.alya.aplikasilansia.ui.newreminder;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.alya.aplikasilansia.R;
import com.alya.aplikasilansia.messaging.ReminderScheduler;
import com.alya.aplikasilansia.ui.reminder.CustomSpinnerAdapter;
import com.alya.aplikasilansia.ui.reminder.ReminderActivity;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddReminderActivity extends AppCompatActivity implements View.OnClickListener, IconReminderFragment.OnIconSelectedListener {

    private TextView tvHourReminder;
    private Button btnIconReminder;
    private Button btnCreateReminder;
    private ImageView imgIconReminder;
    private int selectedIconResourceId;

    private EditText inputTitleReminder;
    private EditText inputDescReminder;
    private Spinner dayReminder;
    private AddReminderViewModel addReminderViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);

        addReminderViewModel = new ViewModelProvider(this).get(AddReminderViewModel.class);

        btnIconReminder = findViewById(R.id.btn_edit_ic_reminder);
        dialogIconReminder(btnIconReminder);

        imgIconReminder = findViewById(R.id.img_edit_ic_reminder); // set the src of this to selected src in the previous fragment

        Button btnBackFromAddReminder = findViewById(R.id.btn_back_addreminder);
        btnBackFromAddReminder.setOnClickListener(this);

        btnCreateReminder = findViewById(R.id.btn_create_reminder);
        btnCreateReminder.setOnClickListener(this);

        tvHourReminder = findViewById(R.id.tv_hour_reminder);
        setTimePicker(tvHourReminder);

        inputTitleReminder = findViewById(R.id.input_judul_reminder);
        inputDescReminder = findViewById(R.id.input_desc_reminder);

        dayReminder = findViewById(R.id.spinner_day_reminder);
        CustomSpinnerAdapter dayReminderAdapter = new CustomSpinnerAdapter(
                this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.day_array)
        );
        dayReminderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dayReminder.setAdapter(dayReminderAdapter);

        addReminderViewModel.reminderLiveData.observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if (firebaseUser != null) {
                    Log.d(TAG, "createReminder:success");
                    Intent intent = new Intent(AddReminderActivity.this, ReminderActivity.class);
                    startActivity(intent);


                }
            }
        });

        addReminderViewModel.errorLiveData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String error) {
                if (error != null) {
                    Log.w(TAG, "createReminder:failure: " + error);
                    Toast.makeText(AddReminderActivity.this, "Data failed: " + error,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void createNewReminder(){
        String title = inputTitleReminder.getText().toString().trim();
        String selectedDay = dayReminder.getSelectedItem().toString();
        String selectedTime = tvHourReminder.getText().toString().trim();
        String desc = inputDescReminder.getText().toString().trim();
        String timestamp = calculateTimestamp(selectedDay, selectedTime);

        if (selectedIconResourceId != 0) {
            addReminderViewModel.createReminder(title, selectedDay, selectedTime, desc, timestamp, selectedIconResourceId);
            Context context = getApplication(); // Inside an Activity or Service
            Log.d("AddReminderActivity", "Attempting to schedule reminder"); // Add log here to confirm call
            ReminderScheduler.scheduleReminder(this, title, desc, timestamp);
            Log.d("AddReminderActivity", "scheduleReminder should have been called"); // Add log here to confirm call

        } else {
            // Handle case where no icon is selected
            Toast.makeText(this, "Please select an icon", Toast.LENGTH_SHORT).show();
            // Optionally, you can prevent reminder creation or provide default behavior
        }
//        addReminderViewModel.createReminder(title, selectedDay, selectedTime, desc);

    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_back_addreminder) {
            finish();
        } else if (v.getId() == R.id.btn_create_reminder){
            createNewReminder();
        }
    }
    public void dialogIconReminder (Button btnIconReminder) {
        btnIconReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IconReminderFragment iconReminderFragment = new IconReminderFragment();
                iconReminderFragment.show(getSupportFragmentManager(), "IconReminderDialog");
//                Toast.makeText(this, "Button Clicked!", Toast.LENGTH_SHORT).show();
                iconReminderFragment.setOnIconSelectedListener(AddReminderActivity.this); // Set listener
            }
        });
    }
    @Override
    public void onIconSelected(int iconResId) {
        // Update ImageView with selected icon
        imgIconReminder.setImageResource(iconResId);
        selectedIconResourceId = iconResId; // Save selected icon resource ID to use later
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

    private String calculateTimestamp(String selectedDay, String selectedTime) {
        Calendar now = Calendar.getInstance();

        // Parse selected time
        String[] timeParts = selectedTime.split(":");
        int hour = Integer.parseInt(timeParts[0]);
        int minute = Integer.parseInt(timeParts[1]);

        // Calculate the day difference
        int dayOfWeekNow = now.get(Calendar.DAY_OF_WEEK);
        int targetDayOfWeek = getDayOfWeek(selectedDay);
        int dayDifference = (targetDayOfWeek - dayOfWeekNow + 7) % 7;

        // Set the target date and time
        Calendar targetDate = (Calendar) now.clone();
        targetDate.add(Calendar.DAY_OF_MONTH, dayDifference);
        targetDate.set(Calendar.HOUR_OF_DAY, hour);
        targetDate.set(Calendar.MINUTE, minute);
        targetDate.set(Calendar.SECOND, 0);
        targetDate.set(Calendar.MILLISECOND, 0);

        // Return the timestamp
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(targetDate.getTime());
    }
    private int getDayOfWeek(String day) {
        switch (day) {
            case "Senin":
                return Calendar.MONDAY;
            case "Selasa":
                return Calendar.TUESDAY;
            case "Rabu":
                return Calendar.WEDNESDAY;
            case "Kamis":
                return Calendar.THURSDAY;
            case "Jumat":
                return Calendar.FRIDAY;
            case "Sabtu":
                return Calendar.SATURDAY;
            case "Minggu":
                return Calendar.SUNDAY;
            default:
                return -1; // Handle invalid input or edge cases
        }
    }

//    private int getDayOfWeek(String day) {
//        switch (day) {
//            case "Sunday":
//                return Calendar.SUNDAY;
//            case "Monday":
//                return Calendar.MONDAY;
//            case "Tuesday":
//                return Calendar.TUESDAY;
//            case "Wednesday":
//                return Calendar.WEDNESDAY;
//            case "Thursday":
//                return Calendar.THURSDAY;
//            case "Friday":
//                return Calendar.FRIDAY;
//            case "Saturday":
//                return Calendar.SATURDAY;
//            default:
//                return -1;
//        }
//    }
}