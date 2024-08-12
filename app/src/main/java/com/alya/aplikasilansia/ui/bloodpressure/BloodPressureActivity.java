package com.alya.aplikasilansia.ui.bloodpressure;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alya.aplikasilansia.LoginActivity;
import com.alya.aplikasilansia.R;
import com.alya.aplikasilansia.data.BloodPressure;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class BloodPressureActivity extends AppCompatActivity {
    private BloodPresViewModel bloodPresViewModel;
    private FirebaseAuth mAuth;
    private MaterialButton btnBackBP, btnSaveBP;
    private TextView tvDatePickerBP;
    private EditText etBP, etPulse;
    private BloodPressureAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_pressure);

        mAuth = FirebaseAuth.getInstance();
        bloodPresViewModel = new ViewModelProvider(this).get(BloodPresViewModel.class);

        if (mAuth.getCurrentUser() == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            btnBackBP = findViewById(R.id.btn_back_bp);
            setBtnBackBP(btnBackBP);

            tvDatePickerBP = findViewById(R.id.tv_date_bp);
            setTvDatePickerBP(tvDatePickerBP);

            btnSaveBP = findViewById(R.id.btn_save_bp);
            etBP = findViewById(R.id.etBloodPressure);
            etPulse = findViewById(R.id.etPulse);

            setUpBloodPressureRV();

            bloodPresViewModel.fetchBloodPressureData();

            bloodPresViewModel.getBloodPressureData().observe(this, new Observer<List<BloodPressure>>() {
                @Override
                public void onChanged(List<BloodPressure> bloodPressures) {
                    adapter.setBloodPressureList(bloodPressures);
                }

            });

            bloodPresViewModel.getPressureLiveData().observe(this, firebaseUser -> {
                Toast.makeText(BloodPressureActivity.this, "Blood pressure added successfully", Toast.LENGTH_SHORT).show();
            });

            bloodPresViewModel.getErrorLiveData().observe(this, error -> {
                Toast.makeText(BloodPressureActivity.this, "error", Toast.LENGTH_SHORT).show();
            });

            btnSaveBP.setOnClickListener(v -> {
                String bloodPressure = etBP.getText().toString().trim();
                String pulse = etPulse.getText().toString().trim();
                String timestamp = tvDatePickerBP.getText().toString().trim();

                if (!bloodPressure.isEmpty() && !pulse.isEmpty() && !timestamp.isEmpty()) {
                    try {
                        int pulseInt = Integer.parseInt(pulse);
                        bloodPresViewModel.addBloodPressure(bloodPressure, String.valueOf(pulseInt), timestamp);

                    } catch (NumberFormatException e) {
                        incompleteFormDialog();
                    }

                } else {
                    incompleteFormDialog();
                }
            });
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
        constraintsBuilder.setEnd(calendar.getTimeInMillis());

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

    private void setUpBloodPressureRV() {
        RecyclerView recyclerView = findViewById(R.id.rv_bp_history);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<BloodPressure> bloodPressureList = new ArrayList<>();
        adapter = new BloodPressureAdapter(bloodPressureList);
        recyclerView.setAdapter(adapter);
    }

    private void incompleteFormDialog(){
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.incomplete_form_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        dialog.show();

        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(R.drawable.custom_corner_rounded);

        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = (int) (getResources().getDisplayMetrics().widthPixels * 0.9);
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(params);

        Button btnClose = dialogView.findViewById(R.id.btn_close_incomplete);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        bloodPresViewModel.fetchBloodPressureData();
    }
}