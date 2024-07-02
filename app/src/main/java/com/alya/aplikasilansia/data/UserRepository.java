package com.alya.aplikasilansia.data;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class UserRepository {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private StorageReference mStorage; // Firebase Storage reference

    public UserRepository() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance().getReference("profile_images"); // Storage reference
    }

    public MutableLiveData<User> fetchUser() {
        MutableLiveData<User> userLiveData = new MutableLiveData<>();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        if (firebaseUser != null) {
            String email = firebaseUser.getEmail();
            DatabaseReference userRef = mDatabase.child("users").child(firebaseUser.getUid());

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String birthDate = snapshot.child("birthDate").getValue(String.class);
                        String userName = snapshot.child("userName").getValue(String.class);
                        String imageUrl = snapshot.child("profileImageUrl").getValue(String.class);

                        // Convert string imageUrl to Uri
                        Uri profileImageUri = (imageUrl != null) ? Uri.parse(imageUrl) : null;

                        User userProfile = new User(email, birthDate, userName, profileImageUri);
                        userLiveData.setValue(userProfile);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("UserRepository", "Database error: ", error.toException());
                    userLiveData.setValue(null); // Optionally set the value to null to indicate a failure
                }
            });
        }

        return userLiveData;
    }

    public void register(String email, String password, String birthDate, String userName, MutableLiveData<FirebaseUser> userLiveData, MutableLiveData<String> errorLiveData) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            User additionalUserInfo = new User(email, birthDate, userName, null);
                            mDatabase.child("users").child(user.getUid()).setValue(additionalUserInfo);
                            userLiveData.postValue(user);
                        }
                    } else {
                        errorLiveData.postValue(task.getException().getMessage());
                    }
                });
    }

    public void login(String email, String password, MutableLiveData<FirebaseUser> userLiveData, MutableLiveData<String> errorLiveData) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        userLiveData.postValue(user);
                    } else {
                        errorLiveData.postValue(task.getException().getMessage());
                    }
                });
    }

    public void signOut() {
        mAuth.signOut();
    }

    public void updateProfile(String newUserName, String email, String birthDate, Uri profileImageUri, MutableLiveData<String> updateResultLiveData) {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        if (firebaseUser != null) {
            DatabaseReference userRef = mDatabase.child("users").child(firebaseUser.getUid());
            Map<String, Object> updates = new HashMap<>();
            updates.put("userName", newUserName);
            // Optionally update other fields like email and birthDate
            // Example: updates.put("email", email);
            // Example: updates.put("birthDate", birthDate);

            // Update profile in Realtime Database
            userRef.updateChildren(updates)
                    .addOnSuccessListener(aVoid -> {
                        // Upload profile image if URI provided
                        if (profileImageUri != null) {
                            uploadProfileImage(profileImageUri, firebaseUser.getUid(), updateResultLiveData);
                        } else {
                            updateResultLiveData.postValue("Profile updated successfully");
                        }
                    })
                    .addOnFailureListener(e -> {
                        // Handle failure
                        updateResultLiveData.postValue("Failed to update profile: " + e.getMessage());
                    });
        }
    }
     // Method to upload profile image to Firebase Storage
    private void uploadProfileImage(Uri imageUri, String userId, MutableLiveData<String> imageUrlLiveData) {
        StorageReference profileImageRef = mStorage.child(userId + ".jpg"); // Adjust filename as needed

        profileImageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    profileImageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();

                        // Update profile image URL in Realtime Database
                        mDatabase.child("users").child(userId).child("profileImageUrl").setValue(imageUrl)
                                .addOnSuccessListener(aVoid -> imageUrlLiveData.postValue(imageUrl))
                                .addOnFailureListener(e -> Log.e("UserRepository", "Failed to update profile image URL: " + e.getMessage()));
                    });
                })
                .addOnFailureListener(e -> {
                    // Handle image upload failure
                    Log.e("UserRepository", "Failed to upload profile image: " + e.getMessage());
                });
    }
}


