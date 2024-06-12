package com.alya.aplikasilansia.ui.editprofile;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.alya.aplikasilansia.R;

public class EditProfileActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Set click listener for the Cancel button
        findViewById(R.id.btn_cancelProfile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the EditProfileActivity and return to the previous activity
                finish();
            }
        });
    }
}