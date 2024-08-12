package com.alya.aplikasilansia;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.alya.aplikasilansia.data.UserData;
import com.alya.aplikasilansia.data.inputMedHistory;
import com.alya.aplikasilansia.ui.editprofile.EditProfileViewModel;

import java.util.ArrayList;
import java.util.List;

public class RegisterStep2Activity extends AppCompatActivity {
    private static final int REQUEST_PICK_IMAGE = 1;
    private FrameLayout parentLayout;
    private Button buttonAddInput, buttonSave, buttonNext;
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
        buttonNext = findViewById(R.id.btn_next_medhistory);
        viewMedRecord = findViewById(R.id.view_med_layout);

//        setProfileImg = findViewById(R.id.uploadImgReg);
//        setProfileImg.setOnClickListener(v-> openGallery());
//
//        profileImage = findViewById(R.id.profile_image_reg);

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

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register3 = new Intent(RegisterStep2Activity.this, RegisterStep3Activity.class);
                startActivity(register3);
                finish();
            }
        });
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                saveInputData();
//                registerViewModel.registerHealth1(inputDataList);
//                saveProfileChanges();
                Log.d("RegisterStep2Activity", "data saved");
                if (inputDataList != null && !inputDataList.isEmpty()) {
//                    Intent register3 = new Intent(RegisterStep2Activity.this, RegisterStep3Activity.class);
//                    register3.putExtra("profileImageUrl", selectedImageUri.toString()); // Convert Uri to String
//                    register3.putExtra("medHistory", new ArrayList<>(inputDataList)); // Convert list to ArrayList
//                    startActivity(register3);
                    UserData userData = UserData.getInstance();

                    userData.setMedHistory(inputDataList); // Save medHistory to UserData
                    // Convert profileImageUri to String and save it
                    userData.setProfileImageUrl(selectedImageUri);
                    Log.d("RegisterStep2Activity", "profile image to send: " +selectedImageUri);

                    Intent register3 = new Intent(RegisterStep2Activity.this, RegisterStep3Activity.class);
                    startActivity(register3);
                    finish();

                } else {
//                    incompleteFormDialog();
                    IncompleteFormDialog dialog = new IncompleteFormDialog();
                    dialog.show(getSupportFragmentManager(), "IncompleteFormDialog");
                }
            }
        });


//        editProfileViewModel.getUserLiveData().observe(this, user -> {
//            if (user != null) {
//                if (user.getProfileImageUrl() != null) {
//                    Glide.with(RegisterStep2Activity.this)
//                            .load(user.getProfileImageUrl())
//                            .into(profileImage);
//                } else {
//                    // Handle no profile image case
//                    profileImage.setImageResource(R.drawable.img);
//                }
//            }
//        });
//
//        editProfileViewModel.getUpdateResultLiveData().observe(this, updateResult -> {
////            Toast.makeText(RegisterStep2Activity.this, updateResult, Toast.LENGTH_SHORT).show();
//            if (updateResult.equals("Profile updated successfully")) {
////                finish();
//            } else {
//                Snackbar.make(findViewById(android.R.id.content), "Failed to update profile", Snackbar.LENGTH_LONG)
//                        .setAction("Retry", v -> saveProfileChanges())
//                        .show();
//            }
//        });

        editProfileViewModel.fetchUser();
    }
    private void saveProfileChanges() {
        editProfileViewModel.updateProfile(null, null, null, selectedImageUri);
//        finish();
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
            Log.d("RegisterStep2Activity", "profile image: " +selectedImageUri);

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
            Toast.makeText(this, "No medical history data available", Toast.LENGTH_SHORT).show();
        }
    }

    private void dialogMedRecord () {
        MedicalRecordFragment medicalRecordFragment = new MedicalRecordFragment();
        medicalRecordFragment.setOnDataSavedListener(new MedicalRecordFragment.OnDataSavedListener() {
            @Override
            public void onDataSaved(List<inputMedHistory> updatedList) {
                // Handle the data saved event here
//                inputDataList.clear();
                inputDataList.addAll(updatedList);
                Log.d("RegisterStep2Activity", "Updated inputDataList: " + inputDataList.toString());

                viewMedicalRecord(); // Update UI after data is saved
//                updateMedRecordView();
            }
        });
        medicalRecordFragment.show(getSupportFragmentManager(), "MedicalRecordDialog");
    }

    private void updateMedRecordView() {
        viewMedRecord.removeAllViews();
        for (inputMedHistory record : inputDataList) {
            viewMedRecord.addView(createRecordView(record));
        }
    }

    private View createRecordView(inputMedHistory history) {
        // Inflate and configure a view to display the record
        // Return the view
        LayoutInflater inflater = LayoutInflater.from(this);
        View itemView = inflater.inflate(R.layout.register_view_medhistory, null);

        TextView tvPenyakit = itemView.findViewById(R.id.tv_penyakit);
        TextView tvMedYears = itemView.findViewById(R.id.tv_med_years);
        TextView tvMedMonths = itemView.findViewById(R.id.tv_med_months);

        tvPenyakit.setText(history.getPenyakit());
        tvMedYears.setText(history.getLamanya());
        tvMedMonths.setText(history.getLamanyaBulan());
        return itemView;
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

        TextView textTv = dialogView.findViewById(R.id.tv_incomplete_form);
        String text = "Mohon lengkapi data riwayat penyakit Anda";
        textTv.setText(text);
        // Get the close button from the custom layout and set its click listener
        Button btnClose = dialogView.findViewById(R.id.btn_close_incomplete);
        btnClose.setOnClickListener(v -> dialog.dismiss());
    }
}