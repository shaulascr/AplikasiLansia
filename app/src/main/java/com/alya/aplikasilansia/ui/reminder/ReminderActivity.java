package com.alya.aplikasilansia.ui.reminder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.alya.aplikasilansia.R;
import com.alya.aplikasilansia.ui.newreminder.AddReminderActivity;

public class ReminderActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        Button btnFromReminder = findViewById(R.id.btn_back_reminder);
        btnFromReminder.setOnClickListener(this);

        Button btnCreateReminder = findViewById(R.id.btn_add_reminder);
        btnCreateReminder.setOnClickListener(this);
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
}