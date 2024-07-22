package com.alya.aplikasilansia;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.alya.aplikasilansia.ui.reminder.CustomSpinnerAdapter;

public class RegisterStep3Activity extends AppCompatActivity {

    private Spinner spinnerCareGiver;
    private Spinner spinnerMaritalStatus;
    private Button btnSaveMedData;
    private RegisterViewModel registerViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_step3);

        spinnerCareGiver = findViewById(R.id.spinner_care_giver);
        spinnerMaritalStatus = findViewById(R.id.spinner_marital);
        btnSaveMedData = findViewById(R.id.btn_save_med);

        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        btnSaveMedData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedCareGiver = spinnerCareGiver.getSelectedItem().toString();
                String selectedMaritalStatus = spinnerMaritalStatus.getSelectedItem().toString();

                if (selectedCareGiver.equals("--Pilih yang Merawat--") || selectedMaritalStatus.equals("--Pilih Status--")) {
                    incompleteFormDialog();
                } else {
                    registerViewModel.registerHealth2(selectedCareGiver, selectedMaritalStatus);
                    Intent intent = new Intent(RegisterStep3Activity.this, MainActivity.class);
                    startActivity(intent);
                }

            }
        });

        // Create an ArrayAdapter using the custom spinner adapter
        CustomSpinnerAdapter careGiverAdapter = new CustomSpinnerAdapter(
                this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.care_giver)
        );
        careGiverAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        CustomSpinnerAdapter maritalStatusAdapter = new CustomSpinnerAdapter(
                this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.marital_status)
        );
        maritalStatusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapters to the spinners
        spinnerCareGiver.setAdapter(careGiverAdapter);
        spinnerMaritalStatus.setAdapter(maritalStatusAdapter);
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

//        TextView textTv = dialogView.findViewById(R.id.tv_incomplete_form);
//        String text = "Mohon lengkapi data riwayat penyakit Anda";
//        textTv.setText(text);
        // Get the close button from the custom layout and set its click listener
        Button btnClose = dialogView.findViewById(R.id.btn_close_incomplete);
        btnClose.setOnClickListener(v -> dialog.dismiss());
    }
}