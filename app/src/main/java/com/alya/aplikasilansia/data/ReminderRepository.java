package com.alya.aplikasilansia.data;

import static android.content.ContentValues.TAG;

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

public class ReminderRepository {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private StorageReference mStorage;

    public ReminderRepository() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance().getReference("reminders");
    }

    public MutableLiveData<Reminder> fetchReminder() {
        MutableLiveData<Reminder> reminderLiveData = new MutableLiveData<>();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        if (firebaseUser != null) {
            DatabaseReference userRef = mDatabase.child("reminders").child(firebaseUser.getUid());

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                       String userId = snapshot.child("userId").getValue(String.class);
                       String title = snapshot.child("title").getValue(String.class);
                       String day = snapshot.child("day").getValue(String.class);
                       String time = snapshot.child("time").getValue(String.class);
                       String desc = snapshot.child("description").getValue(String.class);
                       String timestamp = snapshot.child("timestamp").getValue(String.class);

                       Reminder userReminder = new Reminder(userId, title, day, time, desc, timestamp);
                       reminderLiveData.setValue(userReminder);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("ReminderRepository", "Database error: ", error.toException());
                    reminderLiveData.setValue(null);
                }
            });
        }
        return reminderLiveData;
    }

    public void createReminder(String title, String day, String time, String desc, String timestamp, MutableLiveData<FirebaseUser> reminderLiveData, MutableLiveData<String> errorLiveData) {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser != null) {
            String userId = firebaseUser.getUid();
            Log.d(TAG, "User ID: " + userId); // Log the user ID to verify it's correct
            Reminder reminder = new Reminder(userId, title, day, time, desc, timestamp);
            DatabaseReference remindersRef = mDatabase.child("reminders").child(userId).push();
            remindersRef.setValue(reminder)
                    .addOnSuccessListener(aVoid -> {
                        // Handle success
                        Log.d(TAG, "Reminder added successfully");
                        reminderLiveData.postValue(firebaseUser); // Update the live data with the user
                    })
                    .addOnFailureListener(e -> {
                        // Handle failure
                        Log.e(TAG, "Failed to add reminder: " + e.getMessage());
                        errorLiveData.postValue("Failed to add reminder: " + e.getMessage());
                    });
        } else {
            errorLiveData.postValue("User not authenticated");
        }
    }

}
