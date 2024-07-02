package com.alya.aplikasilansia.ui.editprofile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.alya.aplikasilansia.R;
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

public class EditProfileActivity extends AppCompatActivity {

    private static final int REQUEST_PICK_IMAGE = 1; // Define your request code

    private EditProfileViewModel editProfileViewModel;
    private EditText editTextUserName;
    private ImageView imageViewProfile;
    private Uri selectedImageUri; // Uri for storing selected image URI
    private MutableLiveData<String> updateResultLiveData; // LiveData for update result

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Initialize ViewModel
        editProfileViewModel = new ViewModelProvider(this).get(EditProfileViewModel.class);

        // Initialize Views
        editTextUserName = findViewById(R.id.et_profileName);
        imageViewProfile = findViewById(R.id.profile_image_edit);

        findViewById(R.id.btn_cancelProfile).setOnClickListener(v -> finish());
        findViewById(R.id.btn_saveProfile).setOnClickListener(v -> saveProfileChanges());

        imageViewProfile.setOnClickListener(v -> openGallery());

        editProfileViewModel.getUserLiveData().observe(this, user -> {
            if (user != null) {
                editTextUserName.setText(user.getUserName());
                if (user.getProfileImageUrl() != null) {
                    Glide.with(EditProfileActivity.this)
                            .load(user.getProfileImageUrl())
                            .into(imageViewProfile);
                } else {
                    // Handle no profile image case
                    imageViewProfile.setImageResource(R.drawable.img);
                }
            }
        });

        editProfileViewModel.getUpdateResultLiveData().observe(this, updateResult -> {
            Toast.makeText(EditProfileActivity.this, updateResult, Toast.LENGTH_SHORT).show();
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

    // Method to save profile changes
    private void saveProfileChanges() {
        String newUserName = editTextUserName.getText().toString().trim();

        editProfileViewModel.updateProfile(newUserName, null, null, selectedImageUri);
        finish();
    }

    // Method to open gallery for selecting profile image
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_PICK_IMAGE);
    }

    // Handle result from gallery selection for profile image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            imageViewProfile.setImageURI(selectedImageUri);
        }
    }
}
