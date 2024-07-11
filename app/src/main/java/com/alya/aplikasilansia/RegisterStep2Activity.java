package com.alya.aplikasilansia;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.alya.aplikasilansia.data.inputMedHistory;
import com.alya.aplikasilansia.ui.editprofile.EditProfileViewModel;
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class RegisterStep2Activity extends AppCompatActivity {
    private static final int REQUEST_PICK_IMAGE = 1;
    private FrameLayout parentLayout;
    private Button buttonAddInput, buttonSave;
    private LinearLayout viewMedRecord;
    private RelativeLayout setProfileImg;
    private ImageView profileImage;
    private Uri selectedImageUri;
    private RegisterViewModel registerViewModel;
    private EditProfileViewModel editProfileViewModel;
    private List<inputMedHistory> inputDataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_step2);

//        parentLayout = findViewById(R.id.input_medhistory);
        buttonAddInput = findViewById(R.id.btn_add_medhistory);
        buttonSave = findViewById(R.id.btn_save_medhistory);

        viewMedRecord = findViewById(R.id.view_med_layout);

        setProfileImg = findViewById(R.id.uploadImgReg);
        setProfileImg.setOnClickListener(v-> openGallery());

        profileImage = findViewById(R.id.profile_image_reg);

        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
        editProfileViewModel = new ViewModelProvider(this).get(EditProfileViewModel.class);

        MedicalRecordFragment medicalRecordFragment = new MedicalRecordFragment();
        inputDataList = medicalRecordFragment.inputDataList;

        buttonAddInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogMedRecord();
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                saveInputData();
//                registerViewModel.registerHealth1(inputDataList);
                saveProfileChanges();
                Intent register3 = new Intent(RegisterStep2Activity.this, RegisterStep3Activity.class);
                startActivity(register3);
            }
        });


        editProfileViewModel.getUserLiveData().observe(this, user -> {
            if (user != null) {
                if (user.getProfileImageUrl() != null) {
                    Glide.with(RegisterStep2Activity.this)
                            .load(user.getProfileImageUrl())
                            .into(profileImage);
                } else {
                    // Handle no profile image case
                    profileImage.setImageResource(R.drawable.img);
                }
            }
        });

        editProfileViewModel.getUpdateResultLiveData().observe(this, updateResult -> {
//            Toast.makeText(RegisterStep2Activity.this, updateResult, Toast.LENGTH_SHORT).show();
            if (updateResult.equals("Profile updated successfully")) {
                finish();
            } else {
                Snackbar.make(findViewById(android.R.id.content), "Failed to update profile", Snackbar.LENGTH_LONG)
                        .setAction("Retry", v -> saveProfileChanges())
                        .show();
            }
        });

        editProfileViewModel.fetchUser();
    }
    private void saveProfileChanges() {
        editProfileViewModel.updateProfile(null, null, null, selectedImageUri);
        finish();
    }

    // Method to open gallery for selecting profile image
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            profileImage.setImageURI(selectedImageUri);
        }
    }

    private void viewMedicalRecord() {
        viewMedRecord.removeAllViews();
        // Assuming inputDataList is your list of inputMedHistory objects
        if (inputDataList != null && !inputDataList.isEmpty()) {
            for (inputMedHistory history : inputDataList) {
                View itemView = getLayoutInflater().inflate(R.layout.register_view_medhistory, null);

                TextView tvPenyakit = itemView.findViewById(R.id.tv_penyakit);
                TextView tvMedYears = itemView.findViewById(R.id.tv_med_years);
                TextView tvMedMonths = itemView.findViewById(R.id.tv_med_months);

                tvPenyakit.setText(history.getPenyakit());
                tvMedYears.setText(history.getLamanya());
                tvMedMonths.setText(history.getLamanyaBulan());

                viewMedRecord.addView(itemView);
            }
        } else {
            // Handle case where inputDataList is empty
            // For example, you might show a message or take another action
            Toast.makeText(this, "No medical history data available", Toast.LENGTH_SHORT).show();
        }
    }

    private void dialogMedRecord () {
        MedicalRecordFragment medicalRecordFragment = new MedicalRecordFragment();
        medicalRecordFragment.setOnDataSavedListener(new MedicalRecordFragment.OnDataSavedListener() {
            @Override
            public void onDataSaved(List<inputMedHistory> updatedList) {
                // Handle the data saved event here
                inputDataList.clear();
                inputDataList.addAll(updatedList);
                viewMedicalRecord(); // Update UI after data is saved
            }
        });
        medicalRecordFragment.show(getSupportFragmentManager(), "MedicalRecordDialog");
    }
}