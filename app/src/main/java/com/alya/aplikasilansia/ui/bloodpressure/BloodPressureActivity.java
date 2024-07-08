package com.alya.aplikasilansia.ui.bloodpressure;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alya.aplikasilansia.LoginActivity;
import com.alya.aplikasilansia.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class BloodPressureActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private MaterialButton btnBackBP;
    private TextView tvDatePickerBP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_pressure);

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() == null) {
            // User is not logged in, redirect to LoginActivity
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish(); // Close RestrictedActivity
        } else {
            btnBackBP = findViewById(R.id.btn_back_bp);
            setBtnBackBP(btnBackBP);

            tvDatePickerBP = findViewById(R.id.tv_date_bp);
            setTvDatePickerBP(tvDatePickerBP);

            setUpBloodPressureRV();
        }
    }

    private void setBtnBackBP (MaterialButton btnBackBP) {
        btnBackBP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setTvDatePickerBP (TextView tvDatePickerBP) {
        tvDatePickerBP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerBP();
            }
        });
    }

    private void showDatePickerBP() {
        Calendar calendar = Calendar.getInstance();

        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
        constraintsBuilder.setOpenAt(calendar.getTimeInMillis());
        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Tanggal Tensi")
                .setCalendarConstraints(constraintsBuilder.build())
                .build();
        datePicker.addOnPositiveButtonClickListener(selection -> {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String formattedDate = format.format(selection);
            tvDatePickerBP.setText(formattedDate);
        });
        datePicker.show(getSupportFragmentManager(), "DATE_PICKER");
    }

    private void setUpBloodPressureRV(){
        RecyclerView recyclerView = findViewById(R.id.rv_bp_history);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<BloodPressure> bloodPressureList = new ArrayList<>();
        bloodPressureList.add(new BloodPressure("68/98 mmHg","98","2 Juli 2024"));
        bloodPressureList.add(new BloodPressure("68/98 mmHg","98","1 Juli 2024"));
        bloodPressureList.add(new BloodPressure("68/98 mmHg","98","2 Juni 2024"));

        BloodPressureAdapter adapter = new BloodPressureAdapter(bloodPressureList);
        recyclerView.setAdapter(adapter);
    }
}