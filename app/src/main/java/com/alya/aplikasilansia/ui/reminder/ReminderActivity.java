package com.alya.aplikasilansia.ui.reminder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alya.aplikasilansia.R;
import com.alya.aplikasilansia.ui.newreminder.AddReminderActivity;

import java.util.ArrayList;
import java.util.List;

public class ReminderActivity extends AppCompatActivity implements View.OnClickListener {

    Spinner filterSpinner;
    RecyclerView filteredReminderRV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        Button btnFromReminder = findViewById(R.id.btn_back_reminder);
        btnFromReminder.setOnClickListener(this);

        Button btnCreateReminder = findViewById(R.id.btn_add_reminder);
        btnCreateReminder.setOnClickListener(this);

        filterSpinner = findViewById(R.id.dropdown_reminder_filter);
        setupSpinner(filterSpinner);

        filteredReminderRecyclerView();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_back_reminder) {
            finish();
        } else if (v.getId() == R.id.btn_add_reminder) {
            Intent intent = new Intent(ReminderActivity.this, AddReminderActivity.class);
            startActivity(intent);
        }
    }

    private void setupSpinner(Spinner spinner) {
        String[] filterReminder = getResources().getStringArray(R.array.filter_reminder);

        // Create an ArrayAdapter using a simple spinner item layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, filterReminder);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        filterSpinner.setAdapter(adapter);

//        filterSpinner.setSelection(0, false);
        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Handle item selection
                if (position == 0) {
                    // First item ("Semua Pengingat") is selected, handle as needed (e.g., show message)
                    Toast.makeText(ReminderActivity.this, "Please select a valid option", Toast.LENGTH_SHORT).show();
                } else {
                    // Perform actions based on selected item (exclude the first item)
                    String selectedFilter = filterReminder[position];
                    Toast.makeText(ReminderActivity.this, "Selected: " + selectedFilter, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle case when nothing is selected
            }

        });
    }

    private void filteredReminderRecyclerView() {
        filteredReminderRV = findViewById(R.id.rv_filtered_reminder);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        filteredReminderRV.setLayoutManager(layoutManager);

        // Create a list of FilteredReminder objects (replace with your actual data)
        List<FilteredReminder> filteredReminderList = new ArrayList<>();
        filteredReminderList.add(new FilteredReminder("New Reminder 1", "2024-07-01", "In this updated code, the FilteredReminder constructor properly initializes the fields, and the bind method in the ReminderViewHolder sets the TextViews and ImageView with the correct data from the FilteredReminder object.", R.drawable.kiwi));
        filteredReminderList.add(new FilteredReminder("New Reminder 2", "2024-07-02", "Description 2", R.drawable.white_round_pill));
        FilteredReminderAdapter filteredReminderAdapter = new FilteredReminderAdapter(filteredReminderList);
        filteredReminderRV.setAdapter(filteredReminderAdapter);
    }

    private void filterData(String selectedFilter) {
        // Replace with your logic to filter reminder data based on selectedFilter
        // Example: update filteredReminderList and notify adapter
        List<FilteredReminder> filteredList = new ArrayList<>();
        // Filter your original list based on selectedFilter
        // Example: filteredList = filterOriginalList(selectedFilter);

//        filteredReminderAdapter.updateList(filteredList); // Update adapter with filtered data
    }
}