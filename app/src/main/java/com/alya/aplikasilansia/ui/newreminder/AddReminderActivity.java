package com.alya.aplikasilansia.ui.newreminder;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
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
    private Button btnIconReminder, btnCreateReminder, btnCancel, btnBackFromAddReminder;
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

        btnCancel = findViewById(R.id.btn_cancel_reminder);
        btnCancel.setOnClickListener(v -> {
            Intent intent = new Intent(AddReminderActivity.this, ReminderActivity.class);
            startActivity(intent);
            finish();
        });
        btnBackFromAddReminder = findViewById(R.id.btn_back_addreminder);
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
                    finish();
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
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_back_addreminder) {
            Intent intent = new Intent(AddReminderActivity.this, ReminderActivity.class);
            startActivity(intent);
            finish();
        } else if (v.getId() == R.id.btn_create_reminder){
            createNewReminder();
//            finish();
        }
    }

    private void createNewReminder(){
        String title = inputTitleReminder.getText().toString().trim();
        String selectedDay = dayReminder.getSelectedItem().toString();
        String selectedTime = tvHourReminder.getText().toString().trim();
        String desc = inputDescReminder.getText().toString().trim();

        if (selectedIconResourceId != 0 && !title.isEmpty() && !selectedDay.isEmpty() && !selectedTime.isEmpty() && !desc.isEmpty()) {
            String timestamp = calculateTimestamp(selectedDay, selectedTime);
            addReminderViewModel.createReminder(title, selectedDay, selectedTime, desc, timestamp, selectedIconResourceId);
            Log.d("AddReminderActivity", "Attempting to schedule reminder"); // Add log here to confirm call
            ReminderScheduler.scheduleReminder(this, title, desc, timestamp);
            Log.d("AddReminderActivity", "scheduleReminder should have been called"); // Add log here to confirm call

        } else {
            incompleteFormDialog();
            Log.d("AddReminderActivity", "incomplete"); // Add log here to confirm call

        }

    }

    private void incompleteFormDialog() {
        // Inflate the custom layout for the dialog
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.incomplete_form_dialog, null);

        // Build the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        // Create the AlertDialog
        AlertDialog dialog = builder.create();

        // Set the custom background drawable with rounded corners before showing the dialog
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.custom_corner_rounded);

        // Adjust dialog size programmatically
        dialog.setOnShowListener(dialogInterface -> {
            WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
            params.width = (int) (getResources().getDisplayMetrics().widthPixels * 0.9); // 90% of screen width
            params.height = WindowManager.LayoutParams.WRAP_CONTENT; // Adjust height as needed
            dialog.getWindow().setAttributes(params);
        });

        // Show the dialog
        dialog.show();

        // Get the close button from the custom layout and set its click listener
        Button btnClose = dialogView.findViewById(R.id.btn_close_incomplete);
        btnClose.setOnClickListener(v -> dialog.dismiss());
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
        if (timeParts.length != 2) {
            // Handle invalid time format
            return "";
        }

        int hour = 0;
        int minute = 0;

        try {
            hour = Integer.parseInt(timeParts[0]);
            minute = Integer.parseInt(timeParts[1]);
        } catch (NumberFormatException e) {
            // Handle parsing error
            return "";
        }

        // Calculate the day difference
        int dayOfWeekNow = now.get(Calendar.DAY_OF_WEEK);
        int targetDayOfWeek = getDayOfWeek(selectedDay);
        if (targetDayOfWeek == -1) {
            // Handle invalid day
            return "";
        }
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

}