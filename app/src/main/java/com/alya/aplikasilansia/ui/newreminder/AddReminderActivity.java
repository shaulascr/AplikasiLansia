package com.alya.aplikasilansia.ui.newreminder;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.alya.aplikasilansia.R;

public class AddReminderActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);

        Button btnBackFromAddReminder = findViewById(R.id.btn_back_addreminder);
        btnBackFromAddReminder.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_back_addreminder) {
            finish();
        }
    }
}