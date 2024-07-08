package com.alya.aplikasilansia.ui.healthcare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alya.aplikasilansia.LoginActivity;
import com.alya.aplikasilansia.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class HealthCareActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_care);

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() == null) {
            // User is not logged in, redirect to LoginActivity
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish(); // Close RestrictedActivity
        } else {
            Button backFromHealtnBtn = findViewById(R.id.btn_back_healthcare);
            backFromHealtnBtn.setOnClickListener(this);

            spinnerHealthBar();
            setupHealthRecyclerView();
        }
    }

    private void setupHealthRecyclerView() {
        // Initialize RecyclerView
        RecyclerView recyclerView = findViewById(R.id.rv_health_care);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Create list of HealthcareService objects
        List<HealthCareService> healthcareData = new ArrayList<>();
        healthcareData.add(new HealthCareService("Nama Pelayanan Kesehatan", "Jalan RS. Fatmawati Raya, Pondok Labu, Cilandak, Kota Jakarta Selatan, Jakarta"));
        healthcareData.add(new HealthCareService("Nama Pelayanan Kesehatan 2", "Jalan RS. Fatmawati Raya, Pondok Labu, Cilandak, Kota Jakarta Selatan, Jakarta"));
        healthcareData.add(new HealthCareService("Nama Pelayanan Kesehatan 2", "Jalan RS. Fatmawati Raya, Pondok Labu, Cilandak, Kota Jakarta Selatan, Jakarta"));
        healthcareData.add(new HealthCareService("Nama Pelayanan Kesehatan 2", "Jalan RS. Fatmawati Raya, Pondok Labu, Cilandak, Kota Jakarta Selatan, Jakarta"));
        // Add more data as needed

        // Set up adapter for RecyclerView
        HealthCareAdapter adapter = new HealthCareAdapter(healthcareData);
        recyclerView.setAdapter(adapter);
    }
    public void spinnerHealthBar(){
        Spinner spinner = findViewById(R.id.healthcare_searchbar);
        String[] data = {"Option 1", "Option 2", "Option 3"}; // Sample data
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.bringToFront();

    }

    public void onClick(View v){
        if (v.getId() == R.id.btn_back_healthcare) {
            finish();
        }
    }
}