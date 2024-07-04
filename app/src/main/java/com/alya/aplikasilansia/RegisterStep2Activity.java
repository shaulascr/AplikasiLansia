package com.alya.aplikasilansia;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.alya.aplikasilansia.data.inputMedHistory;
import com.alya.aplikasilansia.ui.editprofile.EditProfileViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class RegisterStep2Activity extends AppCompatActivity {
    private static final int REQUEST_PICK_IMAGE = 1;
    private FrameLayout parentLayout;
    private Button buttonAddInput, buttonSave;
    private List<inputMedHistory> inputDataList = new ArrayList<>();
    private LinearLayout firstInputLayout;
    private RelativeLayout setProfileImg;
    private ImageView profileImage;
    private Uri selectedImageUri;
    private RegisterViewModel registerViewModel;
    private EditProfileViewModel editProfileViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_step2);

        parentLayout = findViewById(R.id.input_medhistory);
        buttonAddInput = findViewById(R.id.btn_add_medhistory);
        buttonSave = findViewById(R.id.btn_save_medhistory);
        firstInputLayout = findViewById(R.id.first_input_medhistory);

        setProfileImg = findViewById(R.id.uploadImgReg);
        setProfileImg.setOnClickListener(v-> openGallery());

        profileImage = findViewById(R.id.profile_image_reg);

        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
        editProfileViewModel = new ViewModelProvider(this).get(EditProfileViewModel.class);

        buttonAddInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewInputField();
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInputData();
                registerViewModel.registerHealth1(inputDataList);
                saveProfileChanges();
                Intent register3 = new Intent(RegisterStep2Activity.this, RegisterStep3Activity.class);
                startActivity(register3);
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

        editProfileViewModel.getUpdateResultLiveData().observe(this, updateResult -> {
            Toast.makeText(RegisterStep2Activity.this, updateResult, Toast.LENGTH_SHORT).show();
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

    private void addNewInputField() {
        // Inflate the input field layout and add it to the parent layout
        LayoutInflater inflater = LayoutInflater.from(this);
        View newInputView = inflater.inflate(R.layout.register_input_medhistory, parentLayout, false);
        parentLayout.addView(newInputView);
    }
    private void saveInputData() {
        // Clear the existing data list
        inputDataList.clear();

        // Save the first input field data
        EditText editTextPenyakit = firstInputLayout.findViewById(R.id.et_penyakit);
        EditText editTextLamanya = firstInputLayout.findViewById(R.id.et_lamanya);
        String penyakit = editTextPenyakit.getText().toString();
        String lamanya = editTextLamanya.getText().toString();
        inputDataList.add(new inputMedHistory(penyakit, lamanya));

        // Iterate through all child views in the parent layout
        for (int i = 0; i < parentLayout.getChildCount(); i++) {
            View childView = parentLayout.getChildAt(i);
            editTextPenyakit = childView.findViewById(R.id.et_penyakit);
            editTextLamanya = childView.findViewById(R.id.et_lamanya);

            // Retrieve the input values
            penyakit = editTextPenyakit.getText().toString();
            lamanya = editTextLamanya.getText().toString();

            // Add the input values to the data list
            inputDataList.add(new inputMedHistory(penyakit, lamanya));
//            Log.d("RegisterStep2Activity", "Input " + (i + 1) + " - Penyakit: " + penyakit + ", Lamanya: " + lamanya);

        }
        // Now inputDataList contains all the input data
        // You can use it as needed
        for (int i = 0; i < inputDataList.size(); i++) {
            inputMedHistory input = inputDataList.get(i);
            Log.d("RegisterStep2Activity", "Input " + (i + 1) + " - Penyakit: " + input.penyakit + ", Lamanya: " + input.lamanya);
        }
    }

}