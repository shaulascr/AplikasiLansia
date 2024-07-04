package com.alya.aplikasilansia;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

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

                registerViewModel.registerHealth2(selectedCareGiver, selectedMaritalStatus);
                Intent intent = new Intent(RegisterStep3Activity.this, MainActivity.class);
                startActivity(intent);
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

    private class CustomSpinnerAdapter extends ArrayAdapter<String> {

        public CustomSpinnerAdapter(@NonNull Context context, int resource, @NonNull String[] objects) {
            super(context, resource, objects);
        }

        @Override
        public boolean isEnabled(int position) {
            // Disable the first item from Spinner
            // First item will be used for hint
            return position != 0;
        }

        @NonNull
        @Override
        public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view = super.getDropDownView(position, convertView, parent);
            TextView textView = (TextView) view;
            if (position == 0) {
                // Set the hint text color to gray
                textView.setTextColor(getContext().getResources().getColor(android.R.color.darker_gray));
            } else {
                textView.setTextColor(getContext().getResources().getColor(android.R.color.black));
            }
            return view;
        }
    }
}